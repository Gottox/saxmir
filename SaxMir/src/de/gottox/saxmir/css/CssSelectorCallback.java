package de.gottox.saxmir.css;

import java.util.Map;

public interface CssSelectorCallback<T> {
	void onStartMatching(T handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes);

	void onEndMatching(T handler, CharSequence tag);

	void onStartElement(T handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes);

	void onCharacters(T handler, CharSequence seq);

	void onEndElement(T handler, CharSequence tag);
}
