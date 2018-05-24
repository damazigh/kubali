package fr.jackson.addons.kubali.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface IInvoker {

	Object invokeGetter(Field field, Object obj);

	Object invokeMethod(Method m, Object[] args, Object o);
}
