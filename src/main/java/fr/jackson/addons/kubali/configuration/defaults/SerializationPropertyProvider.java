package fr.jackson.addons.kubali.configuration.defaults;

import org.springframework.context.annotation.Configuration;

import fr.jackson.addons.kubali.configuration.PropertyProvider;

@Configuration
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
