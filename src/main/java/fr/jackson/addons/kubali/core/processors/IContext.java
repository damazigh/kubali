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

package fr.jackson.addons.kubali.core.processors;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import fr.jackson.addons.kubali.model.Filter;

public interface IContext {
	void addClassName(String className);

	boolean isProcess(String className);

	Filter getFilter(String name);

	boolean isProcessed();

	List<Pair<String, SimpleBeanPropertyFilter>> convert();

	void add(Filter f);

	boolean exist(String id);

	Filter findById(String id);

	void addDefault(Filter f);

	void merge(Filter f);

	void clear();

	void processed();
}
