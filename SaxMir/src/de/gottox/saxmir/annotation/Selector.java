package de.gottox.saxmir.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Selector {
	public static final String CONTENT = "#content";
	public static final String DEFAULT_FLAG = "";

	String value();

	String parameter() default CONTENT;

	Class<?> type() default Void.class;
	
	String flag() default DEFAULT_FLAG;
	
	String useFlag() default DEFAULT_FLAG;
}