package fr.jackson.addons.kubali.configuration.defaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.jackson.addons.kubali.configuration.ClasspathProcessing;
import fr.jackson.addons.kubali.configuration.PropertyProvider;
import fr.jackson.addons.kubali.configuration.SpringProvider;

@Configuration
public class SerializationProviderConf extends SpringProvider {
	@Override
	@Bean
	public ClasspathProcessing classpathProcessing() {
		return () -> "fr.jackson.addons.kubali.dtos";
	}

	@Bean
	@Override
	public PropertyProvider propertyProvider() {
		return new SerializationPropertyProvider();
	}
}
