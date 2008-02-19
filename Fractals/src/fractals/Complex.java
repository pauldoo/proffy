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
    Complex number class.
 
    This class is not immutable but most methods behave as if it were. 
    That is they return new instances rather than modify any existing
    instance.
 
    If the overhead of creating new instances is deemed too high, then the
    "xxxReplace()" methods should be used instead.  These perform their
    operation and write the result out to an existing instance.
*/
final class Complex implements Cloneable
{
    /// Real part
    private double real;
    
    /// Imaginary part
    private double imaginary;
    
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
    public Complex clone()
    {
        try {
            return (Complex)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("Clone should always succeed", ex);
        }
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
    
    public void setReal(double real)
    {
        this.real = real;
    }

    public double getImaginary()
    {
        return imaginary;
    }

    public void setImaginary(double imaginary)
    {
        this.imaginary = imaginary;
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
    
    public Complex negate()
    {
        return new Complex(-R(), -I());
    }
    
    public static void addReplace(Complex a, Complex b)
    {
        a.setReal(a.R() + b.R());
        a.setImaginary(a.I() + b.I());
    }
    
    public static Complex add(Complex a, Complex b)
    {
        Complex result = a.clone();
        addReplace(result, b);
        return result;
    }
    
    public Complex add(Complex b)
    {
        return add(this, b);
    }
    
    public static void subtractReplace(Complex a, Complex b)
    {
        a.setReal(a.R() - b.R());
        a.setImaginary(a.I() - b.I());
    }
    
    public static Complex subtract(Complex a, Complex b)
    {
        Complex result = a.clone();
        subtractReplace(result, b);
        return result;
    }

    public Complex subtract(Complex b)
    {
        return subtract(this, b);
    }
    
    public static void multiplyReplace(Complex a, Complex b)
    {
        double r = a.R() * b.R() - a.I() * b.I();
        double i = a.I() * b.R() + a.R() * b.I();
        a.setReal(r);
        a.setImaginary(i);
    }
    
    public static Complex multiply(Complex a, Complex b)
    {
        Complex result = a.clone();
        multiplyReplace(result, b);
        return result;
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
