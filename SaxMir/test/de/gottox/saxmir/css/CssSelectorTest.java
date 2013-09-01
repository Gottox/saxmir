package de.gottox.saxmir.css;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class CssSelectorTest {

	@Test
	public void testSimpleSelector() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse("body"));
		assertEquals(1, selectors.size());
		assertEquals("Creates tagselector", "body", selectors.get(0).toString());
	}

	@Test
	public void testChildCombinator() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse("body a"));
		assertEquals(1, selectors.size());
		assertEquals("Creates child combinator", "body a", selectors.get(0)
				.toString());
	}

	@Test
	public void testDirectChildCombinator() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse("body > a"));
		assertEquals(1, selectors.size());
		assertEquals("Creates direct child combinator", "body > a", selectors
				.get(0).toString());
	}

	@Test
	public void testDirectSiblinCombinator() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse("body + a"));
		assertEquals(1, selectors.size());
		assertEquals("Creates direct sibling combinator", "body + a", selectors
				.get(0).toString());
	}

	@Test
	public void testSiblingCombinator() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse("body ~ a"));
		assertEquals(1, selectors.size());
		assertEquals("Creates sibling combinator", "body ~ a", selectors.get(0)
				.toString());
	}

	@Test
	public void testMultipleSelectors() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse("body, a"));
		assertEquals("Creates multiple selectors", 2, selectors.size());
		assertEquals("body", selectors.get(1).toString());
		assertEquals("a", selectors.get(0).toString());
	}

	@Test
	public void testRootPseudoClass() {
		List<CssSelector> selectors;
		selectors = sort(CssSelector.parse(":root"));
		assertEquals("*:root", selectors.get(0).toString());
		
		try {
			CssSelector.parse("a body:root");
			fail("Should throw");
		} catch(RuntimeException e) {
			assertEquals(e.getMessage(), ":root used for non-root selector");
		}
	}
	
	List<CssSelector> sort(Set<CssSelector> set) {
		TreeSet<CssSelector> tree = new TreeSet<CssSelector>(
				new Comparator<CssSelector>() {
					@Override
					public int compare(CssSelector o1, CssSelector o2) {
						return o1.toString().compareTo(o2.toString());
					}
				});
		tree.addAll(set);
		return new ArrayList<CssSelector>(tree);
	}

}
