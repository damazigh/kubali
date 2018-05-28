package fr.jackson.addons.kubali.core.processors.impl;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonFilter;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import fr.jackson.addons.kubali.configuration.PropertyProvider;
import fr.jackson.addons.kubali.core.Constants;
import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.core.ITreeHelper;
import fr.jackson.addons.kubali.core.processors.IProcessor;
import fr.jackson.addons.kubali.model.Filter;
import fr.jackson.addons.kubali.model.TreeNode;
import lombok.Setter;

public class DefaultProcessor implements IProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessor.class);

	@Setter
	@Autowired
	private IIntrospector introspector;

	@Setter
	@Autowired
	private PropertyProvider propertyProvider;

	@Setter
	@Autowired
	private ITreeBuilder treeBuilder;

	@Setter
	@Autowired
	private ITreeHelper treeHelper;

	@Setter
	@Autowired
	private Context context;

	@Override
	public void process(Object obj, String specifiedFields) {
		if (obj == null) {
			return;
		}
		LOG.info("processing class {} - fields to serialize {}", obj.getClass(), specifiedFields);
		Class<ConditionalSerialization> anno = ConditionalSerialization.class;
		if (introspector.isClassAnnotated(obj.getClass(), anno) && !StringUtils.isEmpty(specifiedFields)) {
			introspector.addAnnotationToClass(obj.getClass(), JsonFilter.class, Constants.JSON_FILTER_VALUE_ATTR,
					propertyProvider.getDefaultFilterName());
			Filter filter = new Filter();
			filter.setFilterName(Constants.JSON_FILTER_VALUE_ATTR);
			context.addClassName(obj.getClass().getName());
			TreeNode tree = treeBuilder.build(obj);
			String[] fieldsAsTab = specifiedFields.split(propertyProvider.getFieldSeparator());
			Arrays.stream(fieldsAsTab).forEach(criteria -> {
				Optional<TreeNode> op = treeHelper.findByName(tree, criteria);
				if (op.isPresent() && !op.get().isNullValue()) {
					filter.getPropertyToSerialize().add(op.get().getFieldName());
				}
			});

		}
	}

}
