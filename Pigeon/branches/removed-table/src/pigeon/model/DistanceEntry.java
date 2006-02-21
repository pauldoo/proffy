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
