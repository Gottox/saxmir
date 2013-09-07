package de.gottox.saxmir.processor;

import java.lang.reflect.AccessibleObject;
import java.util.Map;
import java.util.Stack;

import de.gottox.saxmir.SXField;
import de.gottox.saxmir.annotation.Selector;
import de.gottox.saxmir.css.CssNavigator;

public class Text extends Processor {
	final protected StringBuilder builder;
	protected CharSequence result;

	public Text(SXField target, Object object) {
		super(target, object);
		if(target.parameter.equals(Selector.CONTENT))
			result = builder = new StringBuilder();
		else
			builder = null;
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		if(builder == null)
			result = attributes.get(target.parameter);
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		try {
			set(result.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
	}

	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
		this.builder.append(seq);
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
	}

}
