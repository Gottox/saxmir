package de.gottox.saxmir.processor;

import java.lang.reflect.InvocationTargetException;
import de.gottox.saxmir.SXField;
import de.gottox.saxmir.css.CssSelectorCallback;

public abstract class Processor implements CssSelectorCallback {
	final protected SXField target;
	final protected Object object;

	public Processor(SXField target, Object object) {
		this.target = target;
		this.object = object;
	}

	protected void set(Object value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		target.set(object, value);
	}
}
