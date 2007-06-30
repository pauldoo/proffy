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

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SeasonTest.class);

        return suite;
    }

    public void testRemove() throws ValidationException {
        Racepoint racepoint = new Racepoint();
        Date liberationDate = new Date(1);
        Season season = new Season();

        Race race1 = new Race();
        race1.setRacepoint(racepoint);
        race1.setLiberationDate(liberationDate);

        assertEquals(season.getRaces().size(), 0);
        season.addRace(race1);
        assertEquals(season.getRaces().size(), 1);
        season.removeRace(race1);
        assertEquals(season.getRaces().size(), 0);
    }

    public void testClashes() throws ValidationException {
        Racepoint racepoint = new Racepoint();
        Date liberationDate = new Date(1);
        Season season = new Season();

        Race race1 = new Race();
        race1.setRacepoint(racepoint);
        race1.setLiberationDate(liberationDate);
        Race race2 = new Race();
        race2.setRacepoint(racepoint);
        race2.setLiberationDate(liberationDate);

        season.addRace(race1);
        try {
            season.addRace(race2);
            assertFalse("Should throw", true);
        } catch (ValidationException ex) {
            assertEquals("Race already exists", ex.toString());
        }
    }

}
