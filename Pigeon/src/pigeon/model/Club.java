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

    public Vector<Member> getMembers() {
        return new Vector<Member>(members);
    }

    public Vector<Racepoint> getRacepoints() {
        return new Vector<Racepoint>(racepoints);
    }
    
    public void addMember(Member member) {
        members.add( member );
    }

    public void addRacepoint(Racepoint racepoint) {
        racepoints.add( racepoint );
    }
    
}
