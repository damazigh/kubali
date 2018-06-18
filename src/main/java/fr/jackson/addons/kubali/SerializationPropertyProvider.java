package fr.jackson.addons.kubali;

import fr.jackson.addons.kubali.configuration.PropertyProvider;

public class SerializationPropertyProvider implements PropertyProvider {
	@Override
	public String getFieldSplitator() {
		return ".";
	}

	@Override
	public String getFieldSeparator() {
		return ",";
	}

	@Override
	public String getDefaultFilterName() {
		return "rootFilter";
	}
}
