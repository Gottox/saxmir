package de.gottox.saxmir.handlers;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import de.gottox.saxmir.Sx;
import de.gottox.saxmir.SxController;
import de.gottox.saxmir.css.CssNavigator;

public class ContentXmlHandler extends AbsHandler {
    protected StringBuilder builder = new StringBuilder();
    protected CharSequence result;
    
	public ContentXmlHandler(SxController controller, Method m, Sx sx) {
		super(controller, m, sx);
		checkSignature(String.class, String.class);
	}

	@Override
	public void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		builder.append("<").append(tag);
		for (Entry<CharSequence, CharSequence> e : attributes.entrySet()) {
			builder.append(' ').append(escapeHtml(e.getKey())).append("=\"")
					.append(escapeHtml(e.getValue())).append('"');
		}
		builder.append(">");
	}
	
	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		super.onStartMatching(handler, tag, attributes);
		builder = new StringBuilder();
	}
	
	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
		builder.append(escapeHtml(seq));
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
		builder.append("</").append(tag).append(">");
	}
	
	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		try {
			method.invoke(controller, tag.toString(), this.builder.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String escapeHtml(CharSequence text) {
		return text.toString().replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("\"", "&quot");
	}
}