/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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
 * @author pauldoo
 */
public final class TimeTest extends TestCase {

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

        Time result1 = Time.createEmpty();
        result1 = result1.repSetRingNumber(ringNumberFoo);
        result1 = result1.repSetMemberTime(1, days);

        {
            Time result2 = Time.createEmpty();
            result2 = result2.repSetRingNumber(ringNumberFoo);
            result2 = result2.repSetMemberTime(1, days);
            assertEquals(result1, result2);
        }
        {
            Time result2 = Time.createEmpty();
            result2 = result2.repSetRingNumber(ringNumberFoo);
            result2 = result2.repSetMemberTime(2, days);
            assertEquals(result1, result2);
        }

        {
            Time result2 = Time.createEmpty();
            result2 = result2.repSetRingNumber(ringNumberBar);
            result2 = result2.repSetMemberTime(1, days);
            assertFalse(result1.equals(result2));
        }
        {
            Time result2 = Time.createEmpty();
            result2 = result2.repSetRingNumber(ringNumberBar);
            result2 = result2.repSetMemberTime(2, days);
            assertFalse(result1.equals(result2));
        }
    }
}
