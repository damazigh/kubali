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

import fr.jackson.addons.kubali.model.TreeNode;

/**
 * 
 * @author adjebarri
 * @since 0.0.1
 *        <p>
 *        Interface that declares method for building the object graph, it uses
 *        the {@link IInvoker}, {@link IIntrospector} for instrospection and
 *        reflection
 *        </p>
 * 
 * @see TreeNode
 * @see ITreeHelper
 *
 */
public interface ITreeBuilder {

	/**
	 * build the object graph.<br/>
	 * <b>Example</b>
	 * 
	 * <pre>
	 * public class SomeClass {
	 *    private String A;
	 *    private List<AnotherClass> B;
	 *    private int C;
	 * }
	 * 
	 * public class AnotherClass {
	 *    private String D;
	 *    private String A;
	 * }
	 * 
	 * public class MainClass {
	 *    public static void main(String [] args) {
	 *        SomeClass obj = new SomeClass();
	 *        ITreeBuilder.build(obj);
	 *    }
	 *    
	 *    The result of this main will be : 
	 *        R   -> the root node (doesn't have any name)
	 *      / | \
	 *     A  B  C
	 *      /   \
	 *     D     A
	 * }
	 * </pre>
	 * 
	 * @param obj
	 *            Any object to be introspected, could be null or an empty
	 *            collection
	 * @return The object graph as a tree of {@link TreeNode}
	 */
	<T> TreeNode build(T obj);
}
