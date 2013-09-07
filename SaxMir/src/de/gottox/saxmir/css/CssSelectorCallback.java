package de.gottox.saxmir.css;

import java.util.Map;

public interface CssSelectorCallback {
	void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes);

	void onEndMatching(CssNavigator handler, CharSequence tag);

	void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes);

	void onCharacters(CssNavigator handler, CharSequence seq);

	void onEndChild(CssNavigator handler, CharSequence tag);
}
