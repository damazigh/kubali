package fr.jackson.addons.kubali.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
public class PropertyProvider {

	private String defaultFilterName;
	private String fieldSplitator;
	private String fieldSeparator;

	public PropertyProvider(@Value("${kubali.jackson.filter.name:defaultValue}") String defaultFilterName,
			@Value("${kubali.jackson.field.splitator}") String fieldSplitator,
			@Value("${kubali.jackson.field.separator}") String fieldSeparator) {
		this.defaultFilterName = defaultFilterName;
		this.fieldSeparator = fieldSeparator;
		this.fieldSplitator = fieldSplitator;

	}
}
