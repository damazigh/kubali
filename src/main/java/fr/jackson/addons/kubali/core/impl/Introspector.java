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

package fr.jackson.addons.kubali.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import fr.jackson.addons.kubali.core.processors.IContext;
import fr.jackson.addons.kubali.model.Filter;
import javassist.CannotCompileException;
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

	@Setter
	@Autowired
	private IContext context;

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
			ct.defrost();
			CtClass annoCtClass = pool.getCtClass(annotationClass.getName());
			ClassFile cf = ct.getClassFile();
			ConstPool cp = cf.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			javassist.bytecode.annotation.Annotation jassistAnnotation = new javassist.bytecode.annotation.Annotation(
					cp, annoCtClass);
			jassistAnnotation.addMemberValue(attrName, new StringMemberValue(value, cp));
			attr.setAnnotation(jassistAnnotation);
			cf.addAttribute(attr);
			ct.toClass();
			ct.freeze();
			return true;
		} catch (NotFoundException | CannotCompileException e) {
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
			return clazz.getMethod(name, new Class[] { int.class });
		} catch (NoSuchMethodException e) {
			LOG.debug("cannot find the method {} in the class {}", name, clazz.getSimpleName());
			LOG.error("Error", e);
		} catch (SecurityException e) {
			LOG.error("the class has a security manager attached to it cannot reflect the method", e);
		}
		return null;
	}

	@Override
	public <T extends Annotation> void AddAnnoToClassInPackage(String basePackage, Class<T> annotated)
			throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			throw new IllegalArgumentException("classloader is null !");
		}
		String path = StringUtils.replace(basePackage, ".", File.separator);
		Enumeration<URL> resources = cl.getResources(path);
		List<File> dirs = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL res = resources.nextElement();
			LOG.debug("File added to directory - {}", res.getFile());
			dirs.add(new File(URLDecoder.decode(res.getFile(), "utf-8")));
		}
		List<Class<?>> classToProcess = dirs.stream().map(d -> {
			if (StringUtils.split(d.getPath(), "!").length > 1 && StringUtils.contains(d.getPath(), "jar")) {
				// jar file
				try {
					return findClassAnnotatedWithInJar(d, basePackage, annotated).stream();
				} catch (IOException e) {
					LOG.error("Something went wrong while processing the jar", d.getPath(), e);
				}
			}
			return findClassesAnnotatedWith(d, basePackage, annotated).stream();
		}).flatMap(d -> d).collect(Collectors.toList());
		classToProcess.forEach(
				c -> context.addDefault(Filter.defaultOf(c.getName(), Filter.buildFilterName(c.getSimpleName()))));
	}

	private <T extends Annotation> List<Class<?>> findClassesAnnotatedWith(File dir, String packageName,
			Class<T> annotatedWith) {
		List<Class<?>> classes = new ArrayList<>();
		if (!dir.exists()) {
			LOG.warn("The dir doesn't exist - {}, it will be ignored", dir.getName());
			return classes;
		}
		Arrays.stream(dir.listFiles()).forEach(f -> {
			if (f.isDirectory() && !StringUtils.contains(f.getName(), ".")) {
				classes.addAll(findClassesAnnotatedWith(f, packageName + "." + f.getName(), annotatedWith));
			} else if (f.getName().endsWith(".class")) {
				try {
					String classname = packageName + '.' + f.getName().substring(0, f.getName().length() - 6);
					LOG.debug("Processing class - {}", classname);
					Class<?> cl = Class.forName(classname);
					if (cl.getAnnotation(annotatedWith) != null) {
						classes.add(cl);
					} else {
						LOG.debug("class {} not annotated with {}", cl.getSimpleName(), annotatedWith.getSimpleName());
					}
				} catch (ClassNotFoundException e) {
					LOG.error("error while trying to return class associating to file : {}", f.getName(), e);
				}
			}
		});
		return classes;
	}

	private <T extends Annotation> List<Class<?>> findClassAnnotatedWithInJar(File jarFile, String packageName,
			Class<T> annotatedWith) throws IOException {
		List<Class<?>> classes = new ArrayList<>();
		String toRemove = "!" + File.separator + StringUtils.replace(packageName, ".", File.separator);
		String jar = StringUtils.remove(jarFile.getPath(), toRemove);
		if (!StringUtils.endsWith(jar, ".jar")) {
			return classes;
		}
		JarInputStream jis = null;
		try {
			String str = removeFileProtocole(jar);
			File f = new File(str);
			jis = new JarInputStream(new FileInputStream(f));
			JarEntry next = jis.getNextJarEntry();
			while (next != null) {
				String name = StringUtils.replace(next.getName(), "/", ".");
				LOG.debug("scanning the entry {}", next.getName());
				if (StringUtils.startsWith(name, packageName) && StringUtils.endsWith(next.getName(), ".class")) {
					String className = StringUtils.substring(name, 0, name.length() - 6);
					Class<?> cl = Class.forName(className);
					classes.add(cl);
				}
				next = jis.getNextJarEntry();
			}
			return classes;
		} catch (ClassNotFoundException e) {
			LOG.error("error while trying to return class associating to jar entry : {}", jarFile.getAbsolutePath(), e);
		} finally {
			if (jis != null) {
				jis.close();
			}
		}
		return classes;
	}

	private String removeFileProtocole(String path) {
		path = StringUtils.remove(path, "file:" + File.separator);
		path = StringUtils.remove(path, "file:///");
		path = StringUtils.remove(path, "file://");
		return path;
	}

	private String encodeURIComponent(String s) {
		String result;

		try {
			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	}
}
