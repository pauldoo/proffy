/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author pauldoo
 */
public class Clock implements Comparable<Clock>
{
    private Member member;
    
    private Date masterSet;
    private Date masterOpen;
    private Date memberSet;
    private Date memberOpen;
    
    private List<Time> times = new ArrayList<Time>();
    
    /** Creates a new instance of Clock */
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
    
    public Date convertMemberTimeToMasterTime(Date time, Race race)
    {
        if (time.getTime() < 0 || time.getTime() >= Constants.MILLISECONDS_PER_DAY) {
            throw new IllegalArgumentException("Member date is expected to be in the range of one day");
        }
        time = new Date(time.getTime() + race.clockingDayOffset());
        return new Date(((time.getTime() - memberSet.getTime()) * (masterOpen.getTime() - masterSet.getTime())) / (memberOpen.getTime() - memberSet.getTime()) + masterSet.getTime());
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
        return times;
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
