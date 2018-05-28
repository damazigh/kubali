package fr.jackson.addons.kubali.core;

import java.util.List;
import java.util.Optional;

import fr.jackson.addons.kubali.model.TreeNode;

public interface ITreeHelper {
	/**
	 * Search is unidirectional, try to retrieve a node by its given name
	 * 
	 * @param root
	 *            the root from which node the search starts
	 * @return {@link Optional} which contains the result of the search
	 */
	Optional<TreeNode> findByName(TreeNode root, String criteria);

	/**
	 * For a given {@link String} which represents a pattern of the fields that
	 * should be serialized, the splittator paremter can be specified in
	 * configuration <br/>
	 * <b><u>Examples</u></b>
	 * <ul>
	 * <li>user.profile will gives : {@code new String [] {"user",
	 * "profile"}}</li>
	 * </ul>
	 * 
	 * @param criteria
	 * 
	 * @return
	 */
	List<String> transform(String criteria);

	/**
	 * This method mark the children of node with the flag
	 * {@link TreeNode#setParentHidden(true))}
	 * 
	 * @param node
	 */
	void markChildrenOfHidden(TreeNode node);

}
