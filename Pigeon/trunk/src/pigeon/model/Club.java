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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author Paul
 */
public class Club implements Serializable {
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    private SortedSet<Member> members;
    private SortedSet<Racepoint> racepoints;
    
    /** Creates a new instance of Club */
    public Club() {
        members = new TreeSet<Member>();
        racepoints = new TreeSet<Racepoint>();
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
    }

    public void addRacepoint(Racepoint racepoint) {
        if (!racepoints.add( racepoint )) {
            throw new IllegalArgumentException();
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
}
