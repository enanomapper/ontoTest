package net.enanomapper.onto.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

public class UOTest extends AbstractOntologyTest {

	@Override
	protected List<String> getOntologyResource() {
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		return Arrays.asList(
		    root + "UO/uo-slim.owl"
		);
	}

	@Test
	public void testHas0000000() throws OWLOntologyCreationException {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		m.addIRIMapper(new AutoIRIMapper(
			new File("materializedOntologies"), true
		));
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		super.addMappings(m, root);
		List<String> ontologyResource = getOntologyResource();
		boolean found = false;
		for (String resource : ontologyResource) {
			OWLOntology o = m.loadOntology(
				IRI.create("file://" + resource)
			);
			Set<OWLClass> classes = o.getClassesInSignature();
			for (OWLClass clazz : classes) {
				if (clazz.getIRI().toString().equals("http://purl.obolibrary.org/obo/UO_0000000"))
					found = true;
			}
		}
		Assert.assertTrue("Class not found", found);
	}
	
}
