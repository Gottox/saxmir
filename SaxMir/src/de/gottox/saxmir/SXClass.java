package de.gottox.saxmir;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.gottox.saxmir.annotation.Selector;
import de.gottox.saxmir.annotation.Selectors;
import de.gottox.saxmir.css.CssNavigator;
import de.gottox.saxmir.processor.Processor;

public class SXClass {
	private Class<?> cls;
	private HashMap<String, ArrayList<SXField>> fields = new HashMap<String, ArrayList<SXField>>();

	private SXClass(Class<?> cls) {
		this.cls = cls;
		genFields();
		genMethods();
	}

	private void genFields() {
		for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
			final Field[] fields = c.getDeclaredFields();
			Field.setAccessible(fields, true);
			for (Field field : fields) {
				genSelectors(field);
			}
		}
	}

	private void genMethods() {
		for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
			final Method[] methods = c.getDeclaredMethods();
			Method.setAccessible(methods, true);
			for (Method method : methods) {
				genSelectors(method);
			}
		}

	}

	private void genSelectors(AccessibleObject ao) {
		Selectors sels = ao.getAnnotation(Selectors.class);
		Selector sel = ao.getAnnotation(Selector.class);
		if (sel != null)
			genSelector(ao, sel);
		if (sels != null) {
			for (Selector s : sels.value()) {
				genSelector(ao, s);
			}
		}
	}

	private void genSelector(AccessibleObject ao, Selector sel) {
		SXField field = new SXField(ao, sel);
		ArrayList<SXField> flagFields = fields.get(field.flag);
		if(flagFields == null) {
			fields.put(field.flag, flagFields = new ArrayList<SXField>());
		}
		flagFields.add(field);
	}

	public SXField[] getFields(String flag) {
		if(flag == null)
			flag = Selector.DEFAULT_FLAG;
		ArrayList<SXField> flagFields = fields.get(flag);
		if(flagFields == null)
			return new SXField[0];
		return flagFields.toArray(new SXField[flagFields.size()]);
	}
	
	public void process(Object obj, CssNavigator navigator, String flag) {
		for(SXField field : getFields(flag)) {
			field.process(obj, navigator);
		}
	}
	
	private static HashMap<Class<?>, SXClass> sxClasses = new HashMap<Class<?>, SXClass>();

	public static SXClass getSxClass(Class<?> cls) {
		SXClass sxClass = sxClasses.get(cls);
		if (sxClass == null) {
			sxClass = new SXClass(cls);
			sxClasses.put(cls, sxClass);
		}
		return sxClass;
	}

	public static void gen(Class<?> cls) {
		getSxClass(cls);
	}
}
