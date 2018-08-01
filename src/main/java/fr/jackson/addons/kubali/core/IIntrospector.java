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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * introspection tool that provide some method to introspect object and class it
 * use the java reflection api
 * 
 * @author adjebarri
 *
 */
public interface IIntrospector {
	/**
	 * test if a given class is annotated with a specific annotation class
	 * 
	 * @param clazz
	 *            the class to test
	 * @param annotationClass
	 *            annotation to test the class with
	 * @return true if the class is annotated with the annotation class false
	 *         otherwise
	 */
	<T, V extends Annotation> boolean isClassAnnotated(Class<T> clazz, Class<V> annotationClass);

	/**
	 * same as above except that the test is made on a field not on a class
	 * 
	 * @param f
	 *            the field to test
	 * @param annotationClass
	 *            annotation to test the field with
	 * @return true if the field is annotated with the annotation class false
	 *         otherwise
	 */
	<T extends Annotation> boolean isFieldAnnotated(Field f, Class<T> annotationClass);

	/**
	 * find the name of given field
	 * 
	 * @param f
	 * @return for a given field return the {@link JsonProperty#value()} if null
	 *         return the field name
	 */
	String findName(Field f);

	/**
	 * 
	 * @param obj
	 *            an object to introspect
	 * @return all the declared fields in this class (the inherited attributes will
	 *         not appears)
	 */
	List<Field> introspect(Object obj);

	/**
	 * introspect an object an return all its complex / simple inner attributes
	 * 
	 * @param obj
	 *            the object to introspect
	 * @param javaBaseType
	 *            true for simple property / false to complex property
	 * @return a set of {@link Field}
	 */
	List<Field> introspect(Object obj, boolean javaBaseType);

	/**
	 * introspect an object an return all its attribute which are a subtype of the
	 * given class as parameter
	 * 
	 * @param obj
	 *            the object to introspect
	 * @param clazz
	 *            the super type of all attributes that should be returned
	 * @return the object to introspect
	 */
	<T> List<Field> introspect(Object obj, Class<T> clazz);

	/**
	 * same as above in addition if the flag not null is set only the not null
	 * attribute will be returned
	 * 
	 * @param obj
	 *            the object to instrospect
	 * @param clazz
	 *            the super type
	 * @param notNull
	 *            nullable flag
	 * @return the result of introspection
	 */
	<T extends Annotation> List<Field> introspect(Object obj, Class<T> clazz, boolean notNull);

	<T, V extends Annotation> boolean addAnnotationToClass(Class<T> clazz, Class<V> annotationClass, String attrName,
			String value);

	/**
	 * find a method by it's given name
	 * 
	 * @param name
	 * @param clazz
	 * @return the {@link Method} if exist null otherwise
	 */
	<T> Method findMethod(String name, Class<T> clazz);

	/**
	 * default method that defines if a clazz is not java base type
	 * 
	 * @param clazz
	 * @return true if the class is not assignable from {@link Integer},
	 *         {@link Double}, {@link String} AND the class isn't a sub type of
	 *         {@link Collection}
	 */
	default <T> boolean isNotBaseType(Class<T> clazz) {
		return !(String.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)
				|| Integer.class.isAssignableFrom(clazz)) || isCollection(clazz);
	}

	/**
	 * method to determine if an object has inner attributes
	 * 
	 * @param o
	 *            the object to scan, if null throws
	 *            {@link IllegalArgumentException}
	 * @return true if the {@link IIntrospector#isNotBaseType(Class)} return true
	 *         AND has at least one field to be instrospected, false otherwise
	 */
	default boolean hasNested(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("The object passed as parameter must be not null");
		}
		return (isNotBaseType(o.getClass()) && Arrays.stream(o.getClass().getDeclaredFields()).count() > 0);
	}

	/**
	 * determine if a class is collection
	 * 
	 * @param clazz
	 * @return true if the {@link Collection} is assignable from the class given as
	 *         parameter
	 */
	default <T> boolean isCollection(Class<T> clazz) {
		return Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * 
	 * @param f
	 * @param clazz
	 * @return
	 */
	default <T> boolean isASubTypeOf(Field f, Class<T> clazz) {
		if (f == null) {
			throw new IllegalArgumentException("The field passed as parameter must be not null");
		} else if (clazz == null) {
			throw new IllegalArgumentException("The class passed as parameter must be not null");
		}
		return clazz.isAssignableFrom(f.getType());
	}

	// TODO rename this method
	<T extends Annotation> void AddAnnoToClassInPackage(String basePackage, Class<T> annotated) throws IOException;

}
