package de.gottox.saxmir;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Selector {
	public final static int TAG_BEFORE = 1;
	public final static int TAG_CONTENT_PLAIN = 2;
	public final static int TAG_CONTENT_HTML = 3;
	public final static int TAG_CONTENT_OUTER_HTML = 4;
	public final static int TAG_AFTER = 5;
	
	String value();
	
	int callAt() default 0;
}