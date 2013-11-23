package de.gottox.saxmir.css;

import static org.junit.Assert.*;

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
	 "  <c class='c1 c2 c3'>" +
	 "  </c>" +
	 " </b>" +
	"</a>";

	@Test
	public void test() throws SAXException, IOException {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		CssHandler handler = new CssHandler();
		handler.getNavigator().register("[attr]", new CssSelectorCallback() {
			
			@Override
			public void onStartChild(CssNavigator handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
			}
			
			@Override
			public void onEndChild(CssNavigator handler, CharSequence tag) {
			}
			
			@Override
			public void onCharacters(CssNavigator handler, CharSequence seq) {
			}

			@Override
			public void onStartMatching(CssNavigator handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
				assertTrue(attributes.containsKey("attr"));
				System.out.println(tag);
				System.out.println(attributes.toString());
			}

			@Override
			public void onEndMatching(CssNavigator handler, CharSequence tag) {
			}
		});
		
		xmlReader.setContentHandler(handler);	
		xmlReader.parse(new InputSource(new StringReader(HIERACHICAL)));
	}
}
