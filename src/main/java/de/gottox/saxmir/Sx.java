package de.gottox.saxmir;

import java.lang.reflect.Method;

import org.xml.sax.helpers.DefaultHandler;

import de.gottox.saxmir.css.CssHandler;
import de.gottox.saxmir.css.CssSelectorCallback;
import de.gottox.saxmir.handlers.AfterHandler;
import de.gottox.saxmir.handlers.BeforeHandler;
import de.gottox.saxmir.handlers.ContentOuterXmlHandler;
import de.gottox.saxmir.handlers.ContentXmlHandler;
import de.gottox.saxmir.handlers.ContentPlainHandler;

public class Sx extends CssHandler {
	private Sx(SxController controller) {
		addController(controller);
	}
	
	public void addController(SxController controller) {
		for (Class<?> c = controller.getClass(); c != SxController.class; c = c
				.getSuperclass()) {
			Method[] methods = c.getDeclaredMethods();
			Method.setAccessible(methods, true);
			for (Method m : methods) {
				genSetter(m, controller);
			}
		}
	}

	private void genSetter(Method m, SxController controller) {
		if (m.isAnnotationPresent(Selector.class) == false)
			return;

		Selector s = m.getAnnotation(Selector.class);

		int callAt = s.callAt();

		CssSelectorCallback handler;

		if(callAt != 0) {
			switch(callAt) {
			case Selector.TAG_CONTENT_HTML:
				handler = new ContentXmlHandler(controller, m, this);
				break;
			case Selector.TAG_CONTENT_OUTER_HTML:
				handler = new ContentOuterXmlHandler(controller, m, this);
				break;
			case Selector.TAG_CONTENT_PLAIN:
				handler = new ContentPlainHandler(controller, m, this);
				break;
			case Selector.TAG_BEFORE:
				handler = new BeforeHandler(controller, m, this);
				break;
			case Selector.TAG_AFTER:
				handler = new AfterHandler(controller, m, this);
				break;
			}
		}
		if (m.getName().endsWith("_xml"))
			handler = new ContentXmlHandler(controller, m, this);
		else if (m.getName().endsWith("_outerXml"))
			handler = new ContentOuterXmlHandler(controller, m, this);
		else if (m.getName().endsWith("_plain"))
			handler = new ContentPlainHandler(controller, m, this);
		else if(m.getName().endsWith("_after"))
			handler = new AfterHandler(controller, m, this);
		else
			handler = new BeforeHandler(controller, m, this);

		this.getNavigator().register(s.value(), handler);
	}

	public static DefaultHandler mk(SxController controller) {
		return new Sx(controller);
	}

}