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

package fr.jackson.addons.kubali.core.processors.impl;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import fr.jackson.addons.kubali.configuration.PropertyProvider;
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
		context.clear();
		context.processed();
		LOG.info("processing class {} - fields to serialize {}", obj.getClass(), specifiedFields);
		Class<ConditionalSerialization> anno = ConditionalSerialization.class;
		if (introspector.isClassAnnotated(obj.getClass(), anno) && !StringUtils.isEmpty(specifiedFields)) {
			context.addClassName(obj.getClass().getName());
			// build the tree
			TreeNode tree = treeBuilder.build(obj);

			// build the root filter
			Filter filter = new Filter(false);
			filter.setFilterName(Filter.buildFilterName(tree.getClazz().getSimpleName()));
			filter.setId(tree.getClazz().getName());
			context.add(filter);
			// process the tree
			String[] fieldsAsTab = specifiedFields.split(propertyProvider.getFieldSeparator());
			Arrays.stream(fieldsAsTab).forEach(criteria -> {
				Optional<TreeNode> op = treeHelper.findByName(tree, criteria);
				if (existAndFirstLevel(op)) {
					filter.update(criteria);
					context.merge(filter);
				} else if (existAndNotFirstLevel(op)) {
					TreeNode node = op.get();
					String filterName = buildFilterName(node.getClazz());
					Filter actualFilter = context.getFilter(node.getClazz().getName());
					if (StringUtils.isBlank(actualFilter.getId())) {
						actualFilter.setFilterName(filterName);
						actualFilter.setId(node.getClazz().getName());
						actualFilter.setSerializeAll(false);
					}
					actualFilter.update(getNameFromCriteria(criteria, node));
					context.merge(actualFilter);
				}
			});
		}
	}

	private boolean existAndFirstLevel(Optional<TreeNode> op) {
		return op.isPresent() && !op.get().isNullValue() && op.get().getParent() == null;
	}

	private boolean existAndNotFirstLevel(Optional<TreeNode> op) {
		return op.isPresent() && !op.get().isNullValue() && op.get().getParent() != null;
	}

	private <T> String buildFilterName(Class<T> clazz) {
		UUID uuid = UUID.randomUUID();
		String result = new StringBuilder(uuid.toString()).append(clazz.getSimpleName()).toString();
		LOG.debug("Generated name for class {} : {}", clazz.getSimpleName(), result);
		return clazz.getSimpleName() + "Filter";
	}

	private String getNameFromCriteria(String criteria, TreeNode node) {
		if (StringUtils.isNotEmpty(criteria) && StringUtils.contains(criteria, propertyProvider.getFieldSplitator())) {
			String[] tab = StringUtils.split(criteria, propertyProvider.getFieldSplitator());
			for (int i = 0; i < tab.length - 1; i++) {
				avoidParentFiltering(node, tab[i]);
			}
			LOG.info("Criteria : {} - retrieved name {}", criteria, tab[tab.length - 1]);
			return tab[tab.length - 1];
		}
		LOG.warn("Unable to retrieve the filed name from criteria");
		return "";
	}

	private void avoidParentFiltering(TreeNode node, String criteria) {
		Filter parent = context.findById(node.getParent().getClazz().getName());
		if (parent != null) {
			parent.update(criteria);
			context.merge(parent);
		}
	}
}
