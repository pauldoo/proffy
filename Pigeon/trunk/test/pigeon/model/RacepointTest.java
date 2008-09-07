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
 * @author Paul
 */
public final class RacepointTest extends TestCase {

    public RacepointTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RacepointTest.class);

        return suite;
    }

    public void testEquality() throws ValidationException {
        Racepoint foo = Racepoint.createEmpty().repSetName("Foo");
        Racepoint foo2 = Racepoint.createEmpty().repSetName("Foo");
        Racepoint bar = Racepoint.createEmpty().repSetName("Bar");

        assertEquals(foo, foo);
        assertEquals(foo, foo2);
        assertFalse(foo.equals(bar));
    }

    public void testExceptions() {
        try {
            Racepoint foo = Racepoint.createEmpty().repSetName("");
            assertTrue("Should throw", false);
        } catch (ValidationException ex) {
            assertEquals("Racepoint name is empty", ex.toString());
        }
    }
}
