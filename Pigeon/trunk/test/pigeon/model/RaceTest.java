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
import java.util.Date;

/**
 *
 * @author Paul
 */
public final class RaceTest extends TestCase {

    public RaceTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RaceTest.class);

        return suite;
    }

    public void testClockOpenOffset() throws ValidationException {
        final long today = pigeon.model.Utilities.beginningOfCalendarDay(new Date()).getTime();

        Race race = Race.createEmpty();
        race = race.repSetLiberationDate(new Date(today + 1000000));
        race = race.repSetDaysCovered(2);
        assertEquals(today, race.liberationDayOffset().getTime());
    }

    public void testEquality() throws ValidationException {
        Date dateFoo = new Date(1);
        Date dateBar = new Date(2);
        Racepoint racepointFoo = Racepoint.createEmpty().repSetName("Foo");
        Racepoint racepointBar = Racepoint.createEmpty().repSetName("Bar");

        Race race1 = Race.createEmpty();
        race1 = race1.repSetLiberationDate(dateFoo);
        race1 = race1.repSetRacepoint(racepointFoo);

        {
            Race race2 = Race.createEmpty();
            race2 = race2.repSetLiberationDate(dateFoo);
            race2 = race2.repSetRacepoint(racepointFoo);
            assertEquals(race1, race2);
        }
        {
            Race race2 = Race.createEmpty();
            race2 = race2.repSetLiberationDate(dateFoo);
            race2 = race2.repSetRacepoint(racepointBar);
            assertFalse(race1.equals(race2));
        }
        {
            Race race2 = Race.createEmpty();
            race2 = race2.repSetLiberationDate(dateBar);
            race2 = race2.repSetRacepoint(racepointFoo);
            assertFalse(race1.equals(race2));
        }
    }

}
