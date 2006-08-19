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
    public void testConvertMemberTimeToMasterTime() throws ValidationException {
        final long today = Utilities.beginningOfDay(new Date()).getTime();
        
        Clock clock = new Clock();
        clock.setTimeOnMasterWhenSet(new Date(today + 200));
        clock.setTimeOnMasterWhenOpened(new Date(today + 300));
        clock.setTimeOnMemberWhenSet(new Date(today + 3000));
        clock.setTimeOnMemberWhenOpened(new Date(today + 4000));
        
        assertEquals(today + 200, clock.getTimeOnMasterWhenSet().getTime());
        assertEquals(today + 300, clock.getTimeOnMasterWhenOpened().getTime());
        assertEquals(today + 3000, clock.getTimeOnMemberWhenSet().getTime());
        assertEquals(today + 4000, clock.getTimeOnMemberWhenOpened().getTime());

        Race race = new Race();
        race.setLiberationDate(new Date(today));
        race.setDaysCovered(1);
        
        assertEquals(today + 200, clock.convertMemberTimeToMasterTime(new Date(3000), race).getTime());
        assertEquals(today + 300, clock.convertMemberTimeToMasterTime(new Date(4000), race).getTime());
        assertEquals(today + 250, clock.convertMemberTimeToMasterTime(new Date(3500), race).getTime());
    }
    
}
