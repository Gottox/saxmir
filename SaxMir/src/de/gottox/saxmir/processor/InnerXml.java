package de.gottox.saxmir.processor;

import java.util.Map;
import java.util.Map.Entry;

import de.gottox.saxmir.SXField;
import de.gottox.saxmir.css.CssNavigator;

public class InnerXml extends Text {

	public InnerXml(SXField target, Object object) {
		super(target, object);
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		super.onEndMatching(handler, tag);
	}

	@Override
	public void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		if (builder == null)
			return;
		builder.append("<").append(tag);
		for (Entry<CharSequence, CharSequence> e : attributes.entrySet()) {
			builder.append(' ').append(escapeHtml(e.getKey())).append("=\"")
					.append(escapeHtml(e.getValue())).append('"');
		}
		builder.append(">");
	}

	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
		super.onCharacters(handler, escapeHtml(seq));
	}

	protected String escapeHtml(CharSequence text) {
		return text.toString().replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("\"", "&quot");
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
		if (builder == null)
			return;
		builder.append("</").append(tag).append('>');
	}
}
