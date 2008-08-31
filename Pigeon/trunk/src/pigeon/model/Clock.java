/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
    Represents a single member's results for a race.

    Stores the master and member set and open times along
    with a list of all the ring numbers clocked and their times.
*/
public final class Clock implements Comparable<Clock>, Serializable
{
    private static final long serialVersionUID = 7133528350681374891L;

    private final Member member;
    private final Date masterSet;
    private final Date masterOpen;
    private final Date memberSet;
    private final Date memberOpen;
    private final int birdsEntered;
    private final List<Time> times;

    private Clock(Member member, Date masterSet, Date masterOpen, Date memberSet, Date memberOpen, int birdsEntered, List<Time> times) {
        this.member = member;
        this.masterSet = (Date)masterSet.clone();
        this.masterOpen = (Date)masterOpen.clone();
        this.memberSet = (Date)memberSet.clone();
        this.memberOpen = (Date)memberOpen.clone();
        this.birdsEntered = birdsEntered;
        this.times = Utilities.unmodifiableSortedListCopy(times);
    }

    public static Clock createEmpty()
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        return new Clock(null, cal.getTime(), cal.getTime(), cal.getTime(), cal.getTime(), 0, Utilities.createEmptyList(Time.class));
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

    public Clock repSetTimeOnMasterWhenSet(Date masterSet)
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, times);
    }

    public Date getTimeOnMemberWhenSet()
    {
        return memberSet;
    }

    public Clock repSetTimeOnMemberWhenSet(Date memberSet)
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, times);
    }

    public Date getTimeOnMasterWhenOpened()
    {
        return masterOpen;
    }

    public Clock repSetTimeOnMasterWhenOpened(Date masterOpen)
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, times);
    }

    public Date getTimeOnMemberWhenOpened()
    {
        return memberOpen;
    }

    public Clock repSetTimeOnMemberWhenOpened(Date memberOpen)
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, times);
    }

    public Clock repAddTime(Time time) throws ValidationException
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, Utilities.replicateListAdd(times, time));
    }

    public Clock repRemoveTime(Time time)
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, Utilities.replicateListRemove(times, time));
    }
    
    public Clock repReplaceTime(Time oldTime, Time newTime) throws ValidationException
    {
        return repRemoveTime(oldTime).repAddTime(newTime);
    }

    public List<Time> getTimes()
    {
        return Utilities.unmodifiableSortedListCopy(times);
    }

    public Member getMember()
    {
        return member;
    }

    public Clock repSetMember(Member member)
    {
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, times);
    }

    @Override
    public boolean equals(Object other)
    {
        return equals((Clock)other);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Clock other)
    {
        return this.member.equals(other.getMember());
    }

    @Override
    public int compareTo(Clock other)
    {
        return this.member.compareTo(other.getMember());
    }

    public int getBirdsEntered()
    {
        return birdsEntered;
    }

    public Clock repSetBirdsEntered(int birdsEntered) throws ValidationException
    {
        if (birdsEntered < times.size()) {
            throw new ValidationException("Total number of birds entered cannot be less than the number of birds clocked.");
        }
        return new Clock(member, masterSet, masterOpen, memberSet, memberOpen, birdsEntered, times);
    }
}
