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
 * Stores a single distance (stored in metres) and provides accessors
 * to return the distance in imperial units.
 */
public final class Distance implements Serializable, Comparable<Distance> {

    private static final long serialVersionUID = 7289132359169706543L;

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
