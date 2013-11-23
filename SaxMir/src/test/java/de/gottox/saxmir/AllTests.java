package de.gottox.saxmir;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.gottox.saxmir.css.CssHandlerTest;
import de.gottox.saxmir.css.CssNavigatorTest;
import de.gottox.saxmir.css.CssSelectorTest;

@RunWith(Suite.class)
@SuiteClasses({ SXTest.class, CssHandlerTest.class, CssNavigatorTest.class, CssSelectorTest.class })
public class AllTests {

}
