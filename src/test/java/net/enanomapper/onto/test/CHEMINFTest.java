package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

public class CHEMINFTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		return Arrays.asList(
		    "/var/lib/jenkin.jenm/workspace/CHEMINF/ontology/cheminf-core.owl"
		);
	}
}
