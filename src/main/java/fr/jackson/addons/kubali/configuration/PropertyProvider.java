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

	public PropertyProvider(@Value("${kubali.jackson.filter.name:defaultValue}") String defaulFilterName) {
		this.defaultFilterName = defaulFilterName;
	}
}
