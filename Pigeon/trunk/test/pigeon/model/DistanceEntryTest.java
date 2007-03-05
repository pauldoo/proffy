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

/**
 *
 * @author Paul
 */
public final class DistanceEntryTest extends TestCase {

    public DistanceEntryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DistanceEntryTest.class);

        return suite;
    }

    public void testEquality() throws ValidationException {
        Member memberFoo = new Member();
        memberFoo.setName("foo");
        Member memberBar = new Member();
        memberBar.setName("bar");

        Racepoint racepointFoo = new Racepoint();
        racepointFoo.setName("foo");
        Racepoint racepointBar = new Racepoint();
        racepointBar.setName("bar");

        Distance distanceFoo = Distance.createFromMetric(1);
        Distance distanceBar = Distance.createFromMetric(2);
        DistanceEntry entry1 = new DistanceEntry(memberFoo, racepointFoo, distanceFoo);

        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointFoo, distanceFoo);
            assertEquals(entry1, entry2);
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointFoo, distanceBar);
            assertEquals(entry1, entry2);
        }

        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointBar, distanceFoo);
            assertFalse(entry1.equals(entry2));
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberFoo, racepointBar, distanceBar);
            assertFalse(entry1.equals(entry2));
        }

        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointFoo, distanceFoo);
            assertFalse(entry1.equals(entry2));
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointFoo, distanceBar);
            assertFalse(entry1.equals(entry2));
        }

        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointBar, distanceFoo);
            assertFalse(entry1.equals(entry2));
        }
        {
            DistanceEntry entry2 = new DistanceEntry(memberBar, racepointBar, distanceBar);
            assertFalse(entry1.equals(entry2));
        }
    }

}
