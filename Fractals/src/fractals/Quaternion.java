/*
    Copyright (C) 2010  Paul Richards.

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

package fractals;

/**
    Quaternion class.

    See: http://en.wikipedia.org/wiki/Quaternion

    Normalized quaternions (q = w + x*i + y*j + z*k, |q| = 1.0)
    are used in some contexts to represent rotations in 3D.
    In this usage the rotation axis is (x, y, z), and the angle of
    rotation is "a = 2*acos(w) = 2*asin((x^2+y^2+z^2)^0.5)".

    Using quaternions to represent rotations in this way allows
    quaternion multiplication to be used to compose rotations.
*/
final class Quaternion {
    /**
        Real part.
    */
    public final double a;

    /**
        I part.
    */
    public final double b;

    /**
        J part.
    */
    public final double c;

    /**
        K part.
    */
    public final double d;

    public Quaternion(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public final double magnitudeSquared()
    {
        return a*a + b*b + c*c + d*d;
    }

    public final double magnitude()
    {
        return Math.sqrt(magnitudeSquared());
    }

    public final Quaternion normalize()
    {
        final double magnitude = magnitude();
        return new Quaternion(
                a / magnitude,
                b / magnitude,
                c / magnitude,
                d / magnitude);
    }

    public static Quaternion multiply(Quaternion lhs, Quaternion rhs)
    {
        return new Quaternion(
                lhs.a*rhs.a - lhs.b*rhs.b - lhs.c*rhs.c - lhs.d*rhs.d,
                lhs.a*rhs.b + lhs.b*rhs.a + lhs.c*rhs.d - lhs.d*rhs.c,
                lhs.a*rhs.c - lhs.b*rhs.d + lhs.c*rhs.a + lhs.d*rhs.b,
                lhs.a*rhs.d + lhs.b*rhs.c - lhs.c*rhs.b + lhs.d*rhs.a);
    }

    public static Quaternion identityRotation()
    {
        return new Quaternion(1.0, 0.0, 0.0, 0.0);
    }
}
