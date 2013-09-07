package de.gottox.saxmir.css;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CssHandler extends DefaultHandler {
	private CssNavigator navigator = new CssNavigator();

	@Override
	public void startDocument() throws SAXException {
		getNavigator().onStartMatching(getNavigator(), null, null);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {
		HashMap<CharSequence, CharSequence> attrHash = new HashMap<CharSequence, CharSequence>();
		for(int i = 0; i < attrs.getLength(); i++) {
			attrHash.put(attrs.getLocalName(i), attrs.getValue(i));
		}
		getNavigator().onStartChild(getNavigator(), localName, attrHash);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		getNavigator().onCharacters(getNavigator(), new String(ch, start, length));
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		getNavigator().onEndChild(getNavigator(), localName);
	}

	public void endDocument() throws SAXException {
		getNavigator().onEndMatching(getNavigator(), null);
	}

	public CssNavigator getNavigator() {
		return navigator;
	}
}
