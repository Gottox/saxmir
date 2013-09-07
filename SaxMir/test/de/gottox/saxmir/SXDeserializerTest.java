package de.gottox.saxmir;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.gottox.saxmir.annotation.Selector;
import de.gottox.saxmir.processor.InnerXml;
import de.gottox.saxmir.processor.Num;
import de.gottox.saxmir.processor.OuterXml;
import de.gottox.saxmir.processor.Text;

public class SXDeserializerTest {
	String xml = "<root>"
			+ "<src>h<b>tm</b>l</src>"
			+ "<number>42</number>"
			+ "<float>42.23</float>"
			+ "<list>"
			+ "<item>1</item>"
			+ "<item>2</item>"
			+ "<item>3</item>"
			+ "</list>"
			+ "<complexlist>"
			+ "<item>"
			+ "<string>1</string>"
			+ "</item>"
			+ "<item>"
			+ "<string>2</string>"
			+ "</item>"
			+ "<item>"
			+ "<string>3</string>"
			+ "</item>"
			+ "</complexlist>"
			+ "</root>";
	private SXDeserializer handler;
	private XMLReader xmlReader;
	class TextClass {
		@Selector("src")
		String text;
		
		@Selector(value = "src", type = InnerXml.class)
		String innerHtml;
		
		@Selector(value = "src", type = OuterXml.class)
		String outerHtml;
	}
	
	@Test
	public void testText() throws IOException, SAXException {
		TextClass testClass = new TextClass();
		handler.register(testClass);
		xmlReader.parse(new InputSource(new StringReader(xml)));
		assertEquals("html", testClass.text);
		assertEquals("h<b>tm</b>l", testClass.innerHtml);
		assertEquals("<src>h<b>tm</b>l</src>", testClass.outerHtml);
	}

	class NumberClass {
		@Selector(value = "number")
		int intVar;
		
		@Selector(value = "number")
		short shortVar;
		
		@Selector(value = "float")
		float floatVar;
		
		@Selector(value = "float")
		float doubleVar;
	}
	
	@Test
	public void testNumbers() throws SAXException, IOException {
		NumberClass testClass = new NumberClass();
		handler.register(testClass);
		xmlReader.parse(new InputSource(new StringReader(xml)));
		assertEquals(42, testClass.intVar);
		assertEquals(42, testClass.shortVar);
		assertEquals(42.23, testClass.floatVar, 0.001f);
		assertEquals(42.23, testClass.doubleVar, 0.001d);
	}
	
	class ListClass {
		@Selector(value = "list > item", type = Text.class)
		ArrayList<String> items = new ArrayList<String>();
	}

	@Test
	public void testList() throws SAXException, IOException {
		ListClass testClass = new ListClass();
		handler.register(testClass);
		xmlReader.parse(new InputSource(new StringReader(xml)));
		assertEquals(3, testClass.items.size());
		assertArrayEquals(new String[] {"1", "2", "3"}, testClass.items.toArray());
	}

	static public class ComplexListClass {
		static public class Item {
			@Selector(value = "> string")
			String string;
		}
		@Selector(value = "complexlist > item", type = Item.class)
		ArrayList<Item> items = new ArrayList<Item>();
	}
	
	@Test
	public void testComplexList() throws SAXException, IOException {
		ComplexListClass testClass = new ComplexListClass();
		handler.register(testClass);
		xmlReader.parse(new InputSource(new StringReader(xml)));
		assertEquals(3, testClass.items.size());
		assertEquals("1", testClass.items.get(0).string);
		assertEquals("2", testClass.items.get(1).string);
		assertEquals("3", testClass.items.get(2).string);
	}
	
	
	static public class NthChildClass {
		@Selector(value = "list > item:nth-child(1)")
		String item1;
		@Selector(value = "list > item:nth-child(2)")
		String item2;
		@Selector(value = "list > item:nth-child(3)")
		String item3;
	}

	@Test
	public void testNthChild() throws SAXException, IOException {
		NthChildClass testClass = new NthChildClass();
		handler.register(testClass);
		xmlReader.parse(new InputSource(new StringReader(xml)));
		assertEquals("1", testClass.item1);
		assertEquals("2", testClass.item2);
		assertEquals("3", testClass.item3);
	}
	
	static public class MethodClass {
		String src = "";
		@Selector(value = "src")
		private void foo(String src) {
			this.src += src;
		}
	}

	@Test
	public void testMethod() throws SAXException, IOException {
		MethodClass testClass = new MethodClass();
		handler.register(testClass);
		xmlReader.parse(new InputSource(new StringReader(xml)));
		assertEquals("html", testClass.src);
	}
	
	@Before
	public void setup() throws SAXException, SAXNotRecognizedException,
			SAXNotSupportedException, IOException {
		xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		handler = new SXDeserializer();
		xmlReader.setContentHandler(handler);	
	}

}
