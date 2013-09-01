package de.gottox.saxmir.css;

import static org.junit.Assert.*;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CssHandlerTest {

	private static final String HIERACHICAL = "<a>" +
	 " <b attr='foo'>" +
	 "  <c>" +
	 "  </c>" +
	 " </b>" +
	"</a>";

	@Test
	public void test() throws SAXException, IOException {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		CssHandler handler = new CssHandler();
		handler.register("[attr]", new CssSelectorCallback<CssHandler>() {
			
			@Override
			public void onStartElement(CssHandler handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
			}
			
			@Override
			public void onEndElement(CssHandler handler, CharSequence tag) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCharacters(CssHandler handler, CharSequence seq) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartMatching(CssHandler handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
				assertTrue(attributes.containsKey("attr"));
				System.out.println(tag);
				System.out.println(attributes.toString());
			}

			@Override
			public void onEndMatching(CssHandler handler, CharSequence tag) {
				// TODO Auto-generated method stub
				
			}
		});
		
		xmlReader.setContentHandler(handler);	
		xmlReader.parse(new InputSource(new StringReader(HIERACHICAL)));
	}
}
