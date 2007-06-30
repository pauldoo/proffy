/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.model;

import junit.framework.*;
import java.io.Serializable;

/**
 *
 * @author Paul
 */
public final class DistanceTest extends TestCase {

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
     * Test of createFromImperial method, of final class pigeon.model.Distance.
     */
    public void testImperial() {
        final int YARDS_PER_MILE = 1760;
        // Test that the Distance final class is accurate to the nearest yard for distances up to 3000 miles
        for (int yards = 0; yards < 3000 * YARDS_PER_MILE; yards++) {
            Distance d = Distance.createFromImperial(0, yards);
            assertEquals(yards, Math.round(d.getYards()));
        }
    }

}
