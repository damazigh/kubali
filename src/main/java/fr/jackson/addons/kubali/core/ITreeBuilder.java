package fr.jackson.addons.kubali.core;

import fr.jackson.addons.kubali.model.TreeNode;

public interface ITreeBuilder {
	<T> TreeNode build(T obj);
}
