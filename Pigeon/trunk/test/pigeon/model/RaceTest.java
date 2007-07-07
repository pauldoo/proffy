/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Paul
 */
public final class RaceTest extends TestCase {

    public RaceTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RaceTest.class);

        return suite;
    }

    public void testClockOpenOffset() throws ValidationException {
        final long today = Utilities.beginningOfDay(new Date()).getTime();

        Race race = new Race();
        race.setLiberationDate(new Date(today + 1000000));
        race.setDaysCovered(2);
        assertEquals(today, race.liberationDayOffset().getTime());
    }

    public void testEquality() throws ValidationException {
        Date dateFoo = new Date(1);
        Date dateBar = new Date(2);
        Racepoint racepointFoo = new Racepoint();
        racepointFoo.setName("Foo");
        Racepoint racepointBar = new Racepoint();
        racepointBar.setName("Bar");

        Race race1 = new Race();
        race1.setLiberationDate(dateFoo);
        race1.setRacepoint(racepointFoo);

        {
            Race race2 = new Race();
            race2.setLiberationDate(dateFoo);
            race2.setRacepoint(racepointFoo);
            assertEquals(race1, race2);
        }
        {
            Race race2 = new Race();
            race2.setLiberationDate(dateFoo);
            race2.setRacepoint(racepointBar);
            assertFalse(race1.equals(race2));
        }
        {
            Race race2 = new Race();
            race2.setLiberationDate(dateBar);
            race2.setRacepoint(racepointFoo);
            assertFalse(race1.equals(race2));
        }
    }

}
