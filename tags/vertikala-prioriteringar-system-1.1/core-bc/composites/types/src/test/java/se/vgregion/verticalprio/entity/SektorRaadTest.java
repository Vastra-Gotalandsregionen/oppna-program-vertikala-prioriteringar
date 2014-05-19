package se.vgregion.verticalprio.entity;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SektorRaadTest {

	SektorRaad root, child1, child2, child1child, child2child;

	@Before
	public void setUp() throws Exception {
		root = new SektorRaad();
		child1 = new SektorRaad();
		child2 = new SektorRaad();
		child1child = new SektorRaad();
		child2child = new SektorRaad();

		root.setChildren(new ArrayList<SektorRaad>());
		root.getChildren().add(child1);
		root.getChildren().add(child2);

		child1.setChildren(new ArrayList<SektorRaad>());
		child1.getChildren().add(child1child);

		child2.setChildren(new ArrayList<SektorRaad>());
		child2.getChildren().add(child2child);
	}

	@Test
	public void getDeepestSelected() {
		// child1child.setSelected(true);
		// child2.setSelected(true);
		// root.setSelected(true);
		//
		// List<SektorRaad> r = root.getDeepestSelected();
		// assertEquals(r.size(), 2);
		// assertTrue(r.contains(child1child));
		// assertTrue(r.contains(child2));
	}

}
