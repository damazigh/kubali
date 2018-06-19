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

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import fr.jackson.addons.kubali.core.processors.IContext;
import fr.jackson.addons.kubali.model.Filter;

/**
 * 
 * @author adjebarri
 * @since 1.0
 */
public class Context implements IContext {
	private static final Logger LOG = LoggerFactory.getLogger(Context.class);
	private List<String> processedClassName = new ArrayList<>();
	private List<Filter> filters = new ArrayList<>();
	private List<Filter> defaultFilters = new ArrayList<>();
	private boolean processed = false;
	private List<Pair<String, String>> additionalFieldToIgnore = new ArrayList<>();

	@Override
	public void addClassName(String className) {
		if (!isProcess(className)) {
			processedClassName.add(className);
		} else {
			LOG.warn("Attemtping to add an already processed className {}", className);
		}
	}

	@Override
	public boolean isProcess(String className) {
		return processedClassName.contains(className);
	}

	@Override
	public Filter getFilter(String name) {
		return filters.stream().filter(f -> StringUtils.equals(name, f.getFilterName())).findFirst()
				.orElseGet(Filter::new);
	}

	@Override
	public boolean isProcessed() {
		return processed;
	}

	@Override
	public List<Pair<String, SimpleBeanPropertyFilter>> convert() {
		List<Filter> notInFilters = defaultFilters.stream().filter(df -> !isIdInFilter(df.getId()))
				.collect(Collectors.toList());
		filters.addAll(notInFilters);
		return filters.stream().map(f -> {
			if (f.isSerializeAll()) {
				return Pair.of(f.getFilterName(),
						SimpleBeanPropertyFilter.serializeAllExcept(buildAdditionalProsToIgnore(f.getFilterName())));
			} else {
				return Pair.of(f.getFilterName(),
						SimpleBeanPropertyFilter.filterOutAllExcept(f.getPropertyToSerialize()));
			}
		}).collect(Collectors.toList());
	}

	@Override
	public void add(Filter f) {
		Filter filter = getFilter(f.getId());
		if (filter != null) {
			// TODO replace remove by merge
			filters.remove(filter);
		}
		filters.add(f);
	}

	private boolean isIdInFilter(String id) {
		boolean r = filters.stream().anyMatch(f -> StringUtils.equals(f.getId(), id));
		return r;
	}

	@Override
	public boolean exist(String id) {
		return filters.stream().anyMatch(f -> StringUtils.equals(f.getId(), id));
	}

	@Override
	public Filter findById(String id) {
		return filters.stream().filter(f -> StringUtils.equals(f.getId(), id)).findFirst().orElse(null);
	}

	public Filter findDefaultById(String id) {
		return defaultFilters.stream().filter(f -> StringUtils.equals(f.getId(), id)).findFirst().orElse(null);
	}

	@Override
	public void addDefault(Filter f) {
		defaultFilters.add(f);
	}

	@Override
	public void merge(Filter f) {
		OptionalInt optIndex = IntStream.range(0, filters.size()).filter(i -> findById(f.getId()) != null).findFirst();
		if (optIndex.isPresent()) {
			int index = optIndex.getAsInt();
			Set<String> set = Filter.getPropertiesToMerge(f.getPropertyToSerialize(),
					filters.get(index).getPropertyToSerialize());
			set = Filter.getPropertiesToMerge(set, buildAdditionalProsToIgnore(f.getFilterName()));
			filters.get(index).getPropertyToSerialize().addAll(set);
		} else {
			filters.add(f);
		}
	}

	@Override
	public void processed() {
		processed = true;
	}

	@Override
	public void clear() {
		processed = false;
		processedClassName.clear();
		filters.clear();
		additionalFieldToIgnore.clear();
	}

	private Set<String> buildAdditionalProsToIgnore(String filterName) {
		return additionalFieldToIgnore.stream().filter(ad -> StringUtils.equals(ad.getLeft(), filterName))
				.map(p -> p.getRight()).collect(Collectors.toSet());
	}

	@Override
	public void setAdditionalFieldToIgnore(List<Pair<String, String>> props) {
		if (props == null) {
			return;
		}
		this.additionalFieldToIgnore = props;
	}
}
