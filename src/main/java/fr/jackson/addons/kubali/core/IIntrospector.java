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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface IIntrospector {
	<T, V extends Annotation> boolean isClassAnnotated(Class<T> clazz, Class<V> annotationClass);

	<T extends Annotation> boolean isFieldAnnotated(Field f, Class<T> annotationClass);

	String findName(Field f);

	/**
	 * 
	 * @param obj
	 *            an object to introspect
	 * @return all the declared fields in this class (the inherited attributes
	 *         will not appears)
	 */
	List<Field> introspect(Object obj);

	List<Field> introspect(Object obj, boolean javaBaseType);

	<T> List<Field> introspect(Object obj, Class<T> clazz);

	<T extends Annotation> List<Field> introspect(Object obj, Class<T> clazz, boolean notNull);

	<T, V extends Annotation> boolean addAnnotationToClass(Class<T> clazz, Class<V> annotationClass, String attrName,
			String value);

	<T> Method findMethod(String name, Class<T> clazz);

	default <T> boolean isNotBaseType(Class<T> clazz) {
		return !(String.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)
				|| Integer.class.isAssignableFrom(clazz)) || isCollection(clazz);
	}

	default boolean hasNested(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("The object passed as parameter must be not null");
		}
		return (isNotBaseType(o.getClass()) && Arrays.stream(o.getClass().getDeclaredFields()).count() > 0);
	}

	default <T> boolean isCollection(Class<T> clazz) {
		return Collection.class.isAssignableFrom(clazz);
	}

	default <T> boolean isASubTypeOf(Field f, Class<T> clazz) {
		if (f == null) {
			throw new IllegalArgumentException("The field passed as parameter must be not null");
		} else if (clazz == null) {
			throw new IllegalArgumentException("The class passed as parameter must be not null");
		}
		return clazz.isAssignableFrom(f.getType());
	}

	<T extends Annotation> void AddAnnoToClassInPackage(String basePackage, Class<T> annotated) throws IOException;

}
