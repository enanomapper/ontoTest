package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

public class CHEMINFTests extends AbstractOntologyTests {

	@Override
	protected List<String> getOntologyResource() {
		return Arrays.asList(
		    "/home/egonw/var/Projects/Google/semanticchemistry/ontology/cheminf-core.owl"
		);
	}

}
