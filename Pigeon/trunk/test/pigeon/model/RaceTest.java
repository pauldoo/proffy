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
public class RaceTest extends TestCase {
    
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
