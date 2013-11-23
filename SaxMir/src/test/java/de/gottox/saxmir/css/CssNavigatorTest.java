package de.gottox.saxmir.css;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CssNavigatorTest {

	final Map<CharSequence, CharSequence> empty = new HashMap<CharSequence, CharSequence>();

	@Test
	public void testSimpleTraversal() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a", handler);

		navigator.onStartMatching(null, "body", empty);
		navigator.onStartChild(null, "body", empty);
		navigator.onStartChild(null, "a", empty);
		navigator.onCharacters(null, "foobar");
		navigator.onEndChild(null, "a");
		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertEquals("foobar", handler.chars);
		assertCallback(handler, "a", null);
	}

	@Test
	public void testChild() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a c", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);

		navigator.onStartChild(null, "b", empty);

		navigator.onStartChild(null, "c", empty);
		navigator.onCharacters(null, "foobar");
		navigator.onEndChild(null, "c");

		navigator.onCharacters(null, "not this");

		navigator.onEndChild(null, "b");

		navigator.onEndChild(null, "a");

		assertEquals("foobar", handler.chars);

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertCallback(handler, "c", null);

	}

	@Test
	public void testDirectChild() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a > b", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);

		navigator.onStartChild(null, "b", empty);

		navigator.onCharacters(null, "foobar");

		navigator.onEndChild(null, "b");

		navigator.onCharacters(null, "not this");

		navigator.onEndChild(null, "a");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertCallback(handler, "b", null);

		assertEquals("foobar", handler.chars);
	}

	@Test
	public void testDirectChildNoMatch() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a > c", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);

		navigator.onStartChild(null, "b", empty);

		navigator.onStartChild(null, "c", empty);
		navigator.onCharacters(null, "foobar");
		navigator.onEndChild(null, "c");

		navigator.onCharacters(null, "not this");

		navigator.onEndChild(null, "b");

		navigator.onEndChild(null, "a");

		assertNull(handler.chars);

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertEquals(0, handler.tags.size());
	}

	@Test
	public void testDirectSibling() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a + b + c", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);
		navigator.onEndChild(null, "a");

		navigator.onStartChild(null, "b", empty);
		navigator.onEndChild(null, "b");

		navigator.onStartChild(null, "c", empty);
		navigator.onStartChild(null, "d", empty);
		navigator.onEndChild(null, "d");
		navigator.onEndChild(null, "c");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertCallback(handler, "c", "d");

	}

	@Test
	public void testDirectSiblingNoMatch() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a + b + c", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);
		navigator.onEndChild(null, "a");

		navigator.onStartChild(null, "b", empty);

		navigator.onStartChild(null, "c", empty);
		navigator.onEndChild(null, "c");

		navigator.onEndChild(null, "b");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertEquals(0, handler.tags.size());
	}

	@Test
	public void testSibling() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a ~ c", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);
		navigator.onEndChild(null, "a");

		navigator.onStartChild(null, "b", empty);
		navigator.onEndChild(null, "b");

		navigator.onStartChild(null, "c", empty);
		navigator.onEndChild(null, "c");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertCallback(handler, "c", null);

	}

	@Test
	public void testNoChildCall() {
		CssNavigator navigator = new CssNavigator();

		navigator.register("a", new TestCallback() {
			@Override
			public void onStartChild(CssNavigator handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
				fail("a has no children!");
			}

			@Override
			public void onEndChild(CssNavigator handler, CharSequence tag) {
				fail("a has no children!");
			}
		});
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		navigator.onStartChild(null, "a", empty);
		navigator.onEndChild(null, "a");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);
	}

	@Test
	public void testClassSelector() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a.foo", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		HashMap<CharSequence, CharSequence> clses = new HashMap<CharSequence, CharSequence>();
		clses.put("class", "baz foo bar");
		navigator.onStartChild(null, "a", clses);
		navigator.onEndChild(null, "a");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertCallback(handler, "a", null);
	}
	
	@Test
	public void testClassSelectorNoMatch() {
		CssNavigator navigator = new CssNavigator();

		final TestCallback handler = new TestCallback();
		navigator.register("a.foo", handler);
		navigator.onStartMatching(null, "body", empty);

		navigator.onStartChild(null, "body", empty);

		HashMap<CharSequence, CharSequence> clses = new HashMap<CharSequence, CharSequence>();
		clses.put("class", "baz foobar foo_");
		navigator.onStartChild(null, "a", clses);
		navigator.onEndChild(null, "a");

		navigator.onEndChild(null, "body");

		navigator.onEndMatching(null, null);

		assertEquals(0, handler.tags.size());
	}
	
	public void assertCallback(TestCallback handler, CharSequence tag, CharSequence childTag) {
		TestCallback.Tag tagObj = handler.tags.get(0);
		assertEquals(tag, tagObj.name);
		assertTrue(tagObj.closed);
		if (childTag != null) {
			tagObj.childTags.contains(childTag);
		}
	}

}
