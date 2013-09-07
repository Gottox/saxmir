package de.gottox.saxmir;

import de.gottox.saxmir.css.CssHandler;

public class SXDeserializer extends CssHandler {
	void register(Object obj, String flag) {
		SXClass sxCls = SXClass.getSxClass(obj.getClass());
		for( SXField field : sxCls.getFields(flag)) {
			field.process(obj, getNavigator());
		}
	}

	public void register(Object obj) {
		register(obj, null);
	}
}
