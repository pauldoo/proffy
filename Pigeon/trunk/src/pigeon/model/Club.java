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
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Paul
 */
public class Club implements Serializable {

    private static final long serialVersionUID = 42L;
    
    private String name;
    private Collection<Member> members = new ArrayList<Member>();
    private Collection<Racepoint> racepoints = new ArrayList<Racepoint>();
    private Collection<DistanceEntry> distances = new ArrayList<DistanceEntry>();
    
    /** Creates a new instance of Club */
    public Club() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException {
        name = name.trim();
        if (name.length() == 0) {
            throw new ValidationException("Club name is empty");
        }
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    public Collection<Member> getMembers() {
        return members;
    }

    public Collection<Racepoint> getRacepoints() {
        return racepoints;
    }
    
    public void addMember(Member member) {
        if (members.contains( member ) || !members.add( member )) {
            throw new IllegalArgumentException();
        }
        for (Racepoint racepoint: racepoints) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (distances.contains( entry ) || !distances.add(entry)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void addRacepoint(Racepoint racepoint) {
        if (racepoints.contains( racepoint ) || !racepoints.add( racepoint )) {
            throw new IllegalArgumentException();
        }
        for (Member member: members) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (distances.contains( entry ) || !distances.add(entry)) {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void removeMember(Member member) {
        if (!members.contains( member ) || !members.remove( member )) {
            throw new IllegalArgumentException();
        }
        for (Racepoint racepoint: racepoints) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (!distances.contains( entry ) || !distances.remove(entry)) {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void removeRacepoint(Racepoint racepoint) {
        if (!racepoints.contains( racepoint ) || !racepoints.remove( racepoint )) {
            throw new IllegalArgumentException();
        }
        for (Member member: members) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (!distances.contains( entry ) || !distances.remove(entry)) {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public int getNumberOfMembers() {
        return members.size();
    }
    
    public int getNumberOfRacepoints() {
        return racepoints.size();
    }

    public DistanceEntry getDistanceEntry(Member member, Racepoint racepoint) {
        DistanceEntry entry = new DistanceEntry(member, racepoint, null);
        for (DistanceEntry stored: distances) {
            if (entry.equals(stored)) {
                return stored;
            }
        }
        throw new IllegalArgumentException();
    }
    
    public Distance getDistance(Member member, Racepoint racepoint) {
        return getDistanceEntry(member, racepoint).getDistance();
    }
    
    public void setDistance(Member member, Racepoint racepoint, Distance distance) {
        getDistanceEntry(member, racepoint).setDistance(distance);
    }
    
    public SortedMap<Racepoint, Distance> getDistancesForMember(Member member) {
        SortedMap<Racepoint, Distance> retval = new TreeMap<Racepoint, Distance>();
        for (Racepoint racepoint: racepoints) {
            retval.put(racepoint, getDistance(member, racepoint));
        }
        return retval;
    }
    
    public SortedMap<Member, Distance> getDistancesForRacepoint(Racepoint racepoint) {
        SortedMap<Member, Distance> retval = new TreeMap<Member, Distance>();
        for (Member member: members) {
            retval.put(member, getDistance(member, racepoint));
        }
        return retval;
    }
        
}
