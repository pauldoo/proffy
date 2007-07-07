/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Stores top level information about a racing organization (be that a federation or club).
 *
 * Maintains a list of members, racepoints, and distance entries.
 *
 * This final class was once called "Club", and sadly many places in the codebase still refer to
 * the old name.
 */
public final class Organization implements Serializable
{
    private static final long serialVersionUID = 5358293332608930714L;

    private String name;
    private List<Member> members = new ArrayList<Member>();
    private List<Racepoint> racepoints = new ArrayList<Racepoint>();
    private Collection<DistanceEntry> distances = new ArrayList<DistanceEntry>();

    public Organization() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException {
        name = name.trim();
        if (name.length() == 0) {
            throw new ValidationException("Organisation name is empty");
        }
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    public List<Member> getMembers() {
        return Utilities.unmodifiableSortedList(members);
    }

    public List<Racepoint> getRacepoints() {
        return Utilities.unmodifiableSortedList(racepoints);
    }

    public void addMember(Member member) throws ValidationException {
        if (members.contains( member ) || !members.add( member )) {
            throw new ValidationException("Member already exists");
        }
        for (Racepoint racepoint: racepoints) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (distances.contains( entry ) || !distances.add(entry)) {
                assert false : entry;
            }
        }
    }

    public void addRacepoint(Racepoint racepoint) throws ValidationException {
        if (racepoints.contains( racepoint ) || !racepoints.add( racepoint )) {
            throw new ValidationException("Racepoint already exists");
        }
        for (Member member: members) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (distances.contains( entry ) || !distances.add(entry)) {
                assert false : entry;
            }
        }
    }

    public void removeMember(Member member) {
        if (!members.contains( member ) || !members.remove( member )) {
            throw new IllegalArgumentException("Member doesn't exist");
        }
        for (Racepoint racepoint: racepoints) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (!distances.contains( entry ) || !distances.remove(entry)) {
                assert false : entry;
            }
        }
    }

    public void removeRacepoint(Racepoint racepoint) {
        if (!racepoints.contains( racepoint ) || !racepoints.remove( racepoint )) {
            throw new IllegalArgumentException("Racepoint doesn't exist");
        }
        for (Member member: members) {
            DistanceEntry entry = new DistanceEntry(member, racepoint, Distance.createFromMetric(0));
            if (!distances.contains( entry ) || !distances.remove(entry)) {
                assert false : entry;
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
        throw new IllegalArgumentException("Member / Racepoint doesn't exist");
    }

    public Distance getDistance(Member member, Racepoint racepoint) {
        return getDistanceEntry(member, racepoint).getDistance();
    }

    public void setDistance(Member member, Racepoint racepoint, Distance distance) {
        getDistanceEntry(member, racepoint).setDistance(distance);
    }

    public Map<Racepoint, Distance> getDistancesForMember(Member member) {
        Map<Racepoint, Distance> retval = new TreeMap<Racepoint, Distance>();
        for (Racepoint racepoint: racepoints) {
            retval.put(racepoint, getDistance(member, racepoint));
        }
        return retval;
    }

    public Map<Member, Distance> getDistancesForRacepoint(Racepoint racepoint) {
        Map<Member, Distance> retval = new TreeMap<Member, Distance>();
        for (Member member: members) {
            retval.put(member, getDistance(member, racepoint));
        }
        return retval;
    }

}
