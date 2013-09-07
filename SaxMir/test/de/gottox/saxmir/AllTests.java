package de.gottox.saxmir;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.gottox.saxmir.css.CssHandlerTest;
import de.gottox.saxmir.css.CssNavigatorTest;
import de.gottox.saxmir.css.CssSelectorTest;

@RunWith(Suite.class)
@SuiteClasses({ CssNavigatorTest.class, CssSelectorTest.class,
		CssHandlerTest.class, SXDeserializerTest.class })
public class AllTests {

}
