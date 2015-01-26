/*
 * Copyright (c) 2015 Enno Boland.
 *
 * You may not redistribute this software without the permission of
 * the Copyright owner.
 */

package de.gottox.saxmir;

import junit.framework.TestCase;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.StringReader;

/**
 * Created by tox on 1/25/15.
 */
public class TagSoupNoScriptTest {
    String doc = "" +
            "<p>" +
            "  <noscript>" +
            "    <b></b>" +
            "  </noscript>" +
            "</p>";

    @Test
    public void testNoScript() throws Exception {
        InputSource source = new InputSource(new StringReader(doc));
        SAXParserImpl xmlReader = SAXParserImpl.newInstance(null);
        TagsoupNoscriptParser parser = new TagsoupNoscriptParser();
        xmlReader.parse(source, parser);

    }
}
