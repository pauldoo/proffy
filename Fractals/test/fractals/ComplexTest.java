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

import junit.framework.TestCase;

public class ComplexTest extends TestCase {

    private final Complex a = new Complex(2, 3);
    private final Complex b = new Complex(4, -5);
    
    public ComplexTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateFromPolar()
    {
        double r = 7;
        double theta = 8;
        Complex expResult = new Complex(r * Math.cos(theta), r * Math.sin(theta));
        Complex result = Complex.createFromPolar(r, theta);
        assertEquals(expResult, result);
        assertApproximatelyEquals(7.0, result.polarR(), 1e-6);
        assertApproximatelyEquals(8.0 - 2 * Math.PI, result.polarTheta(), 1e-6);
    }

    public void testGetReal()
    {
        assertEquals(2.0, a.getReal());
        assertEquals(4.0, b.getReal());
    }

    public void testGetImaginary()
    {
        assertEquals(3.0, a.getImaginary());
        assertEquals(-5.0, b.getImaginary());
    }

    public void testMagnitudeSquared()
    {
        assertEquals(13.0, a.magnitudeSquared());
        assertEquals(41.0, b.magnitudeSquared());
    }

    /**
     * Test of magnitude method, of class Complex.
     */
    public void testMagnitude()
    {
        assertEquals(Math.sqrt(13.0), a.magnitude());
        assertEquals(Math.sqrt(41.0), b.magnitude());
    }

    public void testConjugate()
    {
        assertEquals(new Complex(2.0, -3.0), a.conjugate());
        assertEquals(new Complex(4.0, 5.0), b.conjugate());
    }

    public void testInverse()
    {
        assertEquals(new Complex(1.0, 0.0), a.multiply(a.inverse()));
        assertEquals(new Complex(1.0, 0.0), b.multiply(b.inverse()));
    }

    public void testAdd()
    {
        assertEquals(new Complex(6.0, -2.0), a.add(b));
    }

    public void testSubtract()
    {
        assertEquals(new Complex(-2.0, 8.0), a.subtract(b));
    }

    public void testMultiply()
    {
        assertEquals(new Complex(8.0 + 15.0, 12.0 - 10.0 ), a.multiply(b));
        assertEquals(new Complex(8.0 + 15.0, 12.0 - 10.0 ), b.multiply(a));
    }

    public void testDivide()
    {
        assertEquals(new Complex((8.0 - 15.0) / 41.0, (12.0 + 10.0) / 41.0 ), a.divide(b));
        //assertEquals(new Complex(8.0 + 15.0, 12.0 - 10.0 ), b.divide(a));
    }

    public void testPower()
    {
        assertApproximatelyEquals(a.multiply(a).multiply(a), a.power(new Complex(3.0, 0.0)), 1e-6);
        assertApproximatelyEquals(b.multiply(b).inverse(), b.power(new Complex(-2.0, 0.0)), 1e-6);
        assertApproximatelyEquals(new Complex(-1.0, 0.0) , new Complex(Math.E, 0.0).power(new Complex(0.0, Math.PI)), 1e-6);
    }
    
    private void assertApproximatelyEquals(double a, double b, double tollerance)
    {
        if (Math.abs(a - b) > tollerance) {
            assertEquals(a, b);
        }
    }
    
    private void assertApproximatelyEquals(Complex a, Complex b, double tollerance)
    {
        if (a.subtract(b).magnitudeSquared() > tollerance) {
            assertEquals(a, b);
        }
    }
}
