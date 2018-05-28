package fr.jackson.addons.kubali.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author adjebarri
 *
 */
@Getter
@Setter
@EqualsAndHashCode
public class TreeNode implements Comparable<TreeNode> {
	@Getter(value = AccessLevel.NONE)
	private List<TreeNode> children;
	private boolean root;
	private boolean simple;
	@Getter(value = AccessLevel.NONE)
	private boolean nested;
	private String fieldName;
	private boolean parentHidden;
	private boolean nullValue;
	private Class<?> clazz;
	private boolean isCollection;

	public List<TreeNode> getChildren() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return children;
	}

	public boolean hasNested() {
		return nested;
	}

	@Override
	public int compareTo(TreeNode tn) {
		if (tn == null || !tn.getClazz().equals(this.getClass())) {
			return 0;
		}
		if (StringUtils.equals(tn.getFieldName(), this.getFieldName()) && this.getClazz() != null
				&& this.getClazz().equals(tn.getClazz())) {
			return 1;
		}
		return 0;
	}
}
