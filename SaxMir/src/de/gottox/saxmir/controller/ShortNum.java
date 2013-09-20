package de.gottox.saxmir.controller;

import java.lang.reflect.InvocationTargetException;

import de.gottox.saxmir.SXField;

public class ShortNum extends Num {
	public ShortNum(SXField target, Object object) {
		super(target, object);
	}

	@Override
	protected void setNumber(String string) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		set(Short.valueOf(string));
	}

}
