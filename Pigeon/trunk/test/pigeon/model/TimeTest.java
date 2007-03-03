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
 * @author pauldoo
 */
public final class TimeTest extends TestCase {

    public TimeTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TimeTest.final class);

        return suite;
    }

    public void testEquality() throws ValidationException {
        String ringNumberFoo = "foo";
        String ringNumberBar = "bar";
        final int days = 3;

        Time result1 = new Time();
        result1.setRingNumber(ringNumberFoo);
        result1.setMemberTime(1, days);

        {
            Time result2 = new Time();
            result2.setRingNumber(ringNumberFoo);
            result2.setMemberTime(1, days);
            assertEquals(result1, result2);
        }
        {
            Time result2 = new Time();
            result2.setRingNumber(ringNumberFoo);
            result2.setMemberTime(2, days);
            assertEquals(result1, result2);
        }

        {
            Time result2 = new Time();
            result2.setRingNumber(ringNumberBar);
            result2.setMemberTime(1, days);
            assertFalse(result1.equals(result2));
        }
        {
            Time result2 = new Time();
            result2.setRingNumber(ringNumberBar);
            result2.setMemberTime(2, days);
            assertFalse(result1.equals(result2));
        }
    }
}
