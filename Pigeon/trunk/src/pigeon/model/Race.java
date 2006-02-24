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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author pauldoo
 */
public class Race implements Serializable, Comparable<Race> {
    
    private static final long serialVersionUID = 42L;
    
    private Racepoint racepoint;
    private Date liberationDate;
    private int daysCovered;
    private String windDirection;
    private Map<Member, Map<String, Long>> results = new HashMap<Member, Map<String, Long>>();
    
    
    public Race(Club club) {
        for (Member m: club.getMembers()) {
            results.put(m, new HashMap<String, Long>());
        }
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
    
    public void addResult(Member member, String ringNumber, long time) {
        results.get(member).put(ringNumber, time);
    }
    
    public Map<String, Long> getResults(Member member) {
        return results.get(member);
    }
    
    public long getResult(Member member, String ringNumber) {
        return getResults(member).get(ringNumber);
    }

    public boolean equals(Object other) {
        return equals((Race)other);
    }
    
    public boolean equals(Race other) {
        if (this == other) {
            return true;
        }
        if (liberationDate.compareTo(other.liberationDate) != 0) {
            return false;
        }
        if (racepoint.compareTo(other.racepoint) != 0) {
            return false;
        }
        return true;
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

    public void setDaysCovered(int daysCovered) {
        this.daysCovered = daysCovered;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
}
