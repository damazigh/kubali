package fr.jackson.addons.kubali.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface IIntrospector {
	<T, V extends Annotation> boolean isClassAnnotated(Class<T> clazz, Class<V> annotationClass);

	<T extends Annotation> boolean isFieldAnnotated(Field f, Class<T> annotationClass);

	boolean isCollection(Field f);

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

	<T extends Annotation> List<Field> introspect(Object obj, Class<T> clazz, boolean notNull);

	<T, V extends Annotation> boolean addAnnotationToClass(Class<T> clazz, Class<V> annotationClass, String attrName,
			String value);

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

}