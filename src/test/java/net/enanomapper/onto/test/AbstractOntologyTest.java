package net.enanomapper.onto.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.search.EntitySearcher;
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
	public void checkRDFSLabel() throws Exception {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		m.addIRIMapper(new AutoIRIMapper(
			new File("materializedOntologies"), true
		));
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		addMappings(m, root);
		StringBuffer problems = new StringBuffer();
		List<String> ontologyResource = getOntologyResource();
		int problemCount = 0;
		for (String resource : ontologyResource) {
			OWLOntology o = m.loadOntology(
				IRI.create("file://" + resource)
			);
			Set<OWLClass> classes = o.getClassesInSignature();
			Assert.assertNotNull(classes);
			Assert.assertFalse(classes.isEmpty());
			for (OWLClass owlClass : classes) {
		        boolean hasLabel = false;
		        Collection<OWLAnnotation> annos = EntitySearcher.getAnnotations(owlClass, o);
		        for (OWLAnnotation annotation : annos) {
		        	if ("http://www.w3.org/2000/01/rdf-schema#label".equals(
		        		    annotation.getProperty().getIRI().toString())
		        		)
		        		hasLabel = true;
		        }
				if (!hasLabel) {
					problems.append(owlClass.getIRI().toString()).append(", \n");
					problemCount++;
				}
			}
		}
		Assert.assertEquals(problems.toString(), 0, problemCount);
	}

	@Test
	public void checkForAtLeastOneClass() throws Exception {
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
			Set<OWLClass> classes = o.getClassesInSignature();
			Assert.assertNotNull(classes);
			Assert.assertFalse(classes.isEmpty()); // one or more classes!
		}
	}

	@Test
	public void checkIAODefinitions() throws Exception {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		m.addIRIMapper(new AutoIRIMapper(
			new File("materializedOntologies"), true
		));
		String root = AbstractOntologyTest.ROOT;
		if (System.getProperty("ROOT") != null) {
			root = System.getProperty("ROOT");
		}
		addMappings(m, root);
		StringBuffer problems = new StringBuffer();
		List<String> ontologyResource = getOntologyResource();
		int problemCount = 0;
		for (String resource : ontologyResource) {
			OWLOntology o = m.loadOntology(
				IRI.create("file://" + resource)
			);
			Set<OWLClass> classes = o.getClassesInSignature();
			Assert.assertNotNull(classes);
			Assert.assertFalse(classes.isEmpty());
			for (OWLClass owlClass : classes) {
		        boolean hasDef = false;
		        Collection<OWLAnnotation> annos = EntitySearcher.getAnnotations(owlClass, o);
		        for (OWLAnnotation annotation : annos) {
		        	if ("http://purl.obolibrary.org/obo/IAO_0000115".equals(
		        		annotation.getProperty().getIRI().toString())) {
		        		hasDef = true;
		        	} else if ("http://purl.org/dc/elements/1.1/description".equals(
			        	annotation.getProperty().getIRI().toString())) {
			        	hasDef = true;
			        }
		        }
				if (!hasDef) {
					problems.append(owlClass.getIRI().toString()).append(", \n");
					problemCount++;
				}
			}
		}
		Assert.assertEquals(problems.toString(), 0, problemCount);
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
			Assert.assertEquals("Profile violations found: " + list.toString(), 0, violations.size());
		}
	}

	@SuppressWarnings("serial")
	Map<String,String> mappings = new HashMap<String,String>() {{
		put("http://purl.bioontology.org/ontology/npo", "NPO/npo-slim.owl");
		put("http://semanticscience.org/ontology/cheminf.owl", "CHEMINF/ontology/cheminf.owl");
		put("http://www.enanomapper.net/ontologies/external/ontology-metadata-slim.owl", "Ontology/external/ontology-metadata-slim.owl");
		put("http://purl.obolibrary.org/obo/iao/ontology-metadata.owl", "IAO/default/ontology-metadata.owl");
		put("http://purl.obolibrary.org/obo/iao.owl", "IAO/iao-slim.owl");
		put("http://purl.obolibrary.org/obo/iao/iao-main.owl", "IAO/default/iao-main.owl");
		put("http://purl.obolibrary.org/obo/iao/obsolete.owl", "IAO/default/obsolete.owl");
		put("http://purl.obolibrary.org/obo/iao/2011-05-09/external.owl", "IAO/default/obsolete.owl"); // FIXME
		put("http://purl.obolibrary.org/obo/iao/2011-05-09/externalByHand.owl", "IAO/default/obsolete.owl"); // FIXME
		put("http://purl.obolibrary.org/obo/iao/2011-05-09/externalDerived.owl", "IAO/default/obsolete.owl"); // FIXME
		put("http://purl.obolibrary.org/obo/iao/2011-05-09/obsolete.owl", "IAO/default/obsolete.owl"); // FIXME
		put("http://www.ifomis.org/bfo/1.1", "BFO/bfo-1.1.owl");
		put("http://purl.obolibrary.org/obo/bfo/core-classes.owl", "BFO/bfo-slim.owl");
		put("http://purl.org/obo/owl/ro", "RO/default/ro.owl");
		put("http://www.obofoundry.org/ro/ro.owl", "RO/default/ro.owl");
		put("http://protege.stanford.edu/plugins/owl/dc/protege-dc.owl", "DC/protege-dc.owl");
		put("http://purl.obolibrary.org/obo/envo.owl", "ENVO/envo-slim.owl");
	}};
	
	protected void addMappings(OWLOntologyManager m, String root) {
		for (String ontoIRI : mappings.keySet()) {
			String localPart = mappings.get(ontoIRI);
			m.addIRIMapper(new SimpleIRIMapper(
				IRI.create(ontoIRI), IRI.create("file://" + root + localPart)
		    ));
		}
	}

}
