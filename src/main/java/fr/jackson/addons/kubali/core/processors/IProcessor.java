package fr.jackson.addons.kubali.core.processors;

import fr.jackson.addons.kubali.model.TreeNode;

public interface IProcessor {
	void process(Object obj);

	default boolean shouldProcess(TreeNode node) {
		return false;
	}
}
