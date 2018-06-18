/*
 * Copyright 2018 Amazigh DJEBARRI

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package fr.jackson.addons.kubali.core;

import java.util.List;
import java.util.Optional;

import fr.jackson.addons.kubali.model.TreeNode;

public interface ITreeHelper {
	/**
	 * Search is unidirectional, try to retrieve the parent node of a given one
	 * by it's name
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
