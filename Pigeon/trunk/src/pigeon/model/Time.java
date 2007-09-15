/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

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
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
    Represents the clocking time for a single ring number.

    TODO: Rename, to maybe RingTime, PigeonEntry, ...
*/
public final class Time implements Comparable<Time>, Serializable
{
    private static final long serialVersionUID = 5746826265321182248L;

    private String ringNumber = "";
    private String color = "";
    private Sex sex = Sex.COCK;
    private int time = 0;

    /// In CLUB mode, this list stores the club competitions.
    private Set<String> openCompetitionsEntered = new TreeSet<String>();
    /// Only populated in FEDERATION mode, empty in CLUB mode.
    private Set<String> sectionCompetitionsEntered = new TreeSet<String>();

    public Time()
    {
    }

    public String getRingNumber()
    {
        return ringNumber;
    }

    public void setRingNumber(String ringNumber)
    {
        this.ringNumber = ringNumber.trim();
    }

    public long getMemberTime()
    {
        return time;
    }

    /**
        Member time is measured in ms since the midnight before liberation.
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

    public Collection<String> getOpenCompetitionsEntered()
    {
        return Collections.unmodifiableCollection(openCompetitionsEntered);
    }

    public void setOpenCompetitionsEntered(Collection<String> competitions)
    {
        openCompetitionsEntered = new TreeSet<String>(competitions);
    }

    public Collection<String> getSectionCompetitionsEntered()
    {
        return Collections.unmodifiableCollection(sectionCompetitionsEntered);
    }

    public void setSectionCompetitionsEntered(Collection<String> competitions)
    {
        sectionCompetitionsEntered = new TreeSet<String>(competitions);
    }

    public String getColor()
    {
        if (color == null) {
            setColor("");
        }
        return color;
    }

    public void setColor(String color)
    {
        this.color = color.trim();
    }

    public Sex getSex()
    {
        if (sex == null) {
            setSex(Sex.COCK);
        }
        return sex;
    }

    public void setSex(Sex sex)
    {
        this.sex = sex;
    }
}
