package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

public class ENMTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		return Arrays.asList(
		    "/var/lib/jenkin.jenm/workspace/Ontology/enanomapper.owl"
		);
	}

}
