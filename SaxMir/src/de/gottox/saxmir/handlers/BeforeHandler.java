package de.gottox.saxmir.handlers;

import java.lang.reflect.Method;
import java.util.Map;

import de.gottox.saxmir.Sx;
import de.gottox.saxmir.SxController;
import de.gottox.saxmir.css.CssNavigator;

public class BeforeHandler extends AbsHandler {

	public BeforeHandler(SxController controller, Method m, Sx sx) {
		super(controller, m, sx);
		checkSignature(String.class, Map.class);
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		try {

			Object obj = method.invoke(controller, tag.toString(), attributes);
			if (obj instanceof SxController)
				sx.addController((SxController) obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}