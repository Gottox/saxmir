package de.gottox.saxmir.controller;

import java.lang.reflect.InvocationTargetException;

import de.gottox.saxmir.SXField;

public class FloatNum extends Num {
	public FloatNum(SXField target, Object object) {
		super(target, object);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setNumber(String string) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		set(Float.valueOf(string));
	}

}
