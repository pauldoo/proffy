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

/**
 *
 * @author pauldoo
 */
public class Time {
    
    private String ringNumber;
    private long time;
    
    /** Creates a new instance of Time */
    public Time(String ringNumber, long time) {
        this.setRingNumber(ringNumber);
        this.setMemberTime(time);
    }

    public String getRingNumber() {
        return ringNumber;
    }

    public void setRingNumber(String ringNumber) {
        this.ringNumber = ringNumber;
    }

    public long getMemberTime() {
        return time;
    }

    public void setMemberTime(long time) {
        this.time = time;
    }
    
    public boolean equals(Object other) {
        return equals((Time)other);
    }
    
    public boolean equals(Time other) {
        return this.getRingNumber().equals(other.getRingNumber());
    }
}
