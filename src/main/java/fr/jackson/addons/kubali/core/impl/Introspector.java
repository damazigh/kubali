package fr.jackson.addons.kubali.core.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class Introspector implements IIntrospector {

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
	public boolean isCollection(Field f) {
		return false;
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

		return Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(JsonIgnore.class) != null)
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
			return false;
		}
	}

	@Override
	public <T extends Annotation> List<Field> introspect(Object obj, Class<T> clazz, boolean notNull) {
		List<Field> allFields = introspect(obj);

		return allFields.stream().filter(f -> {
			Annotation ann = f.getAnnotation(clazz);
			boolean result = notNull ? ann != null : ann == null;
			return result;
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
			boolean result = javaBaseType ? isNotBaseType(invocationResult.getClass())
					: !isNotBaseType(invocationResult.getClass());
			return result;
		}).collect(Collectors.toList());
	}

}
