package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

public class IAOTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		return Arrays.asList(
		    root + "IAO/default/ontology-metadata.owl",
		    root + "IAO/default/obsolete.owl",
		    root + "IAO/default/iao-main.owl",
		    root + "IAO/default/iao.owl"
		);
	}

}
