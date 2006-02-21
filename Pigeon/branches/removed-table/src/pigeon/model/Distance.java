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
 * @author pauldoo
 */
public class Distance implements Serializable, Comparable<Distance> {
    
    private static final long serialVersionUID = 42L;
    
    private static final double METRES_PER_YARD = 0.9144;
    private static final int YARDS_PER_MILE = 1760;
    
    private final double distanceInMetres;
    
    /** Creates a new instance of Distance */
    private Distance(double metres) {
        this.distanceInMetres = metres;
    }
    
    public static Distance createFromMetric(double metres) {
        return new Distance(metres);
    }
    
    public static Distance createFromImperial(int miles, int yards) {
        return createFromMetric((miles * YARDS_PER_MILE + yards) * METRES_PER_YARD);
    }

    // Return distance in metres
    public double getMetres() {
        return distanceInMetres;
    }
    
    // Return distance in yards
    public double getYards() {
        return distanceInMetres / METRES_PER_YARD;
    }
    
    public int getMiles() {
        return (int)Math.round(getYards()) / YARDS_PER_MILE;
    }
    
    public int getYardsRemainder() {
        return (int)Math.round(getYards()) % YARDS_PER_MILE;
    }
    
    public String toString() {
        int miles = getMiles();
        int yards = getYardsRemainder();
        return miles + "miles " + yards + "yards";
    }    

    public int hashCode() {
        return new Double(distanceInMetres).hashCode();
    }
    
    public int compareTo(Distance other) {
        return Double.compare(this.distanceInMetres, other.distanceInMetres);
    }
    
}
