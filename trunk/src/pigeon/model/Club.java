/*
 * Club.java
 *
 * Created on 21 August 2005, 15:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author Paul
 */
public class Club implements Serializable {

    // LIKE: Member/Racepoint lists and DistanceEntry list to be something more intelligent.
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    private Collection<Member> members;
    private Collection<Racepoint> racepoints;
    private Collection<DistanceEntry> distances;
    
    /** Creates a new instance of Club */
    public Club() {
        members = new ArrayList<Member>();
        racepoints = new ArrayList<Racepoint>();
        distances = new ArrayList<DistanceEntry>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    /** Returns a shallow copy of the members list.
     */
    public Vector<Member> getMembers() {
        return new Vector<Member>(new TreeSet<Member>(members));
    }

    /** Returns a shallow copy of the racepoints list.
     */
    public Vector<Racepoint> getRacepoints() {
        return new Vector<Racepoint>(new TreeSet<Racepoint>(racepoints));
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
        if (!racepoints.contains( racepoint ) || !racepoints.add( racepoint )) {
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
    
    public SortedSet<DistanceEntry> getDistances() {
        return new TreeSet<DistanceEntry>( distances );
    }
    
}
