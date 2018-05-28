package fr.jackson.addons.kubali.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.core.ITreeHelper;
import fr.jackson.addons.kubali.core.impl.Introspector;
import fr.jackson.addons.kubali.core.impl.Invoker;
import fr.jackson.addons.kubali.core.impl.TreeBuilder;
import fr.jackson.addons.kubali.core.impl.TreeHelper;
import fr.jackson.addons.kubali.core.processors.IContext;
import fr.jackson.addons.kubali.core.processors.IProcessor;
import fr.jackson.addons.kubali.core.processors.impl.Context;
import fr.jackson.addons.kubali.core.processors.impl.DefaultProcessor;

@Configuration
public class SpringProvider {

	@Bean
	public ITreeBuilder treeBuilder() {
		return new TreeBuilder();
	}

	@Bean
	public IIntrospector introspector() {
		return new Introspector();
	}

	@Bean
	public IInvoker invoker() {
		return new Invoker();
	}

	@Bean
	public ITreeHelper treeHelper() {
		return new TreeHelper();
	}

	@Bean
	public IContext context() {
		return new Context();
	}

	@Bean
	public IProcessor processor() {
		return new DefaultProcessor();
	}
}
