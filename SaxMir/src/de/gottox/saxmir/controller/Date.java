package de.gottox.saxmir.controller;

import java.text.SimpleDateFormat;
import java.util.Map;

import de.gottox.saxmir.SXField;
import de.gottox.saxmir.css.CssNavigator;

public class Date extends Text {

	final private SimpleDateFormat formatter;
	private boolean needDate;

	public Date(SXField target, Object object) {
		super(target, object);
		this.formatter = new SimpleDateFormat();
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		CharSequence dateStr = attributes.get("datetime");
		if(dateStr == null) {
			needDate = true;
		} else {
			setDate(dateStr);
		}
	}

	public void setDate(CharSequence dateStr) {
		try {
			set(formatter.parse(dateStr.toString()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		if(needDate)
			setDate(result);
	}

	@Override
	public void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		super.onStartChild(handler, tag, attributes);
	}

	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
		super.onCharacters(handler, seq);
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
		super.onEndChild(handler, tag);
	}

}
