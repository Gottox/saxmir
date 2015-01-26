/*
 * Copyright (c) 2015 Enno Boland.
 *
 * You may not redistribute this software without the permission of
 * the Copyright owner.
 */

package de.gottox.saxmir;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by tox on 1/25/15.
 */
public class TagsoupNoscriptParser extends DefaultHandler {
    private int level = 0;
    StringBuilder output = new StringBuilder();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        addLevel();
        output.append(localName).append('\n');
        level++;
    }

    private void addLevel() {
        for(int i = 0; i < level; i++) {
            output.append("   ");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        level--;
        addLevel();
        output.append('/').append(localName).append('\n');
    }

    @Override
    public String toString() {
        return output.toString();
    }
}
