package de.gottox.saxmir.processor;

import java.lang.reflect.InvocationTargetException;

import de.gottox.saxmir.SXField;

public class ByteNum extends Num {
	public ByteNum(SXField target, Object object) {
		super(target, object);
	}

	@Override
	protected void setNumber(String string) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		set(Byte.valueOf(string));
	}

}
