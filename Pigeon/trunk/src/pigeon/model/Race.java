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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Stores information about a race.
 *
 * Contains basic information like date, time, wind direction etc, along with a
 * list of the Clock objects (one per member).
 */
public final class Race implements Serializable, Comparable<Race> {

    private static final long serialVersionUID = 4311510053131167930L;

    private Racepoint racepoint;
    private Date liberationDate;
    private int daysCovered = 1;
    private int darknessBegins;
    private int darknessEnds;
    private String windDirection;
    private List<Clock> clocks = new ArrayList<Clock>();
    
    // Map from Section -> Member count
    private Map<String, Integer> membersEntered = new TreeMap<String, Integer>();
    
    // Map from Section -> Bird count
    private Map<String, Integer> birdsEntered = new TreeMap<String, Integer>();
    
    // Map from Section -> Pool name -> Bird count
    private Map<String, Map<String, Integer>> birdsEnteredInPools = new TreeMap<String, Map<String, Integer>>();
    
    // Map from Section -> Prize list
    private Map<String, List<Double>> prizes = new TreeMap<String, List<Double>>();
    
    public Race() {
        GregorianCalendar cal = new GregorianCalendar();
        cal = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        setLiberationDate(cal.getTime());
    }

    public Racepoint getRacepoint() {
        return racepoint;
    }

    public void setRacepoint(Racepoint racepoint) {
        this.racepoint = racepoint;
    }

    public Date getLiberationDate() {
        return liberationDate;
    }

    public void setLiberationDate(Date date) {
        this.liberationDate = date;
    }

    public boolean hasHoursOfDarkness()
    {
        return daysCovered > 1;
    }

    public void setHoursOfDarkness(int begins, int ends) throws ValidationException
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
        this.darknessBegins = begins;
        this.darknessEnds = ends;
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

    public boolean equals(Object other) {
        return equals((Race)other);
    }

    public boolean equals(Race other) {
        if (this == other) {
            return true;
        }
        return
                liberationDate.equals(other.liberationDate) &&
                racepoint.equals(other.racepoint);
    }

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

    public void setDaysCovered(int daysCovered) throws ValidationException {
        if (!(daysCovered >= 1 && daysCovered <= 3)) {
            throw new ValidationException("Days covered should be between 1 and 3");
        }
        this.daysCovered = daysCovered;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection.trim();
    }

    public String toString()
    {
        return racepoint + " (" + liberationDate + ")";
    }

    public void addClock(Clock clock) throws ValidationException
    {
        if (clocks.contains( clock ) || !clocks.add( clock )) {
            throw new ValidationException("Member clock already exists");
        }
    }

    public void removeClock(Clock clock) throws ValidationException
    {
        if (!clocks.contains( clock ) || !clocks.remove( clock )) {
            throw new ValidationException("Member clock does not exists");
        }
    }

    public List<Clock> getClocks()
    {
        return Utilities.unmodifiableSortedList(clocks);
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
        if (membersEntered == null) {
            setMembersEntered(new TreeMap<String, Integer>());
        }
        return Collections.unmodifiableMap(membersEntered);
    }

    public void setMembersEntered(Map<String, Integer> membersEntered)
    {
        this.membersEntered = new TreeMap<String, Integer>(membersEntered);
    }

    public Map<String, Integer> getBirdsEntered()
    {
        if (birdsEntered == null) {
            setBirdsEntered(new TreeMap<String, Integer>());
        }
        return Collections.unmodifiableMap(birdsEntered);
    }

    public void setBirdsEntered(Map<String, Integer> birdsEntered)
    {
        this.birdsEntered = new TreeMap<String, Integer>(birdsEntered);
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
        if (birdsEnteredInPools == null) {
            setBirdsEnteredInPools(new TreeMap<String, Map<String, Integer>>());
        }
        Map<String, Map<String, Integer>> result = new TreeMap<String, Map<String, Integer>>();
        for (Map.Entry<String, Map<String, Integer>> e: birdsEnteredInPools.entrySet()) {
            result.put(e.getKey(), Collections.unmodifiableMap(e.getValue()));
        }
        return Collections.unmodifiableMap(result);
    }

    public void setBirdsEnteredInPools(Map<String, Map<String, Integer>> birdsEnteredInPools)
    {
        Map<String, Map<String, Integer>> result = new TreeMap<String, Map<String, Integer>>();
        for (Map.Entry<String, Map<String, Integer>> e: birdsEnteredInPools.entrySet()) {
            result.put(e.getKey(), new TreeMap<String, Integer>(e.getValue()));
        }
        this.birdsEnteredInPools = result;
    }
    
    public Map<String, List<Double>> getPrizes()
    {
        if (prizes == null) {
            setPrizes(new TreeMap<String, List<Double>>());
        }
        Map<String, List<Double>> result = new TreeMap<String, List<Double>>();
        for (Map.Entry<String, List<Double>> e: prizes.entrySet()) {
            result.put(e.getKey(), Collections.unmodifiableList(e.getValue()));
        }
        return Collections.unmodifiableMap(result);
    }
    
    public void setPrizes(Map<String, List<Double>> prizes)
    {
        Map<String, List<Double>> result = new TreeMap<String, List<Double>>();
        for (Map.Entry<String, List<Double>> e: prizes.entrySet()) {
            result.put(e.getKey(), new ArrayList<Double>(e.getValue()));
        }
        this.prizes = prizes;
    }
}
