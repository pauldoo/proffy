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
public class Result implements Serializable, Comparable<Result> {
    
    private static final long serialVersionUID = 42L;
    
    private Member member;
    private String ringNumber;
    private long time;
    
    /** Creates a new instance of Result */
    public Result(Member member, String ringNumber, long time) {
        this.setMember(member);
        this.setRingNumber(ringNumber);
        this.time = time;
    }
    
    public boolean equals(Object other) {
        return equals((Result)other);
    }
    
    public boolean equals(Result other) {
        if (this == other) {
            return true;
        }
        return
                getMember().equals(other.getMember()) &&
                getRingNumber().equals(other.getRingNumber());
    }
    
    public int compareTo(Result other) {
        if (this == other) {
            return 0;
        }
        int result = getMember().compareTo(other.getMember());
        if (result != 0) {
            return result;
        }
        return getRingNumber().compareTo(other.getRingNumber());
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getRingNumber() {
        return ringNumber;
    }

    public void setRingNumber(String ringNumber) {
        this.ringNumber = ringNumber;
    }
}
