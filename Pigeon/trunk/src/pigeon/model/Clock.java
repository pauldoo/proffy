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
