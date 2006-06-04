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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author pauldoo
 */
public class Race implements Serializable, Comparable<Race> {
    
    private static final long serialVersionUID = 42L;
    
    private Racepoint racepoint;
    private Date liberationDate;
    private int daysCovered = 1;
    private String windDirection;
    private Collection<Result> results = new ArrayList<Result>();
    
    
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
    
    public void addResult(Result result) throws ValidationException {
        if (results.contains( result ) || !results.add( result )) {
            throw new ValidationException("Result already exists");
        }
    }
    
    public Collection<Result> getResults() {
        return results;
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
}
