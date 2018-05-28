package fr.jackson.addons.kubali.core.processors.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jackson.addons.kubali.core.processors.IContext;

public class Context implements IContext {
	private static final Logger LOG = LoggerFactory.getLogger(Context.class);
	private List<String> processedClassName = new ArrayList<>();

	@Override
	public void addClassName(String className) {
		if (!isProcess(className)) {
			processedClassName.add(className);
		} else {
			LOG.warn("Attemtping to add an already processed className {}", className);
		}
	}

	@Override
	public boolean isProcess(String className) {
		return processedClassName.contains(className);
	}
}
