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

import fr.jackson.addons.kubali.configuration.PropertyProvider;
import fr.jackson.addons.kubali.core.impl.TreeBuilder;
import fr.jackson.addons.kubali.model.TreeNode;

/**
 * 
 * @author adjebarri
 * @since 0.0.1
 *
 *        Interface that defines method to use/search in a tree of
 *        {@link TreeNode}.<br/>
 * 
 * @see TreeBuilder
 */
public interface ITreeHelper {
	/**
	 * Search is unidirectional, try to retrieve the parent node of a given one by
	 * it's name !<br/>
	 * This method uses {@link ITreeHelper#transform(String)} to transform the param
	 * criteria into a list, then a queue is created the root element is put into
	 * it.<br/>
	 * <b>If</b> the field name represent a nested node (<u>not direct child of the
	 * root element</u>) <b>then</b> we will split another time the field name
	 * {@link PropertyProvider#getFieldSplitator()} in order to get the real graph
	 * of the object.<br/>
	 * After building the real field name in a loop it will get the front element of
	 * the queue <b>if</b> the node's field name equals the looked for name we
	 * return the parent's node if exist or the node itself, else it will add the
	 * children of the front node to the queue to be processed<br/>
	 * <b> Note that if the node is nested it will just pass to the next elementto
	 * process !</b><br/>
	 * 
	 * <pre>
	 * Lets say that we have this tree :
	 *      A
	 *    / | \
	 *   B  C  D
	 *          \
	 *           E
	 * Only the field 'E' isNeeded - criteria = A.D.E
	 * 
	 * this.findByName(tree, criteria);
	 * 
	 * A queue with A will be created
	 *  -> build the actual lookedForName : 'A'
	 *  -> get the front node of queue (A)
	 *  -> lookedForName == A.getFiedName() && nextFieldName != null (nextFieldName = D);
	 *  -> add all children of A
	 *  -> skip B && C && add their children
	 *  -> Node = D : same process as for node A
	 *  -> node.getFieldName() = E && criteria = E && nextFieldName == null
	 *  -> return the parent node 'D'
	 * </pre>
	 * 
	 * @param root
	 *            The tree root node
	 * @param criteria
	 *            The fields to serialize as a String separated by configured
	 *            separator
	 * @return {@link Optional} which contains the result of the search otherwise an
	 *         empty one
	 */
	Optional<TreeNode> findByName(TreeNode root, String criteria);

	/**
	 * For a given {@link String} 'fields to serialize' this method uses
	 * {@link PropertyProvider#getFieldSeparator()} to split the given String and
	 * construct a {@link List} of the field name to serialize.<br/>
	 * <b><u>Examples</u></b>
	 * <ul>
	 * <li>user.profile will gives : {@code new String [] {"user", "profile"}}</li>
	 * </ul>
	 * 
	 * @param criteria
	 *            The given parameter that specifies the fields to serialize
	 *            separated by a Separator
	 * 
	 * @return A list of field to serialize
	 */
	List<String> transform(String criteria);

}
