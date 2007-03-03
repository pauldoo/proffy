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
import java.util.Collection;
import java.util.SortedMap;
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
    private static final long serialVersionUID = 42L;

    private String name;
    private Collection<Member> members = new ArrayList<Member>();
    private Collection<Racepoint> racepoints = new ArrayList<Racepoint>();
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

    public Collection<Member> getMembers() {
        return members;
    }

    public Collection<Racepoint> getRacepoints() {
        return racepoints;
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
