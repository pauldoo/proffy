/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2007  Paul Richards
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
 * Stores a single distance (stored in metres) and provides accessors
 * to return the distance in imperial units.
 */
public class Distance implements Serializable, Comparable<Distance> {

    private static final long serialVersionUID = 42L;

    private final double distanceInMetres;

    private Distance(double metres) {
        this.distanceInMetres = metres;
    }

    public static Distance createFromMetric(double metres) {
        return new Distance(metres);
    }

    public static Distance createFromImperial(int miles, int yards) {
        return createFromMetric((miles * Constants.YARDS_PER_MILE + yards) * Constants.METRES_PER_YARD);
    }

    // Return distance in metres
    public double getMetres() {
        return distanceInMetres;
    }

    // Return distance in yards
    public double getYards() {
        return distanceInMetres / Constants.METRES_PER_YARD;
    }

    public int getMiles() {
        return (int)Math.round(getYards()) / Constants.YARDS_PER_MILE;
    }

    public int getYardsRemainder() {
        return (int)Math.round(getYards()) % Constants.YARDS_PER_MILE;
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
