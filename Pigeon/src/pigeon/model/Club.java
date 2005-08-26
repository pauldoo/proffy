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
import java.util.Vector;

/**
 *
 * @author Paul
 */
public class Club implements Serializable {
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    private Vector<Member> members;
    private Vector<Racepoint> racepoints;
    
    /** Creates a new instance of Club */
    public Club() {
        members = new Vector<Member>();
        racepoints = new Vector<Racepoint>();
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
        return members;
    }

    public Vector<Racepoint> getRacepoints() {
        return racepoints;
    }
    
    public void addMember(Member member) {
        getMembers().add( member );
    }

    public void addRacepoint(Racepoint racepoint) {
        getRacepoints().add( racepoint );
    }
    
}
