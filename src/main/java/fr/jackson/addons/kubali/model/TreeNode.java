package fr.jackson.addons.kubali.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author adjebarri
 *
 */
@Getter
@Setter
public class TreeNode {
	@Getter(value = AccessLevel.NONE)
	private List<TreeNode> children;
	private boolean root;
	private boolean simple;
	@Getter(value = AccessLevel.NONE)
	private boolean nested;
	private String fieldName;

	public List<TreeNode> getChildren() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return children;
	}

	public boolean hasNested() {
		return nested;
	}
}
