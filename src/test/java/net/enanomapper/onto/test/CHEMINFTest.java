package net.enanomapper.onto.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CHEMINFTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		return Arrays.asList(
		    "/home/egonw/var/Projects/Google/semanticchemistry/ontology/cheminf-core.owl"
		);
	}

	@Test
	public void canParseXML() throws Exception {
		super.canParseXML();
	}
}
