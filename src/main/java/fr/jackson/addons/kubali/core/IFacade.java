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

package fr.jackson.addons.kubali.core;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * Entry point class that is called at spring's start up
 * 
 * @author adjebarri
 * @since 0.0.1
 *
 */
public interface IFacade {
	SimpleFilterProvider process(Object obj, String fieldsToSerialize);

	/**
	 * A method that allows an additional filtering mechanism. it could be useful to
	 * avoid duplication of class.
	 * 
	 * @param props
	 *            a set of pair object (filterName <=> property to ignore)
	 */
	void setPropertyToIgnore(List<Pair<String, String>> props);
}
