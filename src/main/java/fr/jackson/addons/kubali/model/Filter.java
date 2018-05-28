package fr.jackson.addons.kubali.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {
	@Getter(value = AccessLevel.NONE)
	private Set<String> propertyToSerialize;
	private String filterName;

	public Set<String> getPropertyToSerialize() {
		if (propertyToSerialize == null) {
			propertyToSerialize = new HashSet<>();
		}
		return propertyToSerialize;
	}
}
