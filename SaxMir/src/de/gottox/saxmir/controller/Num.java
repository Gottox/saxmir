package de.gottox.saxmir.controller;

import java.lang.reflect.InvocationTargetException;

import org.hamcrest.core.IsAnything;

import de.gottox.saxmir.SXField;
import de.gottox.saxmir.css.CssNavigator;

public abstract class Num extends Text {

	private int radix;

	public Num(SXField target, Object object) {
		super(target, object);
		this.radix = 10;
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		try {
			setNumber(result.toString());
		} catch (Exception e) {
			throw new RuntimeException("cannot apply data as number");
		}
	}

	protected abstract void setNumber(String string) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
}
