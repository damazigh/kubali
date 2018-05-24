package fr.jackson.addons.kubali.treebuilder;

import org.junit.gen5.api.BeforeAll;

import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.IInvoker;
import fr.jackson.addons.kubali.core.impl.Introspector;
import fr.jackson.addons.kubali.core.impl.Invoker;
import fr.jackson.addons.kubali.core.impl.TreeBuilder;

public class TreeBuilderTest {
	private TreeBuilder tb;

	@BeforeAll
	public void init() {
		tb = new TreeBuilder();
		IIntrospector i = new Introspector();
		IInvoker invoker = new Invoker();
	}

}
