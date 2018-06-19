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

package fr.jackson.addons.kubali.configuration;

/**
 * @since 0.0.1
 * @author adjebarri
 *         <p>
 *         A class that allows to define the root base package where the
 *         entities are declared.
 *         </p>
 * @see PropertyProvider
 * @see SpringProvider
 */
@FunctionalInterface
public interface ClasspathProcessing {
	String baseModelPackage();
}
