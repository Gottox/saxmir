package de.gottox.saxmir.controller;

import java.lang.reflect.InvocationTargetException;

import de.gottox.saxmir.SXField;

public class LongNum extends Num {
	public LongNum(SXField target, Object object) {
		super(target, object);
	}

	@Override
	protected void setNumber(String string) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		set(Long.valueOf(string));
	}

}
