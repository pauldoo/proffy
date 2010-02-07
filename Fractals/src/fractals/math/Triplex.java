/*
    Copyright (C) 2009, 2010  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package fractals.math;

import fractals.Utilities;

public final class Triplex
{
    public final double x;
    public final double y;
    public final double z;

    public Triplex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
        Asserts that no values are NaN, then returns 'this'.
    */
    Triplex assertNotNaN()
    {
        Utilities.assertNotNaN(x);
        Utilities.assertNotNaN(y);
        Utilities.assertNotNaN(z);
        return this;
    }

    public static Triplex add(Triplex a, Triplex b)
    {
        return new Triplex(
                a.x + b.x,
                a.y + b.y,
                a.z + b.z);
    }

    public static Triplex subtract(Triplex a, Triplex b)
    {
        return new Triplex(
                a.x - b.x,
                a.y - b.y,
                a.z - b.z);
    }

    public static Triplex multiply(Triplex a, double b)
    {
        return new Triplex(
                a.x * b,
                a.y * b,
                a.z * b);
    }

    public final double magnitudeSquared()
    {
        return x*x + y*y + z*z;
    }

    public final double magnitude()
    {
        return Math.sqrt(magnitudeSquared());
    }

    public final Triplex negate()
    {
        return new Triplex(-x, -y, -z);
    }

    public static Triplex normalize(Triplex a)
    {
        final double mag = a.magnitude();
        return new Triplex(
                a.x / mag,
                a.y / mag,
                a.z / mag);
    }

    public static Triplex crossProduct(Triplex a, Triplex b)
    {
        return new Triplex(
                a.y * b.z - b.y * a.z,
                a.z * b.x - b.z * a.x,
                a.x * b.y - b.x * a.y);
    }

    public static double dotProduct(Triplex a, Triplex b)
    {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public Triplex normalize()
    {
        return normalize(this);
    }

    public boolean approximatelyEquals(Triplex other, double tollerance) {
        return subtract(this, other).magnitudeSquared() <= (tollerance*tollerance);
    }
    public boolean approximatelyEquals(Triplex other) {
        return approximatelyEquals(other, 1e-6);
    }
}
