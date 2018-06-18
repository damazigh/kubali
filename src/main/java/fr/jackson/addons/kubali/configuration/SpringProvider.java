/*
 * Copyright [2018] [Amazigh DJEBARRI]

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

package fr.jackson.addons.kubali.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.jackson.addons.kubali.core.IFacade;
import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import fr.jackson.addons.kubali.core.ITranslator;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.core.ITreeHelper;
import fr.jackson.addons.kubali.core.impl.Facade;
import fr.jackson.addons.kubali.core.impl.Introspector;
import fr.jackson.addons.kubali.core.impl.Invoker;
import fr.jackson.addons.kubali.core.impl.Translator;
import fr.jackson.addons.kubali.core.impl.TreeBuilder;
import fr.jackson.addons.kubali.core.impl.TreeHelper;
import fr.jackson.addons.kubali.core.processors.IContext;
import fr.jackson.addons.kubali.core.processors.IProcessor;
import fr.jackson.addons.kubali.core.processors.impl.Context;
import fr.jackson.addons.kubali.core.processors.impl.DefaultProcessor;

@Configuration
public abstract class SpringProvider {

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

	@Bean
	public ITranslator translator() {
		return new Translator();
	}

	@Bean
	public IFacade facade() {
		return new Facade();
	}

	@Bean
	public abstract ClasspathProcessing classpathProcessing();

	@Bean
	public abstract PropertyProvider propertyProvider();
}
