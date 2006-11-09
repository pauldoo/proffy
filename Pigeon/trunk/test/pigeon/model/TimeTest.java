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
        final int days = 3;
        
        Time result1 = new Time(ringNumberFoo);
        result1.setMemberTime(1, days);
        
        {
            Time result2 = new Time(ringNumberFoo);
            result2.setMemberTime(1, days);
            assertEquals(result1, result2);
        }
        {
            Time result2 = new Time(ringNumberFoo);
            result2.setMemberTime(2, days);
            assertEquals(result1, result2);
        }
        
        {
            Time result2 = new Time(ringNumberBar);
            result2.setMemberTime(1, days);

            assertFalse(result1.equals(result2));
        }
        {
            Time result2 = new Time(ringNumberBar);
            result2.setMemberTime(2, days);
            assertFalse(result1.equals(result2));
        }
    }    
}
