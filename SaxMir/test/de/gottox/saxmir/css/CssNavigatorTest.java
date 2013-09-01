package de.gottox.saxmir.css;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;

public class CssNavigatorTest {

	private final class TestCallback implements CssSelectorCallback<Object> {
		private String tag;

		public TestCallback(String tag) {
			this.tag = tag;
		}

		@Override
		public void onStartMatching(Object handler, CharSequence tag,
				Map<CharSequence, CharSequence> attributes) {
			assertEquals(CssNavigatorTest.this.handler, handler);
			matched = tag;
		}

		@Override
		public void onStartElement(Object handler, CharSequence tag,
				Map<CharSequence, CharSequence> attributes) {
			assertEquals(CssNavigatorTest.this.handler, handler);
			assertEquals(this.tag, tag);
			start = tag;
		}

		@Override
		public void onEndMatching(Object handler, CharSequence tag) {
			assertEquals(CssNavigatorTest.this.handler, handler);
			end = tag;
		}

		@Override
		public void onEndElement(Object handler, CharSequence tag) {
			assertEquals(CssNavigatorTest.this.handler, handler);
			assertEquals(this.tag, tag);
			unmatched = tag;
		}

		@Override
		public void onCharacters(Object handler, CharSequence seq) {
			assertEquals(CssNavigatorTest.this.handler, handler);
			chars = seq;
		}
	}

	final Map<CharSequence, CharSequence> empty = new HashMap<CharSequence, CharSequence>();
	final Object handler = new Object();

	CharSequence matched = null, start = null, end = null, unmatched = null;
	CharSequence chars = null;

	@Test
	public void testSimpleTraversal() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a", new TestCallback("a"));


		navigator.onStartMatching(handler, "body", empty);
		navigator.onStartElement(handler, "body", empty);
		navigator.onStartElement(handler, "a", empty);
		navigator.onCharacters(handler, "foobar");
		navigator.onEndElement(handler, "a");
		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertEquals("foobar", chars);
		assertCallback("a");
	}

	@Test
	public void testChild() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a c", new TestCallback("c"));
		navigator.onStartMatching(handler, "body", empty);

		navigator.onStartElement(handler, "body", empty);

		navigator.onStartElement(handler, "a", empty);

		navigator.onStartElement(handler, "b", empty);

		navigator.onStartElement(handler, "c", empty);
		navigator.onCharacters(handler, "foobar");
		navigator.onEndElement(handler, "c");

		navigator.onCharacters(handler, "not this");

		navigator.onEndElement(handler, "b");

		navigator.onEndElement(handler, "a");

		assertEquals("foobar", chars);

		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertCallback("c");

	}

	@Test
	public void testDirectChild() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a > b", new TestCallback("b"));
		navigator.onStartMatching(handler, "body", empty);

		navigator.onStartElement(handler, "body", empty);

		navigator.onStartElement(handler, "a", empty);

		navigator.onStartElement(handler, "b", empty);

		navigator.onCharacters(handler, "foobar");

		navigator.onEndElement(handler, "b");
		
		navigator.onCharacters(handler, "not this");

		navigator.onEndElement(handler, "a");

		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertCallback("b");

		assertEquals("foobar", chars);
	}

	@Test
	public void testDirectChildNoMatch() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a > c", new TestCallback(null));
		navigator.onStartMatching(handler, "body", empty);

		navigator.onStartElement(handler, "body", empty);

		navigator.onStartElement(handler, "a", empty);

		navigator.onStartElement(handler, "b", empty);

		navigator.onStartElement(handler, "c", empty);
		navigator.onCharacters(handler, "foobar");
		navigator.onEndElement(handler, "c");

		navigator.onCharacters(handler, "not this");

		navigator.onEndElement(handler, "b");

		navigator.onEndElement(handler, "a");

		assertNull(chars);

		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertCallback(null);
	}

	@Test
	public void testDirectSibling() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a + b + c", new TestCallback("c"));
		navigator.onStartMatching(handler, "body", empty);

		navigator.onStartElement(handler, "body", empty);

		navigator.onStartElement(handler, "a", empty);
		navigator.onEndElement(handler, "a");

		navigator.onStartElement(handler, "b", empty);
		navigator.onEndElement(handler, "b");

		navigator.onStartElement(handler, "c", empty);
		navigator.onEndElement(handler, "c");

		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertCallback("c");

	}

	@Test
	public void testDirectSiblingNoMatch() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a + b + c", new TestCallback("c"));
		navigator.onStartMatching(handler, "body", empty);

		navigator.onStartElement(handler, "body", empty);

		navigator.onStartElement(handler, "a", empty);
		navigator.onEndElement(handler, "a");

		navigator.onStartElement(handler, "b", empty);
		
		navigator.onStartElement(handler, "c", empty);
		navigator.onEndElement(handler, "c");
		
		navigator.onEndElement(handler, "b");

		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertCallback(null);

	}
	
	@Test
	public void testSibling() {
		CssNavigator<Object> navigator = new CssNavigator<Object>();

		navigator.register("a ~ c", new TestCallback("c"));
		navigator.onStartMatching(handler, "body", empty);

		navigator.onStartElement(handler, "body", empty);

		navigator.onStartElement(handler, "a", empty);
		navigator.onEndElement(handler, "a");

		navigator.onStartElement(handler, "b", empty);
		navigator.onEndElement(handler, "b");

		navigator.onStartElement(handler, "c", empty);
		navigator.onEndElement(handler, "c");

		navigator.onEndElement(handler, "body");

		navigator.onEndMatching(handler, null);

		assertCallback("c");

	}

	public void assertCallback(CharSequence tag) {
		assertEquals(tag, matched);
		assertEquals(tag, start);
		assertEquals(tag, end);
		assertEquals(tag, unmatched);
	}

}
