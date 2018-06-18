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

package fr.jackson.addons.kubali.core.impl;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import fr.jackson.addons.kubali.configuration.ClasspathProcessing;
import fr.jackson.addons.kubali.core.IFacade;
import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.ITranslator;
import fr.jackson.addons.kubali.core.processors.IProcessor;
import lombok.Setter;

public class Facade implements IFacade {

	@Setter
	@Autowired
	private IProcessor processor;

	@Setter
	@Autowired
	private ITranslator translator;

	@Setter
	@Autowired
	private IIntrospector introspector;

	@Setter
	@Autowired
	private ClasspathProcessing classpathProcessor;

	@Override
	public SimpleFilterProvider process(Object obj, String fieldsToSerialize) {
		processor.process(obj, fieldsToSerialize);
		return translator.translate();
	}

	@PostConstruct
	private void postConstruct() throws IOException {
		introspector.AddAnnoToClassInPackage(classpathProcessor.baseModelPackage(), ConditionalSerialization.class);
	}
}
