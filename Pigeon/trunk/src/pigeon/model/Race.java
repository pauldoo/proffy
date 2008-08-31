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
import java.util.Map;
import java.util.TreeMap;

/**
    Stores information about a race.

    Contains basic information like date, time, wind direction etc, along with a
    list of the Clock objects (one per member).
*/
public final class Race implements Serializable, Comparable<Race> {

    private static final long serialVersionUID = 4311510053131167930L;

    private final Racepoint racepoint;
    private final Date liberationDate;
    private final int daysCovered;
    private final int darknessBegins;
    private final int darknessEnds;
    private final String windDirection;
    private final List<Clock> clocks;
    
    // Map from Section -> Member count
    private final Map<String, Integer> membersEntered;
    
    // Map from Section -> Bird count
    private final Map<String, Integer> birdsEntered;
    
    // Map from Section -> Pool name -> Bird count
    private final Map<String, Map<String, Integer>> birdsEnteredInPools;
    
    // Map from Section -> Prize list
    private final Map<String, List<Double>> prizes;

    private Race(
            Racepoint racepoint,
            Date liberationDate,
            int daysCovered,
            int darknessBegins, 
            int darknessEnds, 
            String windDirection,
            List<Clock> clocks,
            Map<String, Integer> membersEntered,
            Map<String, Integer> birdsEntered,
            Map<String, Map<String, Integer>> birdsEnteredInPools,
            Map<String, List<Double>> prizes
    ) {
        this.racepoint = racepoint;
        this.liberationDate = (Date)liberationDate.clone();
        this.daysCovered = daysCovered;
        this.darknessBegins = darknessBegins;
        this.darknessEnds = darknessEnds;
        this.windDirection = windDirection;
        this.clocks = Utilities.unmodifiableSortedListCopy(clocks);
        this.membersEntered = Utilities.unmodifiableMapCopy(membersEntered);
        this.birdsEntered = Utilities.unmodifiableMapCopy(birdsEntered);
        this.birdsEnteredInPools = Utilities.unmodifiableMapMapCopy(birdsEnteredInPools);
        this.prizes = Utilities.unmodifiableMapListCopy(prizes);
    }
    
    public static Race createEmpty()
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        
        //Class c = Integer.class;
        
        return new Race(
                null,
                cal.getTime(),
                1,
                0,
                0,
                null,
                Utilities.createEmptyList(Clock.class),
                Utilities.createEmptyMap(String.class, Integer.class),
                Utilities.createEmptyMap(String.class, Integer.class),
                new TreeMap<String, Map<String, Integer>>(),
                new TreeMap<String, List<Double>>());
    }

    public Racepoint getRacepoint() {
        return racepoint;
    }

    public Race repSetRacepoint(Racepoint racepoint) {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    public Date getLiberationDate() {
        return liberationDate;
    }

    public Race repSetLiberationDate(Date liberationDate) {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    public boolean hasHoursOfDarkness()
    {
        return daysCovered > 1;
    }

    public Race repSetHoursOfDarkness(int begins, int ends) throws ValidationException
    {
        if (!hasHoursOfDarkness()) {
            throw new ValidationException("Hours of darkness not applicable for a 1 day race");
        }
        int NOON = (int)(12 * Constants.MILLISECONDS_PER_HOUR);
        int MIDNIGHT = (int)(24 * Constants.MILLISECONDS_PER_HOUR);
        if (begins < NOON || begins >= MIDNIGHT) {
            throw new ValidationException("Darkness expected to begin between 12-noon and midnight");
        }
        if (ends < 0 || ends >= NOON) {
            throw new ValidationException("Darkness expected to end between midnight and 12-noon");
        }
        return new Race(racepoint, liberationDate, daysCovered, begins, ends, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    public int getDarknessBegins()
    {
        if (!hasHoursOfDarkness()) {
            throw new IllegalArgumentException("Race does not contain hours of darkness");
        }
        return darknessBegins;
    }

    public int getDarknessEnds()
    {
        if (!hasHoursOfDarkness()) {
            throw new IllegalArgumentException("Race does not contain hours of darkness");
        }
        return darknessEnds;
    }

    @Override
    public boolean equals(Object other) {
        return equals((Race)other);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Race other) {
        if (this == other) {
            return true;
        }
        return
                liberationDate.equals(other.liberationDate) &&
                racepoint.equals(other.racepoint);
    }

    @Override
    public int compareTo(Race other) {
        if (this == other) {
            return 0;
        }
        int result = liberationDate.compareTo(other.liberationDate);
        if (result != 0) {
            return result;
        }
        return racepoint.compareTo(other.racepoint);
    }

    public int getDaysCovered() {
        return daysCovered;
    }

    public Race repSetDaysCovered(int daysCovered) throws ValidationException {
        if (!(daysCovered >= 1 && daysCovered <= 3)) {
            throw new ValidationException("Days covered should be between 1 and 3");
        }
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    public String getWindDirection() {
        return windDirection;
    }

    public Race repSetWindDirection(String windDirection) {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    @Override
    public String toString()
    {
        return racepoint + " (" + liberationDate + ")";
    }

    public Race repAddClock(Clock clock) throws ValidationException
    {
        List<Clock> newClocks = Utilities.replicateListAdd(clocks, clock);
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, newClocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    public Race repRemoveClock(Clock clock) throws ValidationException
    {
        List<Clock> newClocks = Utilities.replicateListRemove(clocks, clock);
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, newClocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);        
    }
    
    public Race repReplaceClock(Clock oldClock, Clock newClock) throws ValidationException
    {
        return repRemoveClock(oldClock).repAddClock(newClock);
    }

    public List<Clock> getClocks()
    {
        return clocks;
    }

    public Date liberationDayOffset()
    {
        return Utilities.beginningOfCalendarDay(liberationDate);
    }

    public long getLengthOfDarknessEachNight()
    {
        return (Constants.MILLISECONDS_PER_DAY + darknessEnds) - darknessBegins;
    }

    public Map<String, Integer> getMembersEntered()
    {
        Map<String, Integer> result = membersEntered;
        if (result == null) {
            result = Utilities.unmodifiableMapCopy(new TreeMap<String, Integer>());
        }
        return result;
    }

    public Race repSetMembersEntered(Map<String, Integer> membersEntered)
    {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }

    public Map<String, Integer> getBirdsEntered()
    {
        Map<String, Integer> result = birdsEntered;
        if (result == null) {
            result = Utilities.unmodifiableMapCopy(new TreeMap<String, Integer>());
        }
        return result;
    }

    public Race repSetBirdsEntered(Map<String, Integer> birdsEntered)
    {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }
    
    public int getTotalNumberOfMembersEntered()
    {
        int result = 0;
        for (Integer i: getMembersEntered().values()) {
            result += i;
        }
        return result;
    }
    
    public int getTotalNumberOfBirdsEntered()
    {
        int result = 0;
        for (Integer i: getBirdsEntered().values()) {
            result += i;
        }
        return result;
    }

    public Map<String, Map<String, Integer>> getBirdsEnteredInPools()
    {
        Map<String, Map<String, Integer>> result = birdsEnteredInPools;
        if (birdsEnteredInPools == null) {
            result = Utilities.unmodifiableMapMapCopy(new TreeMap<String, Map<String, Integer>>());
        }
        return result;
    }

    public Race repSetBirdsEnteredInPools(Map<String, Map<String, Integer>> birdsEnteredInPools)
    {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }
    
    public Map<String, List<Double>> getPrizes()
    {
        Map<String, List<Double>> result = prizes;
        if (result == null) {
            result = Utilities.unmodifiableMapListCopy(new TreeMap<String, List<Double>>());
        }
        return result;
    }
    
    public Race repSetPrizes(Map<String, List<Double>> prizes)
    {
        return new Race(racepoint, liberationDate, daysCovered, darknessBegins, darknessEnds, windDirection, clocks, membersEntered, birdsEntered, birdsEnteredInPools, prizes);
    }
}
