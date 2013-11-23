package de.gottox.saxmir;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.gottox.saxmir.Selector;

public class SXTest {
	String xml = "<root>" 
			+ " <attr foo='bar'></attr>"
			+ " <src>h<b>tm</b>l</src>"
			+ " <number>42</number>"
			+ " <float>42.23</float>"
			+ " <list>"
			+ "  <item>1</item>"
			+ "  <item>2</item>"
			+ "  <item>3</item>"
			+ " </list>"
			+ " <complexlist>"
			+ "  <item>"
			+ "   <string>1</string>"
			+ "  </item>"
			+ "  <item>"
			+ "   <string>2</string>"
			+ "  </item>"
			+ "  <item>"
			+ "   <string>3</string>"
			+ "  </item>"
			+ " </complexlist>"
			+ "</root>";
	
	private InputSource xmlSource = new InputSource(new StringReader(xml));
	private XMLReader xmlReader;

	@Before
	public void setup() throws SAXException, SAXNotRecognizedException,
			SAXNotSupportedException, IOException {
		xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);	
	}	
	
	@Test
	public void testInstanciate() {
		assertNotNull(Sx.mk(new SxController() {
		}));
	}

	@Test
	public void testThrowsOnIncorrectSignatur() {
		try {
			assertNotNull(Sx.mk(new SxController() {
				@Selector("s")
				void f1() {
				}
			}));
			fail("Should throw");
		} catch (RuntimeException ex) {
		}
		try {
			assertNotNull(Sx.mk(new SxController() {
				@Selector("s")
				void f2(String a1, HashMap<?,?> map) {
					
				}
			}));
			fail("Should throw");
		} catch (RuntimeException ex) {
		}
	}

	@Test
	public void testCorrectSignatur() {
		assertNotNull(Sx.mk(new SxController() {
			@Selector("s")
			void f1(String a1, Map<?, ?> map) {
			}
			
			@Selector("s")
			void f2_xml(String a1, String a2) {
				
			}
			
			@Selector("s")
			void f3_xml(CharSequence a1, CharSequence a2) {
				
			}
		}));
	}

	
	@Test
	public void testEmptyHandler() throws IOException, SAXException {
		ContentHandler handler = Sx.mk(new SxController() {
		});
		
		xmlReader.setContentHandler(handler);
		xmlReader.parse(xmlSource);
	}
	
	@Test
	public void testBeforeTextHandler() throws IOException, SAXException {
		final boolean[] reached = { false };
		ContentHandler handler = Sx.mk(new SxController() {
			@Selector("attr[foo]")
			void f1(String tag, Map<CharSequence, CharSequence> attr) {
				reached[0] = true;
				assertEquals(attr.get("foo"), "bar");
			}
		});
		xmlReader.setContentHandler(handler);
		xmlReader.parse(xmlSource);
		assertTrue(reached[0]);
	}
}
