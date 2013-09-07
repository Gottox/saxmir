package de.gottox.saxmir.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import de.gottox.saxmir.SXClass;
import de.gottox.saxmir.SXField;
import de.gottox.saxmir.css.CssNavigator;

public class ComplexProcessor extends Processor {

	private Object obj;

	public ComplexProcessor(SXField target, Object object) {
		super(target, object);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStartMatching(CssNavigator handler, CharSequence tag,
			Map<CharSequence, CharSequence> attributes) {
		try {
			obj = target.type.newInstance();
			SXClass sxCls = SXClass.getSxClass(target.type);
			sxCls.process(obj, handler, target.flag);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onEndMatching(CssNavigator handler, CharSequence tag) {
		try {
			set(obj);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
