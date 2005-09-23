/*
 * DistanceTest.java
 * JUnit based test
 *
 * Created on 23 September 2005, 20:23
 */

package pigeon.model;

import junit.framework.*;
import java.io.Serializable;

/**
 *
 * @author Paul
 */
public class DistanceTest extends TestCase {
    
    public DistanceTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DistanceTest.class);
        
        return suite;
    }

    /**
     * Test of createFromImperial method, of class pigeon.model.Distance.
     */
    public void testImperial() {
        final int YARDS_PER_MILE = 1760;
        // Test that the Distance class is accurate to the nearest yard for distances up to 3000 miles
        for (int yards = 0; yards < 3000 * YARDS_PER_MILE; yards++) {
            Distance d = Distance.createFromImperial(0, yards);
            assertEquals(yards, Math.round(d.getYards()));
        }
    }
    
}
