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

import java.util.Date;
import junit.framework.*;

/**
 *
 * @author pauldoo
 */
public final class ClockTest extends TestCase {

    public ClockTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ClockTest.class);

        return suite;
    }

    /**
     * Test of ConvertMemberTimeToMasterTime method, of final class pigeon.model.Clock.
     */
    public void testConvertMemberTimeToMasterTime() throws ValidationException {
        final long today = Utilities.beginningOfDay(new Date()).getTime();

        Clock clock = new Clock();
        clock.setTimeOnMasterWhenSet(new Date(today + 200));
        clock.setTimeOnMasterWhenOpened(new Date(today + 10000600));
        clock.setTimeOnMemberWhenSet(new Date(today + 3000));
        clock.setTimeOnMemberWhenOpened(new Date(today + 10007000));

        assertEquals(today + 200, clock.getTimeOnMasterWhenSet().getTime());
        assertEquals(today + 10000600, clock.getTimeOnMasterWhenOpened().getTime());
        assertEquals(today + 3000, clock.getTimeOnMemberWhenSet().getTime());
        assertEquals(today + 10007000, clock.getTimeOnMemberWhenOpened().getTime());

        Race race = new Race();
        race.setLiberationDate(new Date(today));
        race.setDaysCovered(1);

        assertEquals(today + 200, clock.convertMemberTimeToMasterTime(new Date(3000), race).getTime());
        assertEquals(today + 5000200, clock.convertMemberTimeToMasterTime(new Date(5005000), race).getTime());
        assertEquals(today + 10000200, clock.convertMemberTimeToMasterTime(new Date(10007000), race).getTime());
    }

    /**
     * Test clock variation example in rulebook.
     */
    public void testClockVariationCalculationExample() throws ValidationException {
        final long today = Utilities.beginningOfDay(new Date()).getTime();

        {
            // Member clock gains 2m 40s
            Clock clock = new Clock();
            clock.setTimeOnMasterWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMemberWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMasterWhenOpened(new Date(today + toMs(1, 21, 40, 50)));
            clock.setTimeOnMemberWhenOpened(new Date(today + toMs(1, 21, 43, 30)));

            Race race = new Race();
            race.setLiberationDate(new Date(today));
            race.setDaysCovered(2);

            final long birdTime = toMs(1, 14, 30, 25);
            final long correctedTime = clock.convertMemberTimeToMasterTime(new Date(birdTime), race).getTime() - today;
            assertEquals(-113 * 1000, correctedTime - birdTime);
        }
        {
            // Member clock is spot on
            Clock clock = new Clock();
            clock.setTimeOnMasterWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMemberWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMasterWhenOpened(new Date(today + toMs(1, 21, 40, 50)));
            clock.setTimeOnMemberWhenOpened(new Date(today + toMs(1, 21, 40, 50)));

            Race race = new Race();
            race.setLiberationDate(new Date(today));
            race.setDaysCovered(2);

            final long birdTime = toMs(1, 14, 30, 25);
            final long correctedTime = clock.convertMemberTimeToMasterTime(new Date(birdTime), race).getTime() - today;
            assertEquals(0, correctedTime - birdTime);
        }
        {
            // Member clock loses 2m 40s
            Clock clock = new Clock();
            clock.setTimeOnMasterWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMemberWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMasterWhenOpened(new Date(today + toMs(1, 21, 40, 50)));
            clock.setTimeOnMemberWhenOpened(new Date(today + toMs(1, 21, 38, 10)));

            Race race = new Race();
            race.setLiberationDate(new Date(today));
            race.setDaysCovered(2);

            final long birdTime = toMs(1, 14, 30, 25);
            final long correctedTime = clock.convertMemberTimeToMasterTime(new Date(birdTime), race).getTime() - today;
            assertEquals(114 * 1000, correctedTime - birdTime);
        }
        {
            // Member clock loses 5m (trigger double correction)
            Clock clock = new Clock();
            clock.setTimeOnMasterWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMemberWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMasterWhenOpened(new Date(today + toMs(1, 21, 40, 50)));
            clock.setTimeOnMemberWhenOpened(new Date(today + toMs(1, 21, 35, 50)));

            Race race = new Race();
            race.setLiberationDate(new Date(today));
            race.setDaysCovered(2);

            final long birdTime = toMs(1, 14, 30, 25);
            final long correctedTime = clock.convertMemberTimeToMasterTime(new Date(birdTime), race).getTime() - today;
            assertEquals(428 * 1000, correctedTime - birdTime); // corrected by over 7 minutes
        }
        {
            // Member clock gains 5m (trigger no correction)
            Clock clock = new Clock();
            clock.setTimeOnMasterWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMemberWhenSet(new Date(today + toMs(0, 20, 55, 20)));
            clock.setTimeOnMasterWhenOpened(new Date(today + toMs(1, 21, 40, 50)));
            clock.setTimeOnMemberWhenOpened(new Date(today + toMs(1, 21, 45, 50)));

            Race race = new Race();
            race.setLiberationDate(new Date(today));
            race.setDaysCovered(2);

            final long birdTime = toMs(1, 14, 30, 25);
            final long correctedTime = clock.convertMemberTimeToMasterTime(new Date(birdTime), race).getTime() - today;
            assertEquals(0, correctedTime - birdTime);
        }
    }

    private static long toMs(int days, int hours, int minutes, int seconds) {
        if (hours < 0 || hours >= 24) {
            throw new IllegalArgumentException("Hours outwith one day");
        }
        if (minutes < 0 || minutes >= 60) {
            throw new IllegalArgumentException("Minutes outwith one hour");
        }
        if (seconds < 0 || seconds >= 60) {
            throw new IllegalArgumentException("Seconds outwith one minute");
        }
        hours += days * 24;
        minutes += hours * 60;
        seconds += minutes * 60;
        return seconds * 1000;
    }
}
