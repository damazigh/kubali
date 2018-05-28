package fr.jackson.addons.kubali.core.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.Setter;

public class Introspector implements IIntrospector {
	private static final Logger LOG = LoggerFactory.getLogger(Introspector.class);

	@Setter
	@Autowired
	private IInvoker invoker;

	@Override
	public <T, V extends Annotation> boolean isClassAnnotated(Class<T> clazz, Class<V> annotationClass) {
		return clazz.getAnnotation(annotationClass) != null ? true : false;
	}

	@Override
	public <T extends Annotation> boolean isFieldAnnotated(Field f, Class<T> annotationClass) {
		return f.getAnnotation(annotationClass) != null ? true : false;
	}

	@Override
	public String findName(Field f) {
		JsonProperty ann = f.getAnnotation(JsonProperty.class);
		if (ann != null) {
			return ann.value();
		}
		return f.getName();
	}

	@Override
	public List<Field> introspect(Object obj) {

		return Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(JsonIgnore.class) == null)
				.collect(Collectors.toList());
	}

	@Override
	public <T, V extends Annotation> boolean addAnnotationToClass(Class<T> clazz, Class<V> annotationClass,
			String attrName, String value) {
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass ct = pool.getCtClass(clazz.getName());
			CtClass annoCtClass = pool.getCtClass(annotationClass.getName());
			ClassFile cf = ct.getClassFile();
			ConstPool cp = cf.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			javassist.bytecode.annotation.Annotation jassistAnnotation = new javassist.bytecode.annotation.Annotation(
					cp, annoCtClass);
			jassistAnnotation.addMemberValue(attrName, new StringMemberValue(value, cp));
			attr.setAnnotation(jassistAnnotation);
			cf.addAttribute(attr);
			return true;
		} catch (NotFoundException e) {
			LOG.error("error occured during adding annotation to {}", clazz.getSimpleName(), e);
			return false;
		}
	}

	@Override
	public <T extends Annotation> List<Field> introspect(Object obj, Class<T> clazz, boolean notNull) {
		List<Field> allFields = introspect(obj);

		return allFields.stream().filter(f -> {
			Annotation ann = f.getAnnotation(clazz);
			return notNull ? ann != null : ann == null;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Field> introspect(Object obj, boolean javaBaseType) {
		List<Field> allFields = introspect(obj);
		return allFields.stream().filter(field -> {
			Object invocationResult = invoker.invokeGetter(field, obj);
			if (invocationResult == null) {
				return false;
			}
			return (javaBaseType ? isNotBaseType(invocationResult.getClass())
					: !isNotBaseType(invocationResult.getClass())) && !isASubTypeOf(field, Collection.class);
		}).collect(Collectors.toList());
	}

	@Override
	public <T> List<Field> introspect(Object obj, Class<T> clazz) {
		List<Field> allFields = introspect(obj);
		return allFields.stream().filter(field -> isASubTypeOf(field, clazz)).collect(Collectors.toList());
	}

	@Override
	public <T> Method findMethod(String name, Class<T> clazz) {
		try {
			return clazz.getDeclaredMethod(name);
		} catch (NoSuchMethodException e) {
			LOG.debug("cannot find the method {} in the class {}", name, clazz.getSimpleName());
			LOG.error("Error", e);
		} catch (SecurityException e) {
			LOG.error("the class has a security manager attached to it cannot reflect the method", e);
		}
		return null;
	}

}
