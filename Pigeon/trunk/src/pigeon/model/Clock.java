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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Represents a single member's results for a race.
 *
 * Stores the master and member set and open times along
 * with a list of all the ring numbers clocked and their times.
 */
public final class Clock implements Comparable<Clock>, Serializable
{
    private static final long serialVersionUID = 7133528350681374891L;

    private Member member;

    private Date masterSet;
    private Date masterOpen;
    private Date memberSet;
    private Date memberOpen;

    private List<Time> times = new ArrayList<Time>();

    public Clock()
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        setTimeOnMasterWhenSet(cal.getTime());
        setTimeOnMasterWhenOpened(cal.getTime());
        setTimeOnMemberWhenSet(cal.getTime());
        setTimeOnMemberWhenOpened(cal.getTime());
    }

    /**
     * Convert a 24hr time for the clocking day, to a full Date object with correction applied.
     */
    public Date convertMemberTimeToMasterTime(Date time, Race race)
    {
        if (time.getTime() < 0 || time.getTime() >= race.getDaysCovered() * Constants.MILLISECONDS_PER_DAY) {
            throw new IllegalArgumentException("Time is outwith the length of the race");
        }
        time = new Date(time.getTime() + race.liberationDayOffset().getTime() - memberSet.getTime() + masterSet.getTime());

        long raceDuration = masterOpen.getTime() - masterSet.getTime();
        long totalDrift = (memberOpen.getTime() - memberSet.getTime()) - raceDuration;
        long driftPerDay = Constants.MILLISECONDS_PER_DAY * totalDrift / raceDuration;
        // +ve drift means that the member clock is running fast
        // -ve drivt means that the member clock is running slow
        if (driftPerDay > (3 * Constants.MILLISECONDS_PER_MINUTE)) {
            // Clock is running fast by more than 3 mins per day, regard member clock as correct
        } else {
            long shortRun = time.getTime() - masterSet.getTime();
            long longRun = memberOpen.getTime() - memberSet.getTime();
            long correction;
            if (driftPerDay < (-3 * Constants.MILLISECONDS_PER_MINUTE)) {
                // Clock is running slow by more than 3 mins per day, perform double correction
                correction = 2 * totalDrift * shortRun / longRun;
            } else {
                // Clock drift is within +/- 3 mins per day
                correction = totalDrift * shortRun / longRun;
            }
            correction = Utilities.roundToNearestSecond(correction);
            time = new Date(time.getTime() - correction);
        }
        return time;
    }

    public Date getTimeOnMasterWhenSet()
    {
        return masterSet;
    }

    public void setTimeOnMasterWhenSet(Date timeOnMasterClockWhenSet)
    {
        this.masterSet = timeOnMasterClockWhenSet;
    }

    public Date getTimeOnMemberWhenSet()
    {
        return memberSet;
    }

    public void setTimeOnMemberWhenSet(Date timeOnMemberClockWhenSet)
    {
        this.memberSet = timeOnMemberClockWhenSet;
    }

    public Date getTimeOnMasterWhenOpened()
    {
        return masterOpen;
    }

    public void setTimeOnMasterWhenOpened(Date timeOnMasterClockWhenOpened)
    {
        this.masterOpen = timeOnMasterClockWhenOpened;
    }

    public Date getTimeOnMemberWhenOpened()
    {
        return memberOpen;
    }

    public void setTimeOnMemberWhenOpened(Date timeOnMemberClockWhenOpened)
    {
        this.memberOpen = timeOnMemberClockWhenOpened;
    }

    public void addTime(Time time)
    {
        this.times.add(time);
    }

    public void removeTime(Time time)
    {
        this.times.remove(time);
    }

    public List<Time> getTimes()
    {
        return Utilities.unmodifiableSortedList(times);
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
    }

    public boolean equals(Object other)
    {
        return equals((Clock)other);
    }

    public boolean equals(Clock other)
    {
        return this.member.equals(other.getMember());
    }

    public int compareTo(Clock other)
    {
        return this.member.compareTo(other.getMember());
    }
}
