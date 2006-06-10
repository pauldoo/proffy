/*
 * ClockTest.java
 * JUnit based test
 *
 * Created on June 4, 2006, 9:30 PM
 */

package pigeon.model;

import junit.framework.*;

/**
 *
 * @author pauldoo
 */
public class ClockTest extends TestCase {
    
    public ClockTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ClockTest.class);
        
        return suite;
    }

    /**
     * Test of ConvertMemberTimeToMasterTime method, of class pigeon.model.Clock.
     */
    public void testConvertMemberTimeToMasterTime() {
        Clock clock = new Clock(null);
        clock.setTimeOnMasterWhenSet(200);
        clock.setTimeOnMasterWhenOpened(300);
        clock.setTimeOnMemberWhenSet(3000);
        clock.setTimeOnMemberWhenOpened(4000);
        

        assertEquals(200, clock.convertMemberTimeToMasterTime(3000));
        assertEquals(300, clock.convertMemberTimeToMasterTime(4000));
        assertEquals(250, clock.convertMemberTimeToMasterTime(3500));
        
        assertEquals(200, clock.getTimeOnMasterWhenSet());
        assertEquals(300, clock.getTimeOnMasterWhenOpened());
        assertEquals(3000, clock.getTimeOnMemberWhenSet());
        assertEquals(4000, clock.getTimeOnMemberWhenOpened());
    }
    
}
