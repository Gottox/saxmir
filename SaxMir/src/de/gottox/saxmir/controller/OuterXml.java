package de.gottox.saxmir.controller;

import java.util.Map;

import de.gottox.saxmir.SXField;
import de.gottox.saxmir.css.CssNavigator;

public class OuterXml extends InnerXml {

	public OuterXml(SXField target, Object object) {
		super(target, object);
	}
	
	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		onStartChild(handler, tag, attributes);
	}
	
	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		onEndChild(handler, tag);
		super.onEndMatching(handler, tag);
	}

}
