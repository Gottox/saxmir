package de.gottox.saxmir.css;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class TestCallback implements CssSelectorCallback {

	List<TestCallback.Tag> tags = new LinkedList<TestCallback.Tag>();

	class Tag {
		public String name;
		public String id;
		public Map<CharSequence, CharSequence> attr = new HashMap<CharSequence, CharSequence>();
		public boolean closed = false;
		public LinkedList<String> childTags = new LinkedList<String>();
	}

	Stack<Tag> tagStack = new Stack<Tag>();
	Tag tag;
	CharSequence chars;

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		this.tag = new Tag();
		if (attributes.containsKey("id"))
			this.tag.id = attributes.get("id").toString();
		this.tag.name = tag.toString();
		this.tag.attr = attributes;
		this.tagStack.push(this.tag);
		tags.add(this.tag);
	}

	@Override
	public void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		this.tag.childTags.add(tag.toString());
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		this.tag = tagStack.pop();
		this.tag.closed = true;
		assertEquals(this.tag.name, tag);
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
	}

	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
		chars = seq;
	}
}