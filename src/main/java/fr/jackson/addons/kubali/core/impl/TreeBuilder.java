package fr.jackson.addons.kubali.core.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.model.TreeNode;
import lombok.Setter;

@Component
public class TreeBuilder implements ITreeBuilder {

	@Setter
	@Autowired
	private IIntrospector introspector;

	@Setter
	@Autowired
	private IInvoker invoker;

	@Override
	public <T> TreeNode build(T obj) {
		TreeNode tree = new TreeNode();
		List<Field> fields = introspector.introspect(obj);
		populate(tree, true, obj);
		fields.forEach(f -> tree.getChildren().add(doProcess(f, obj, tree)));
		return null;
	}

	private TreeNode doProcess(Field field, Object parent, TreeNode parentNode) {
		// get the field value object
		Object obj = invoker.invokeGetter(field, parent);
		TreeNode actualNode = new TreeNode();
		populate(actualNode, false, obj);
		actualNode.setFieldName(introspector.findName(field));
		if (obj != null && !actualNode.isSimple() && actualNode.hasNested()) {
			actualNode.setNullValue(false);
			introspector.introspect(obj, true).parallelStream()
					.forEach(f -> actualNode.getChildren().add(handleSimpleField(f)));
			if (actualNode.hasNested()) {
				List<TreeNode> recursiveResult = introspector.introspect(obj, false).stream()
						.map(f -> doProcess(f, obj, actualNode)).collect(Collectors.toList());
				actualNode.getChildren().addAll(recursiveResult);
			}
		}
		return actualNode;
	}

	private TreeNode handleSimpleField(Field f) {
		TreeNode simpleNode = new TreeNode();
		simpleNode.setNested(false);
		simpleNode.setRoot(false);
		simpleNode.setSimple(true);
		simpleNode.setFieldName(introspector.findName(f));
		return simpleNode;
	}

	private void populate(TreeNode node, boolean isRoot, Object obj) {
		if (obj != null) {
			node.setNested(introspector.hasNested(obj));
			node.setSimple(!introspector.isNotBaseType(obj.getClass()));
			node.setNullValue(false);
		} else {
			// by default null value will be considered as simple value
			node.setNested(false);
			node.setSimple(true);
			node.setNullValue(true);
		}
		node.setRoot(isRoot);
	}
}
