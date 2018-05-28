package fr.jackson.addons.kubali.core.processors;

import fr.jackson.addons.kubali.model.TreeNode;

public interface IProcessor {
	void process(Object obj, String specifiedFields);

	default boolean shouldProcess(TreeNode node) {
		return false;
	}
}
