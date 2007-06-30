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

/**
 *
 * @author Paul
 */
public final class DistanceEntryTest extends TestCase {

    public DistanceEntryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DistanceEntryTest.class);

        return suite;
    }

    public void testEquality() throws ValidationException {
        Member memberFoo = new Member();
        memberFoo.setName("foo");
        Member memberBar = new Member();
        memberBar.setName("bar");

        Racepoint racepointFoo = new Racepoint();
        racepointFoo.setName("foo");
        Racepoint racepointBar = new Racepoint();
        racepointBar.setName("bar");

        Distance distanceFoo = Distance.createFromMetric(1);
        Distance distanceBar = Distance.createFromMetric(2);
        DistanceEntry entry1 = new DistanceEntry(memberFoo, racepointFoo, distanceFoo);

        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointFoo, distanceFoo);
            assertEquals(entry1, entry2);
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointFoo, distanceBar);
            assertEquals(entry1, entry2);
        }

        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointBar, distanceFoo);
            assertFalse(entry1.equals(entry2));
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointBar, distanceBar);
            assertFalse(entry1.equals(entry2));
        }

        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointFoo, distanceFoo);
            assertFalse(entry1.equals(entry2));
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointFoo, distanceBar);
            assertFalse(entry1.equals(entry2));
        }

        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointBar, distanceFoo);
            assertFalse(entry1.equals(entry2));
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointBar, distanceBar);
            assertFalse(entry1.equals(entry2));
        }
    }

}
