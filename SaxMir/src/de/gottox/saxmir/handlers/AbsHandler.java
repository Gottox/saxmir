package de.gottox.saxmir.handlers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import de.gottox.saxmir.Sx;
import de.gottox.saxmir.SxController;
import de.gottox.saxmir.css.CssHandler;
import de.gottox.saxmir.css.CssNavigator;
import de.gottox.saxmir.css.CssSelectorCallback;

public class AbsHandler implements CssSelectorCallback {

	protected final Method method;
	protected final SxController controller;
	protected Sx sx;

	public AbsHandler(SxController controller, Method m, Sx sx) {
		this.controller = controller;
		this.method = m;
		this.sx = sx;
	}

	void checkSignature(Class<?>... signatur) {
		Class<?>[] parameters = method.getParameterTypes();
		
		RuntimeException ex = new RuntimeException("Method " + method.getName() + " must have the following signature: " + Arrays.toString(signatur));
		
		if(parameters.length != signatur.length)
			throw ex;
			
		for(int i = 0; i < parameters.length; i++) {
			if(parameters[i].isAssignableFrom(signatur[i]) == false)
				throw ex;
		}
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
	}

	@Override
	public void onStartChild(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
	}

	@Override
	public void onCharacters(CssNavigator handler, CharSequence seq) {
	}

	@Override
	public void onEndChild(CssNavigator handler, CharSequence tag) {
	}
}