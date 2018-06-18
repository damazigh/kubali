/*
 * Copyright 2018 Amazigh DJEBARRI

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package fr.jackson.addons.kubali.core.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.jackson.addons.kubali.core.Constants;
import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.model.TreeNode;
import lombok.Setter;

public class TreeBuilder implements ITreeBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(TreeBuilder.class);
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
		populate(tree, true, obj, null, null);
		fields.forEach(f -> tree.getChildren().add(doProcess(f, obj, tree)));
		return tree;
	}

	private TreeNode doProcess(Field field, Object parent, TreeNode parentNode) {
		// get the field value object
		Object obj = invoker.invokeGetter(field, parent);
		TreeNode actualNode = new TreeNode();
		populate(actualNode, false, obj, parentNode, field);
		actualNode.setFieldName(introspector.findName(field));
		if (obj != null && !actualNode.isSimple() && actualNode.hasNested()) {
			actualNode.setNullValue(false);
			// handle simple field as String attributes
			introspector.introspect(obj, true).parallelStream()
					.forEach(f -> actualNode.getChildren().add(handleSimpleField(f, actualNode)));
			// handle collection fields as List
			introspector.introspect(obj, Collection.class).parallelStream()
					.forEach(f -> actualNode.getChildren().add(handleCollection(f, obj, actualNode)));
			// TODO Handle arrays
			if (actualNode.hasNested()) {
				List<TreeNode> recursiveResult = introspector.introspect(obj, false).stream()
						.map(f -> doProcess(f, obj, actualNode)).collect(Collectors.toList());
				actualNode.getChildren().addAll(recursiveResult);
			}
		}
		return actualNode;
	}

	private TreeNode handleSimpleField(Field f, TreeNode parent) {
		TreeNode simpleNode = new TreeNode();
		simpleNode.setNested(false);
		simpleNode.setRoot(false);
		simpleNode.setSimple(true);
		simpleNode.setFieldName(introspector.findName(f));
		simpleNode.setClazz(f.getType());
		simpleNode.setParent(parent);
		return simpleNode;
	}

	private TreeNode handleCollection(Field f, Object o, TreeNode parent) {
		if (f == null) {
			throw new IllegalArgumentException("unable to handle fied which are null !");
		}
		TreeNode node = null;
		LOG.debug("trying to retrieve attached object to field");
		Object obj = invoker.invokeGetter(f, o);
		if (obj != null) {
			Method m = introspector.findMethod(Constants.LIST_GET_METHOD_NAME, obj.getClass());
			LOG.debug("obj retrieved and it's not null");
			if (Collection.class.isAssignableFrom(obj.getClass()) && m != null) {
				Object inner = invoker.invokeMethod(m, new Object[] { 0 }, obj);
				if (inner != null) {
					node = new TreeNode();
					node.setFieldName(introspector.findName(f));
					node.setParent(parent);
					node.setClazz(inner.getClass());
					// TODO handle collection of collection
					node.setCollection(false);
					node.setNested(introspector.hasNested(inner));
					node.setSimple(!introspector.isNotBaseType(obj.getClass()));

					if (node.hasNested()) {
						final TreeNode nodeToProcess = node;
						List<TreeNode> recursiveResult = introspector.introspect(inner, false).stream()
								.map(fld -> doProcess(fld, obj, nodeToProcess)).collect(Collectors.toList());
						node.getChildren().addAll(recursiveResult);
					}

				} else {
					node = handleNullOrEmptyCollection(f, parent);
				}
			} else {
				node = handleNullOrEmptyCollection(f, parent);
			}
		} else {
			node = handleNullOrEmptyCollection(f, parent);
		}
		return node;
	}

	private TreeNode handleNullOrEmptyCollection(Field f, TreeNode parent) {
		TreeNode tn = new TreeNode();
		tn.setFieldName(introspector.findName(f));
		tn.setNullValue(true);
		tn.setClazz(f.getType());
		tn.setSimple(true);
		tn.setNested(false);
		tn.setRoot(false);
		tn.setParentHidden(false);
		tn.setParent(parent);
		return tn;
	}

	private void populate(TreeNode node, boolean isRoot, Object obj, TreeNode parent, Field f) {
		if (obj != null) {
			node.setNested(introspector.hasNested(obj));
			node.setSimple(!introspector.isNotBaseType(obj.getClass()));
			node.setNullValue(false);
			node.setClazz(obj.getClass());
		} else {
			// by default null value will be considered as simple value
			node.setNested(false);
			node.setSimple(true);
			node.setNullValue(true);
			if (f != null) {
				node.setClazz(f.getType());
			}
		}
		node.setRoot(isRoot);
		node.setParent(parent);
	}
}
