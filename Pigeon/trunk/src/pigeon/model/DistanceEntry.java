/*
 * DistanceEntry.java
 *
 * Created on 29 September 2005, 20:26
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
class DistanceEntry implements Serializable, Comparable<DistanceEntry> {
        
    private static final long serialVersionUID = 42L;

    private Member member;
    private Racepoint racepoint;
    private Distance distance;

    public DistanceEntry(Member member, Racepoint racepoint, Distance distance) {
        this.member = member;
        this.racepoint = racepoint;
        this.distance = distance;
    }

    public int hashCode() {
        return member.hashCode() ^ racepoint.hashCode();
    }

    public boolean equals(Object other) {
        return equals((DistanceEntry) other);
    }

    public boolean equals(DistanceEntry other) {
        if (this == other) {
            return true;
        } else {
            return this.member.equals(other.member) && this.racepoint.equals(other.racepoint);
        }
    }

    public int compareTo(DistanceEntry other) {
        int retval;
        retval = this.member.compareTo( other.member );
        if (retval != 0) {
            return retval;
        }
        retval = this.racepoint.compareTo( other.racepoint );
        if (retval != 0) {
            return retval;
        }
        return 0;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Racepoint getRacepoint() {
        return racepoint;
    }

    public void setRacepoint(Racepoint racepoint) {
        this.racepoint = racepoint;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

}
