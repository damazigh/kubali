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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * interface that provides some useful method in order to invoke a field method
 * in a non nullable object
 * 
 * @author adjebarri
 * @since 0.0.1
 *
 */
public interface IInvoker {
	/**
	 * 
	 * @param field
	 *            an attribute of object (result of the introspection)
	 * @param obj
	 *            the introspected object
	 * @return call the getter of the field, if an exception occurs return null
	 */
	Object invokeGetter(Field field, Object obj);

	/**
	 * 
	 * @param m
	 *            targeted method to execute
	 * @param args
	 *            the taken method argument
	 * @param o
	 *            the object where to apply the methpd
	 * @return result of execution, if an exception occurs return null
	 */
	Object invokeMethod(Method m, Object[] args, Object o);
}
