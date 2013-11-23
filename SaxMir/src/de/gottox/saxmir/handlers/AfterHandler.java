package de.gottox.saxmir.handlers;

import java.lang.reflect.Method;
import de.gottox.saxmir.Sx;
import de.gottox.saxmir.SxController;
import de.gottox.saxmir.css.CssNavigator;

public class AfterHandler extends AbsHandler {

	public AfterHandler(SxController controller, Method m, Sx sx) {
		super(controller, m, sx);
		checkSignature(String.class);
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		try {

			Object obj = method.invoke(controller, tag.toString());
			if (obj instanceof SxController)
				sx.addController(controller);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}