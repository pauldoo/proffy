/*
 * ClockTest.java
 * JUnit based test
 *
 * Created on June 4, 2006, 9:30 PM
 */

package pigeon.model;

import java.util.Date;
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
        Clock clock = new Clock();
        clock.setTimeOnMasterWhenSet(new Date(200));
        clock.setTimeOnMasterWhenOpened(new Date(300));
        clock.setTimeOnMemberWhenSet(new Date(3000));
        clock.setTimeOnMemberWhenOpened(new Date(4000));
        

        assertEquals(200, clock.convertMemberTimeToMasterTime(new Date(3000)).getTime());
        assertEquals(300, clock.convertMemberTimeToMasterTime(new Date(4000)).getTime());
        assertEquals(250, clock.convertMemberTimeToMasterTime(new Date(3500)).getTime());
        
        assertEquals(200, clock.getTimeOnMasterWhenSet().getTime());
        assertEquals(300, clock.getTimeOnMasterWhenOpened().getTime());
        assertEquals(3000, clock.getTimeOnMemberWhenSet().getTime());
        assertEquals(4000, clock.getTimeOnMemberWhenOpened().getTime());
    }
    
}
