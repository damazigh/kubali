package fr.jackson.addons.kubali.core.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import fr.jackson.addons.kubali.core.IInvoker;

public class Invoker implements IInvoker {

	@Override
	public Object invokeGetter(Field field, Object obj) {
		Class<?> clazz = obj.getClass();
		for (Method method : clazz.getMethods()) {
			if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))
					&& method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
				Object o;
				try {
					o = method.invoke(obj);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					return null;
				}
				return o;
			}
		}
		return null;
	}

	@Override
	public Object invokeMethod(Method m, Object[] args, Object o) {
		try {
			return m.invoke(o, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

}
