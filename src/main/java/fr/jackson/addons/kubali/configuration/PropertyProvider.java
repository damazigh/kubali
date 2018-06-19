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
 * 
 * @author adjebarri
 * @since 0.0.1
 * 
 *        <p>
 *        Configuration interface. To use this library in an other project this
 *        class should be implemented
 *        </p>
 * 
 * @see SpringProvider
 *
 */
public interface PropertyProvider {

	/**
	 * <p>
	 * This is configuration method that should be implemented. it defines the
	 * hierarchical character that is used for nested path
	 * </p>
	 * <u>Example :</u><br/>
	 * : user.address.town : method should return -> <b>.</b>
	 * 
	 * @return The hierarchical separator for nested path filtering
	 */
	String getFieldSplitator();

	/**
	 * <p>
	 * Same as above, configuration method that defines the separator between
	 * the requested fields to bee included in the response
	 * </p>
	 * <u>Example :</u><br/>
	 * : A consumer of webservice specifies these fields : user.address,name,age
	 * : the below method should return -> <b>,</b>
	 * 
	 * @return The separator between the requested fields
	 */
	String getFieldSeparator();

	/**
	 * will be removed in the next release
	 * 
	 * @return The default root filter name
	 */
	@Deprecated
	String getDefaultFilterName();
}
