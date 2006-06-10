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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author pauldoo
 */
public class Clock {
    
    private Member member;

    private long masterSet;
    private long masterOpen;
    private long memberSet;
    private long memberOpen;
    
    private Collection<Time> times = new ArrayList<Time>();
    
    /** Creates a new instance of Clock */
    public Clock(Member member) {
        this.member = member;
    }

    public long convertMemberTimeToMasterTime(long time) {
        return ((time - memberSet) * (masterOpen - masterSet)) / (memberOpen - memberSet) + masterSet;
    }
    
    public long getTimeOnMasterWhenSet() {
        return masterSet;
    }

    public void setTimeOnMasterWhenSet(long timeOnMasterClockWhenSet) {
        this.masterSet = timeOnMasterClockWhenSet;
    }

    public long getTimeOnMemberWhenSet() {
        return memberSet;
    }

    public void setTimeOnMemberWhenSet(long timeOnMemberClockWhenSet) {
        this.memberSet = timeOnMemberClockWhenSet;
    }

    public long getTimeOnMasterWhenOpened() {
        return masterOpen;
    }

    public void setTimeOnMasterWhenOpened(long timeOnMasterClockWhenOpened) {
        this.masterOpen = timeOnMasterClockWhenOpened;
    }

    public long getTimeOnMemberWhenOpened() {
        return memberOpen;
    }

    public void setTimeOnMemberWhenOpened(long timeOnMemberClockWhenOpened) {
        this.memberOpen = timeOnMemberClockWhenOpened;
    }
    
    public void addTime(Time time) {
        this.times.add(time);
    }
    
    public void removeTime(Time time) {
        this.times.remove(time);
    }
    
    public Member getMember() {
        return member;
    }
    
}
