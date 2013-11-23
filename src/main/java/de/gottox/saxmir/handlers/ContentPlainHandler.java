package de.gottox.saxmir.handlers;

import java.lang.reflect.Method;
import de.gottox.saxmir.Sx;
import de.gottox.saxmir.SxController;
import de.gottox.saxmir.css.CssNavigator;

public class ContentPlainHandler extends AbsHandler {
	protected StringBuilder builder = new StringBuilder();
	protected CharSequence result;

	public ContentPlainHandler(SxController controller, Method m, Sx sx) {
		super(controller, m, sx);
		checkSignature(String.class, String.class);
	}

	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
		this.builder.append(seq);
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
		String tagStr = tag.toString().toLowerCase();
		if(tagStr.equals("br") || tagStr.equals("div"))
			this.builder.append('\n');
		else if(tagStr.equals("p"))
			this.builder.append("\n\n");
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		try {
			method.invoke(controller, tag.toString(), this.builder.toString());
			this.builder = new StringBuilder();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}