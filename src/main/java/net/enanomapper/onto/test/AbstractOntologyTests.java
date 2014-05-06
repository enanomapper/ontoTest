package net.enanomapper.onto.test;

import java.io.FileInputStream;
import java.util.List;

import javax.swing.ListCellRenderer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public abstract class AbstractOntologyTests {

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

}
