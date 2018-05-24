package fr.jackson.addons.kubali.core.impl;

import java.util.Optional;

import fr.jackson.addons.kubali.core.ITreeHelper;
import fr.jackson.addons.kubali.model.TreeNode;

public class TreeHelper implements ITreeHelper {

	@Override
	public Optional<TreeNode> findByName(TreeNode root, String criteria) {
		TreeNode actual = root;
		String[] names = transform(criteria);

		return Optional.of(null);
	}

	@Override
	public String[] transform(String criteria) {
		return null;
	}

}
