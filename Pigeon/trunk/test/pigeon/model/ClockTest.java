/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.

        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
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
