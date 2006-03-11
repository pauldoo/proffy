/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.model;

import junit.framework.*;

/**
 *
 * @author Paul
 */
public class RacepointTest extends TestCase {
    
    public RacepointTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RacepointTest.class);
        
        return suite;
    }

    public void testEquality() throws ValidationException {
        Racepoint foo = new Racepoint();
        foo.setName("Foo");
        Racepoint foo2 = new Racepoint();
        foo2.setName("Foo");
        Racepoint bar = new Racepoint();
        bar.setName("Bar");
        
        assertEquals(foo, foo);
        assertEquals(foo, foo2);
        assertFalse(foo.equals(bar));        
    }

    public void testExceptions() {
        try {
            Racepoint foo = new Racepoint();
            foo.setName("");
            assertTrue("Should throw", false);
        } catch (ValidationException ex) {
            assertEquals("Racepoint name is empty", ex.toString());
        }
    }
}
