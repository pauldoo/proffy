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

import java.util.Date;
import junit.framework.*;

/**
 *
 * @author Paul
 */
public final class SeasonTest extends TestCase {

    public SeasonTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SeasonTest.class);

        return suite;
    }

    public void testRemove() throws ValidationException {
        Racepoint racepoint = Racepoint.createEmpty();
        Date liberationDate = new Date(1);
        Season season = Season.createEmpty();

        Race race1 = Race.createEmpty();
        race1 = race1.repSetRacepoint(racepoint);
        race1 = race1.repSetLiberationDate(liberationDate);

        assertEquals(season.getRaces().size(), 0);
        season = season.repAddRace(race1);
        assertEquals(season.getRaces().size(), 1);
        season = season.repRemoveRace(race1);
        assertEquals(season.getRaces().size(), 0);
    }

    public void testClashes() throws ValidationException {
        Racepoint racepoint = Racepoint.createEmpty();
        Date liberationDate = new Date(1);
        Season season = Season.createEmpty();

        Race race1 = Race.createEmpty();
        race1 = race1.repSetRacepoint(racepoint);
        race1 = race1.repSetLiberationDate(liberationDate);
        Race race2 = Race.createEmpty();
        race2 = race2.repSetRacepoint(racepoint);
        race2 = race2.repSetLiberationDate(liberationDate);

        season = season.repAddRace(race1);
        try {
            season = season.repAddRace(race2);
            assertFalse("Should throw", true);
        } catch (ValidationException ex) {
            assertEquals("Race already exists", ex.toString());
        }
    }

}
