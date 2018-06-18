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

package fr.jackson.addons.kubali.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filter other = (Filter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Getter(value = AccessLevel.NONE)
	private Set<String> propertyToSerialize;
	private String filterName;
	// the complete name as uid
	private String id;
	private boolean serializeAll = true;

	public Set<String> getPropertyToSerialize() {
		if (propertyToSerialize == null) {
			propertyToSerialize = new HashSet<>();
		}
		return propertyToSerialize;
	}

	public void update(String property) {
		if (propertyToSerialize == null) {
			propertyToSerialize = new HashSet<>();
		}
		boolean exist = propertyToSerialize.stream().anyMatch(p -> p.equals(property));
		if (!exist) {
			propertyToSerialize.add(property);
		}
	}

	public static Set<String> getPropertiesToMerge(Set<String> set1, Set<String> set2) {
		if (set1 == null || set2 == null) {
			throw new IllegalArgumentException("Cannot apply merge on null set");
		}
		return set1.stream().filter(p -> !set2.contains(p)).collect(Collectors.toSet());
	}

	public static Filter defaultOf(String id, String name) {
		Filter f = new Filter();
		f.setSerializeAll(true);
		f.setFilterName(name);
		f.setId(id);
		return f;
	}

	public static String buildFilterName(String classname) {
		String result = classname + "Filter";
		return result;
	}

	public Filter(boolean status) {
		serializeAll = false;
	}

	public Filter() {
	}
}
