package de.gottox.saxmir.handlers;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import de.gottox.saxmir.Sx;
import de.gottox.saxmir.SxController;
import de.gottox.saxmir.css.CssNavigator;
import de.gottox.saxmir.css.CssSelectorCallback;

public class ContentOuterXmlHandler extends ContentXmlHandler {

	public ContentOuterXmlHandler(SxController controller, Method m, Sx sx) {
		super(controller, m, sx);
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		super.onStartMatching(handler, tag, attributes);
		onStartChild(handler, tag, attributes);
	}
	
	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		onEndChild(handler, tag);
		super.onEndMatching(handler, tag);
	}
}