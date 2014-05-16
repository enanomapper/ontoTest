package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CHEMINFTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		return Arrays.asList(
		    "/var/lib/jenkin.jenm/workspace/CHEMINF/ontology/cheminf-core.owl"
		);
	}

	@Test
	public void canParseXML() throws Exception {
		super.canParseXML();
	}
}
