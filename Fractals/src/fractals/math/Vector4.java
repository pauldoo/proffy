/*
    Copyright (C) 2007, 2008  Paul Richards.

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

/**
    Simple struct-like object for storing 4D vectors.
*/
public final class Vector4
{
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    
    public Vector4(double a, double b, double c, double d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    @Override
    public int hashCode()
    {
        return
            new Double(getA()).hashCode() ^
            new Double(getB()).hashCode() ^
            new Double(getC()).hashCode() ^
            new Double(getD()).hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        return equals((Vector4)o);
    }
    
    boolean equals(Vector4 other)
    {
        return
            this == other || (
                this.getA() == other.getA() &&
                this.getB() == other.getB() &&
                this.getC() == other.getC() &&
                this.getD() == other.getD());
    }
    
    @Override
    public String toString()
    {
        return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ")";
    }
    
    public static Vector4 add(Vector4 lhs, Vector4 rhs)
    {
        return new Vector4(
                lhs.getA() + rhs.getA(),
                lhs.getB() + rhs.getB(),
                lhs.getC() + rhs.getC(),
                lhs.getD() + rhs.getD());
    }
    
    public Vector4 add(Vector4 rhs)
    {
        return add(this, rhs);
    }
    
    public static Vector4 multiply(Vector4 vector, double scalar)
    {
        return new Vector4(
                vector.getA() * scalar,
                vector.getB() * scalar,
                vector.getC() * scalar,
                vector.getD() * scalar);
    }
    
    public Vector4 multiply(double scalar)
    {
        return multiply(this, scalar);
    }
}
