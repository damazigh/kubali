package fr.jackson.addons.kubali.core.processors.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonFilter;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import fr.jackson.addons.kubali.configuration.PropertyProvider;
import fr.jackson.addons.kubali.core.Constants;
import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.core.processors.IProcessor;
import fr.jackson.addons.kubali.model.TreeNode;
import lombok.Setter;

public class DefaultProcessor implements IProcessor {

	@Setter
	@Autowired
	private IIntrospector introspector;

	@Setter
	@Autowired
	private PropertyProvider propertyProvider;

	@Setter
	@Autowired
	private ITreeBuilder treeBuilder;

	@Override
	public void process(Object obj) {
		if (obj == null) {
			return;
		}
		Class<ConditionalSerialization> anno = ConditionalSerialization.class;
		if (introspector.isClassAnnotated(obj.getClass(), anno)) {
			introspector.addAnnotationToClass(obj.getClass(), JsonFilter.class, Constants.JSON_FILTER_VALUE_ATTR,
					propertyProvider.getDefaultFilterName());
			TreeNode tree = treeBuilder.build(obj);
		}
	}

}
