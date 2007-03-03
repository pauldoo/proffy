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

/**
 * Associates a distance measurement with a member and racepoint pair.
 */
public class DistanceEntry implements Serializable, Comparable<DistanceEntry> {

    private static final long serialVersionUID = 42L;

    private final Member member;
    private final Racepoint racepoint;
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

    public Racepoint getRacepoint() {
        return racepoint;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

}
