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
