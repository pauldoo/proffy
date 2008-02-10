/*
    Copyright (C) 2008  Paul Richards.

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
    Immutable complex number class.
*/
final class Complex
{
    /// Real part
    private final double real;
    
    /// Imaginary part
    private final double imaginary;
    
    public Complex(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    public static Complex createFromPolar(double r, double theta)
    {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    @Override
    public int hashCode()
    {
        return
            new Double(R()).hashCode() ^
            new Double(I()).hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        return equals((Complex)o);
    }
    
    public boolean equals(Complex other)
    {
        return
            this == other || (
                this.R() == other.R() &&
                this.I() == other.I());
    }
    
    @Override
    public String toString()
    {
        return R() + " + " + I() + "i";
    }
    
    public double getReal()
    {
        return real;
    }

    public double getImaginary()
    {
        return imaginary;
    }
    
    /**
        Shorter named version of getReal() to make method bodies more readable.
    */
    private double R()
    {
        return getReal();
    }

    /**
        Shorter named version of getImaginary() to make method bodies more readable.
    */
    private double I()
    {
        return getImaginary();                
    }
    
    public double polarR()
    {
        return magnitude();
    }
    
    public double polarTheta()
    {
        return Math.atan2(I(), R());
    }
    
    public double magnitudeSquared()
    {
        return R() * R() + I() * I();
    }

    public double magnitude()
    {
        return Math.sqrt(magnitudeSquared());
    }
    
    public Complex conjugate()
    {
        return new Complex(R(), -I());
    }
    
    public Complex inverse()
    {
        return conjugate().divide(magnitudeSquared());
    }
    
    public static Complex add(Complex a, Complex b)
    {
        return new Complex(
                a.R() + b.R(),
                a.I() + b.I());
    }
    
    public Complex add(Complex b)
    {
        return add(this, b);
    }
    
    public static Complex subtract(Complex a, Complex b)
    {
        return new Complex(
                a.R() - b.R(),
                a.I() - b.I());
    }

    public Complex subtract(Complex b)
    {
        return subtract(this, b);
    }
    
    public static Complex multiply(Complex a, Complex b)
    {
        return new Complex(
                a.R() * b.R() - a.I() * b.I(),
                a.I() * b.R() + a.R() * b.I());
    }
    
    public Complex multiply(Complex b)
    {
        return multiply(this, b);
    }

    public static Complex divide(Complex a, double b)
    {
        return new Complex(
                a.R() / b,
                a.I() / b);
    }
    
    public Complex divide(double b)
    {
        return divide(this, b);
    }
    
    public static Complex divide(Complex a, Complex b)
    {
        return a.multiply(b.conjugate()).divide(b.magnitudeSquared());
    }
    
    public Complex divide(Complex b)
    {
        return divide(this, b);
    }
    
    public static Complex power(Complex a, Complex b)
    {
        return createFromPolar(
                Math.pow(a.polarR(), b.R()) * Math.exp(-b.I() * a.polarTheta()),
                b.I() * Math.log(a.polarR()) + b.R() * a.polarTheta());
    }
    
    public Complex power(Complex b)
    {
        return power(this, b);
    }
}
