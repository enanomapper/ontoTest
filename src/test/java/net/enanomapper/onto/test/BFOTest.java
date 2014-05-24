package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

public class BFOTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		return Arrays.asList(
		    root + "BFO/default/bfo-1.1.owl"
		);
	}

}
