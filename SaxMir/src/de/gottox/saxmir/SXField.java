package de.gottox.saxmir;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.gottox.saxmir.annotation.Selector;
import de.gottox.saxmir.controller.ByteNum;
import de.gottox.saxmir.controller.ComplexProcessor;
import de.gottox.saxmir.controller.Date;
import de.gottox.saxmir.controller.DoubleNum;
import de.gottox.saxmir.controller.FloatNum;
import de.gottox.saxmir.controller.IntegerNum;
import de.gottox.saxmir.controller.LongNum;
import de.gottox.saxmir.controller.Num;
import de.gottox.saxmir.controller.Processor;
import de.gottox.saxmir.controller.ShortNum;
import de.gottox.saxmir.controller.Text;
import de.gottox.saxmir.css.CssNavigator;
import de.gottox.saxmir.css.CssSelector;
import de.gottox.saxmir.css.CssSelectorCallback;

public class SXField {
	final public AccessibleObject ao;
	final public Set<CssSelector> selector;
	final public String parameter;
	final public Class<?> type;
	final public Class<? extends Processor> processor;
	final public String flag;
	final public boolean isField;
	final public boolean isMethod;
	final public boolean isCollection;

	public SXField(AccessibleObject ao, Selector sel) {
		this.selector = CssSelector.parse(sel.value());
		this.parameter = sel.parameter();
		this.ao = ao;
		this.isField = ao instanceof Field;
		this.isMethod = ao instanceof Method;
		Class<?> type;
		if (isField) {
			type = ((Field) ao).getType();
			this.isCollection = Collection.class.isAssignableFrom(type);
		} else if (isMethod) {
			final Class<?>[] parameters = ((Method) ao).getParameterTypes();
			if (parameters.length != 1)
				throw new RuntimeException(
						"All setter methods must contain one parameter!");
			type = parameters[0];
			this.isCollection = false;
		} else
			throw new RuntimeException(
					"Accessible Object neither Field nor Method. Should not happen");
		this.type = sel.type() == Void.class ? type : sel.type();
		this.processor = findProcessor();
		this.flag = sel.flag();
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Processor> findProcessor() {
		if(Processor.class.isAssignableFrom(type))
			return (Class<? extends Processor>)type;
		else if (type.isAssignableFrom(java.util.Date.class))
			return Date.class;
		else if (type.isAssignableFrom(String.class))
			return Text.class;
		else if(type == Byte.class || "byte".equals(type.getName()))
			return ByteNum.class;
		else if(type == Double.class || "double".equals(type.getName()))
			return DoubleNum.class;
		else if(type == Float.class || "float".equals(type.getName()))
			return FloatNum.class;
		else if(type == Integer.class || "int".equals(type.getName()))
			return IntegerNum.class;
		else if(type == Long.class || "long".equals(type.getName()))
			return LongNum.class;
		else if(type == Short.class || "short".equals(type.getName()))
			return ShortNum.class;
		else
			return ComplexProcessor.class;
	}

	public boolean isField() {
		return isField;
	}

	public boolean isMethod() {
		return !isField;
	}

	public void process(final Object obj, CssNavigator navigator) {
		navigator.register(selector, new Processor(this, obj) {
			Stack<Processor> processors = new Stack<Processor>();
			Processor processor;

			@Override
			public void onStartMatching(CssNavigator handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
				processors.push(processor = newProcessor(obj));
				processor.onStartMatching(handler, tag, attributes);
			}

			@Override
			public void onStartChild(CssNavigator handler, CharSequence tag,
					Map<CharSequence, CharSequence> attributes) {
				processor.onStartChild(handler, tag, attributes);
			}

			@Override
			public void onEndMatching(CssNavigator handler, CharSequence tag) {
				processors.pop().onEndMatching(handler, tag);
			}

			@Override
			public void onEndChild(CssNavigator handler, CharSequence tag) {
				processor.onEndChild(handler, tag);
			}

			@Override
			public void onCharacters(CssNavigator handler, CharSequence seq) {
				processor.onCharacters(handler, seq);
			}
		});
	}

	private Processor newProcessor(Object obj) {
		try {
			Constructor<? extends Processor> constructor = processor
					.getConstructor(SXField.class, Object.class);
			return constructor.newInstance(this, obj);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void set(Object obj, Object value) {
		try {
			if (isField) {
				final Field field = (Field) ao;
				if (isCollection) {
					((Collection) field.get(obj)).add(value);
				} else {
					field.set(obj, value);
				}
			} else if (isMethod) {
				((Method) ao).invoke(obj, value);
			}
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
	public String toString() {
		return super.toString() + "[" + ao.toString() + "]";
	}
}
