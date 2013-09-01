package de.gottox.saxmir.css;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CssHandler extends DefaultHandler {
	CssNavigator<CssHandler> navigator = new CssNavigator<CssHandler>();

	@Override
	public void startDocument() throws SAXException {
		navigator.onStartMatching(this, null, null);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {
		HashMap<CharSequence, CharSequence> attrHash = new HashMap<CharSequence, CharSequence>();
		for(int i = 0; i < attrs.getLength(); i++) {
			attrHash.put(attrs.getLocalName(i), attrs.getValue(i));
		}
		navigator.onStartElement(this, localName, attrHash);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		navigator.onCharacters(this, new String(ch, start, length));
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		navigator.onEndElement(this, localName);
	}

	public void endDocument() throws SAXException {
		navigator.onEndMatching(this, null);
	}

	public void register(String selector, CssSelectorCallback<CssHandler> handler) {
		navigator.register(selector, handler);
	}

	public void register(Set<CssSelector> selectors,
			CssSelectorCallback<CssHandler> handler) {
		navigator.register(selectors, handler);
	}
}
