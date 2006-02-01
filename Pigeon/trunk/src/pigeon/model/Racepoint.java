/*
 * Racepoint.java
 *
 * Created on 21 August 2005, 16:00
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.model;

import java.io.Serializable;

/**
 *
 * @author Paul
 */
public class Racepoint implements Serializable, Comparable<Racepoint> {
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    
    /** Creates a new instance of Racepoint */
    public Racepoint() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException {
        name = name.trim();
        if (name.length() == 0) {
            throw new ValidationException("Name is empty");
        }
        this.name = name;
    }
    
    public String toString() {
        return getName();
    }

    public int hashCode() {
        return name.hashCode();
    }
    
    public boolean equals(Object other) {
        return equals((Racepoint)other);
    }
    
    public boolean equals(Racepoint other) {
        if (this == other) {
            return true;
        } else {
            return name.equals(other.name);
        }
    }
    
    public int compareTo(Racepoint other) {
        if (this == other) {
            return 0;
        } else {
            return name.compareTo(other.name);
        }
    }
    
}
