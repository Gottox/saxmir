package de.gottox.saxmir.controller;

import java.lang.reflect.InvocationTargetException;

import de.gottox.saxmir.SXField;

public class IntegerNum extends Num {
	public IntegerNum(SXField target, Object object) {
		super(target, object);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setNumber(String string) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		set(Integer.valueOf(string));
	}

}
