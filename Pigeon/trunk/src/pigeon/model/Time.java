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

import java.io.Serializable;

/**
 * Represents the clocking time for a single ring number.
 */
public class Time implements Comparable<Time>, Serializable
{
    private static final long serialVersionUID = 42L;

    private String ringNumber = "";
    private int time = 0;

    public Time()
    {
    }

    public String getRingNumber()
    {
        return ringNumber;
    }

    public void setRingNumber(String ringNumber)
    {
        this.ringNumber = ringNumber;
    }

    public long getMemberTime()
    {
        return time;
    }

    /**
     * Member time is measured in ms since the midnight before liberation.
     */
    public void setMemberTime(long time, int daysInRace) throws ValidationException
    {
        if (time < 0 || time >= daysInRace * Constants.MILLISECONDS_PER_DAY) {
            throw new ValidationException("Time is outwith the length of the race (" + daysInRace + " days)");
        }
        this.time = (int)time;
    }

    public boolean equals(Object other)
    {
        return equals((Time)other);
    }

    public boolean equals(Time other)
    {
        return this.getRingNumber().equals(other.getRingNumber());
    }

    public int compareTo(Time other)
    {
        return this.getRingNumber().compareTo(other.getRingNumber());
    }
}
