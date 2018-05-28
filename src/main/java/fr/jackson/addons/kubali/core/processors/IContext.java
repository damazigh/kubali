package fr.jackson.addons.kubali.core.processors;

public interface IContext {
	void addClassName(String className);

	boolean isProcess(String className);

}
