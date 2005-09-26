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
import java.util.Map;
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
    
    // TODO: Fix deletion of members and racepoints
    // TODO: Fix serialization so that member/racepoint references are stored 
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    private SortedSet<Member> members;
    private SortedSet<Racepoint> racepoints;
    private SortedMap<Member, SortedMap<Racepoint, Distance>> distances; 
    
    /** Creates a new instance of Club */
    public Club() {
        members = new TreeSet<Member>();
        racepoints = new TreeSet<Racepoint>();
        distances = new TreeMap<Member, SortedMap<Racepoint, Distance>>();
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
        return new Vector<Member>(members);
    }

    /** Returns a shallow copy of the racepoints list.
     */
    public Vector<Racepoint> getRacepoints() {
        return new Vector<Racepoint>(racepoints);
    }
    
    public void addMember(Member member) {
        if (!members.add( member )) {
            throw new IllegalArgumentException();
        }
        SortedMap<Racepoint, Distance> racepointDistances = new TreeMap<Racepoint, Distance>();
        for (Racepoint racepoint: racepoints) {
            racepointDistances.put(racepoint, Distance.createFromMetric(0));
        }
        distances.put(member, racepointDistances);
    }

    public void addRacepoint(Racepoint racepoint) {
        if (!racepoints.add( racepoint )) {
            throw new IllegalArgumentException();
        }
        for (Map.Entry<Member, SortedMap<Racepoint, Distance>> entry: distances.entrySet()) {
            entry.getValue().put(racepoint, Distance.createFromMetric(0));
        }
    }
    
    public void removeMember(Member member) {
        members.remove(member);
    }
    
    public void removeRacepoint(Racepoint racepoint) {
        racepoints.remove(racepoint);
    }
    
    public int getNumberOfMembers() {
        return members.size();
    }
    
    public int getNumberOfRacepoints() {
        return racepoints.size();
    }
    
    public Distance getDistance(Member member, Racepoint racepoint) {
        return distances.get(member).get(racepoint);
    }
    
    public void setDistance(Member member, Racepoint racepoint, Distance distance) {
        distances.get(member).put(racepoint, distance);
    }
    
    public SortedMap<Racepoint, Distance> getDistancesForMember(Member member) {
        return distances.get(member);
    }
    
    public SortedMap<Member, Distance> getDistancesForRacepoint(Racepoint racepoint) {
        SortedMap<Member, Distance> retval = new TreeMap<Member, Distance>();
        for (Map.Entry<Member, SortedMap<Racepoint, Distance>> entry: distances.entrySet()) {
            retval.put(entry.getKey(), entry.getValue().get(racepoint));
        }
        return retval;
    }
}
