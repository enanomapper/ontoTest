package net.enanomapper.onto.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public abstract class AbstractOntologyTest {

	public static String ROOT = "/var/lib/jenkin.jenm/workspace/";

	protected abstract List<String> getOntologyResource();
	
	@Test
	public void canParseXML() throws Exception {
		List<String> ontologyResource = getOntologyResource();
		Assert.assertNotNull(ontologyResource);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader parser = saxParser.getXMLReader();
		for (String resource : ontologyResource) {
			Assert.assertNotNull(resource);
			parser.parse(new InputSource(
				new FileInputStream(resource))
			);
		}
	}

	@Test
	public void loadOWL() throws Exception {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		m.addIRIMapper(new AutoIRIMapper(
			new File("materializedOntologies"), true
		));
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		addMappings(m, root);
		List<String> ontologyResource = getOntologyResource();
		for (String resource : ontologyResource) {
			OWLOntology o = m.loadOntology(
				IRI.create("file://" + resource)
			);
			Assert.assertNotNull(o);
			Assert.assertFalse(o.isEmpty());
		}
	}

	@Test
	public void checkForViolations() throws Exception {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		m.addIRIMapper(new AutoIRIMapper(
			new File("materializedOntologies"), true
		));
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		addMappings(m, root);
		List<String> ontologyResource = getOntologyResource();
		for (String resource : ontologyResource) {
			OWLOntology o = m.loadOntology(
				IRI.create("file://" + resource)
			);
			StringBuffer list = new StringBuffer();
			OWL2DLProfile profile = new OWL2DLProfile();
			OWLProfileReport report = profile.checkOntology(o);
			List<OWLProfileViolation> violations = report.getViolations();
			for(OWLProfileViolation violation : violations) {
				list.append(violation).append('\n');
			}
			Assert.assertEquals("Violations: " + list.toString(), 0, violations.size());
		}
	}

	private void addMappings(OWLOntologyManager m, String root) {
		m.addIRIMapper(new SimpleIRIMapper(
			IRI.create("http://purl.bioontology.org/ontology/npo"),
			IRI.create("file://" + root + "NPO/npo-asserted.owl")
		));
		m.addIRIMapper(new SimpleIRIMapper(
			IRI.create("http://semanticscience.org/ontology/cheminf.owl"),
			IRI.create("file://" + root + "CHEMINF/ontology/cheminf.owl")
		));
		m.addIRIMapper(new SimpleIRIMapper(
			IRI.create("http://www.enanomapper.net/ontologies/external/ontology-metadata-slim.owl"),
			IRI.create("file://" + root + "Ontology/external/ontology-metadata-slim.owl")
		));
	}

}
