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
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
    Represents the clocking time for a single ring number.

    TODO: Rename, to maybe RingTime, PigeonEntry, ...
*/
public final class Time implements Comparable<Time>, Serializable
{
    private static final long serialVersionUID = 5746826265321182248L;

    private final String ringNumber;
    private final String color;
    private final Sex sex;
    private final int time;

    /// In CLUB mode, this list stores the club competitions.
    private final Set<String> openCompetitionsEntered;
    /// Only populated in FEDERATION mode, empty in CLUB mode.
    private final Set<String> sectionCompetitionsEntered;

    private Time(String ringNumber, String color, Sex sex, int time, Set<String> openCompetitionsEntered, Set<String> sectionCompetitionsEntered) {
        this.ringNumber = ringNumber.trim();
        this.color = color.trim();
        this.sex = sex;
        this.time = time;
        this.openCompetitionsEntered = Utilities.unmodifiableSetCopy(openCompetitionsEntered);
        this.sectionCompetitionsEntered = Utilities.unmodifiableSetCopy(sectionCompetitionsEntered);
    }

    public static Time createEmpty()
    {
        return new Time("", "", Sex.COCK, 0, Utilities.createEmtpySet(String.class), Utilities.createEmtpySet(String.class));
    }

    public String getRingNumber()
    {
        return ringNumber;
    }

    public Time repSetRingNumber(String ringNumber)
    {
        return new Time(ringNumber, color, sex, time, openCompetitionsEntered, sectionCompetitionsEntered);
    }

    public long getMemberTime()
    {
        return time;
    }

    /**
        Member time is measured in ms since the midnight before liberation.
     */
    public Time repSetMemberTime(long time, int daysInRace) throws ValidationException
    {
        if (time < 0 || time >= daysInRace * Constants.MILLISECONDS_PER_DAY) {
            throw new ValidationException("Time is outwith the length of the race (" + daysInRace + " days)");
        }
        return new Time(ringNumber, color, sex, (int)time, openCompetitionsEntered, sectionCompetitionsEntered);
    }

    @Override
    public boolean equals(Object other)
    {
        return equals((Time)other);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Time other)
    {
        return this.getRingNumber().equals(other.getRingNumber());
    }

    @Override
    public int compareTo(Time other)
    {
        return this.getRingNumber().compareTo(other.getRingNumber());
    }

    public Collection<String> getOpenCompetitionsEntered()
    {
        return Collections.unmodifiableCollection(openCompetitionsEntered);
    }

    public Time repSetOpenCompetitionsEntered(Set<String> competitions)
    {
        return new Time(ringNumber, color, sex, time, competitions, sectionCompetitionsEntered);
    }

    public Collection<String> getSectionCompetitionsEntered()
    {
        return Collections.unmodifiableCollection(sectionCompetitionsEntered);
    }

    public Time repSetSectionCompetitionsEntered(Set<String> competitions)
    {
        return new Time(ringNumber, color, sex, time, openCompetitionsEntered, competitions);
    }

    public String getColor()
    {
        String result = color;
        if (result == null) {
            result = "";
        }
        return result;
    }

    public Time repSetColor(String color)
    {
        return new Time(ringNumber, color, sex, time, openCompetitionsEntered, sectionCompetitionsEntered);
    }

    public Sex getSex()
    {
        Sex result = sex;
        if (result == null) {
            result = Sex.COCK;
        }
        return sex;
    }

    public Time repSetSex(Sex sex)
    {
        return new Time(ringNumber, color, sex, time, openCompetitionsEntered, sectionCompetitionsEntered);
    }
}
