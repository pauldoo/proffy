/*
 * TimeTest.java
 * JUnit based test
 *
 * Created on June 4, 2006, 9:56 PM
 */

package pigeon.model;

import junit.framework.*;

/**
 *
 * @author pauldoo
 */
public class TimeTest extends TestCase {
    
    public TimeTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TimeTest.class);
        
        return suite;
    }

    public void testEquality() throws ValidationException {
        String ringNumberFoo = "foo";
        String ringNumberBar = "bar";
        
        Time result1 = new Time(ringNumberFoo, 1);
        
        {
            Time result2 = new Time(ringNumberFoo, 1);
            assertEquals(result1, result2);
        }
        {
            Time result2 = new Time(ringNumberFoo, 2);
            assertEquals(result1, result2);
        }
        
        {
            Time result2 = new Time(ringNumberBar, 1);
            assertFalse(result1.equals(result2));
        }
        {
            Time result2 = new Time(ringNumberBar, 2);
            assertFalse(result1.equals(result2));
        }
    }    
}
