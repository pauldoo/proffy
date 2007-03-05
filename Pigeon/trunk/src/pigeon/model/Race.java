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
        return Utilities.beginningOfDay(liberationDate);
    }

    public long getLengthOfDarknessEachNight()
    {
        return (Constants.MILLISECONDS_PER_DAY + darknessEnds) - darknessBegins;
    }
}
