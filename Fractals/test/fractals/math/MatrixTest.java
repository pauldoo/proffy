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

import junit.framework.TestCase;

/**
 *
 * @author pauldoo
 */
public class MatrixTest extends TestCase {
    
    public MatrixTest(String testName) {
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

    public void testIdentity() {
        Matrix a = Matrix.createIdentity(2);
        assertEquals(a.get(0, 0), 1.0);
        assertEquals(a.get(0, 1), 0.0);
        assertEquals(a.get(1, 0), 0.0);
        assertEquals(a.get(1, 1), 1.0);

        Matrix b = Matrix.createIdentity(4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(b.get(i, j), (i==j) ? 1.0 : 0.0);
            }
        }
    }

    public void testCreateAndEquals() {
        Matrix a = Matrix.create3x5(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0);
        assertEquals(a.rows(), 3);
        assertEquals(a.columns(), 5);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(a.get(i, j), i * 5 + j + 1.0);
            }
        }

        Matrix b = Matrix.create5x3(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0, 120.0, 130.0, 140.0, 150.0);
        assertEquals(b.rows(), 5);
        assertEquals(b.columns(), 3);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(b.get(i, j), (i * 3 + j + 1.0) * 10.0);
            }
        }

        Matrix c = Matrix.create5x5(-10.0, -20.0, -30.0, -40.0, -50.0, -60.0, -70.0, -80.0, -90.0, -100.0, -110.0, -120.0, -130.0, -140.0, -150.0, -160.0, -170.0, -180.0, -190.0, -200.0, -210.0, -220.0, -230.0, -240.0, -250.0);
        assertEquals(c.rows(), 5);
        assertEquals(c.columns(), 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c.get(i, j), (i * 5 + j + 1.0) * -10.0);
            }
        }

        assertTrue(a.equals(a));
        assertTrue(b.equals(b));
        assertTrue(c.equals(c));
        assertTrue(a.equals(Matrix.create3x5(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0)));
        assertTrue(b.equals(Matrix.create5x3(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0, 120.0, 130.0, 140.0, 150.0)));
        assertTrue(c.equals(Matrix.create5x5(-10.0, -20.0, -30.0, -40.0, -50.0, -60.0, -70.0, -80.0, -90.0, -100.0, -110.0, -120.0, -130.0, -140.0, -150.0, -160.0, -170.0, -180.0, -190.0, -200.0, -210.0, -220.0, -230.0, -240.0, -250.0)));
        assertTrue(a.equals(b) == false);
        assertTrue(a.equals(c) == false);
        assertTrue(b.equals(c) == false);
        assertTrue(c.equals(Matrix.createIdentity(5)) == false);

        assertTrue(a.compareTo(a) == 0);
        assertTrue(b.compareTo(b) == 0);
        assertTrue(c.compareTo(c) == 0);
        assertTrue(a.compareTo(b) != 0);
        assertTrue(a.compareTo(c) != 0);
        assertTrue(b.compareTo(c) != 0);
        assertTrue(c.compareTo(Matrix.createIdentity(5)) != 0);
    }

    public void testMultiply() {
        Matrix a = Matrix.create3x5(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0);
        Matrix b = Matrix.create5x3(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0, 120.0, 130.0, 140.0, 150.0);
        Matrix c = Matrix.create5x5(-10.0, -20.0, -30.0, -40.0, -50.0, -60.0, -70.0, -80.0, -90.0, -100.0, -110.0, -120.0, -130.0, -140.0, -150.0, -160.0, -170.0, -180.0, -190.0, -200.0, -210.0, -220.0, -230.0, -240.0, -250.0);

        assertEquals(3, Matrix.multiply(a, b).rows());
        assertEquals(3, Matrix.multiply(a, b).columns());
        assertEquals(5, Matrix.multiply(b, a).rows());
        assertEquals(5, Matrix.multiply(b, a).columns());

        assertEquals(3, Matrix.multiply(a, c).rows());
        assertEquals(5, Matrix.multiply(a, c).columns());
        assertEquals(5, Matrix.multiply(c, b).rows());
        assertEquals(3, Matrix.multiply(c, b).columns());

        assertTrue(Matrix.multiply(c, Matrix.createIdentity(5)).equals(c));
        assertTrue(Matrix.multiply(Matrix.createIdentity(5), c).equals(c));
    }

    public void testInvert() {
        Matrix a = Matrix.create4x4(
                2, 3, 5, 7,
                11, 13, 17, 19,
                23, 29, 31, 37,
                41, 43, 47, 53);

        Matrix aInverse = Matrix.invert4x4(a);

        Matrix identity = Matrix.multiply(a, aInverse);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assert(Math.abs(identity.get(i, j) - ((i==j) ? 1.0 : 0.0)) < 1e-9);
            }
        }
    }

    static boolean approximatelyEqual(Matrix a, Matrix b)
    {
        if (a.rows() == b.rows() &&
            a.columns() == b.columns()) {

            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.columns(); j++) {
                    if (Math.abs(a.get(i, j) - b.get(i, j)) >= 1e-9) {
                        return false;
                    }
                }
            }

            return true;

        } else {
            return false;
        }
    }

    public void testCreate3x4()
    {
        {
            Matrix actual = Matrix.create4x4(new Triplex(0.0, 0.0, 0.0), Quaternion.identityRotation());
            Matrix expected = Matrix.create4x4(
                    1.0, 0.0, 0.0, 0.0,
                    0.0, 1.0, 0.0, 0.0,
                    0.0, 0.0, 1.0, 0.0,
                    0.0, 0.0, 0.0, 1.0);
            assertEquals(expected, actual);
        }
        {
            Matrix actual = Matrix.create4x4(new Triplex(1.0, 2.0, 3.0), Quaternion.identityRotation());
            Matrix expected = Matrix.create4x4(
                    1.0, 0.0, 0.0, 1.0,
                    0.0, 1.0, 0.0, 2.0,
                    0.0, 0.0, 1.0, 3.0,
                    0.0, 0.0, 0.0, 1.0);
            assertEquals(expected, actual);
        }
        {
            Matrix actual = Matrix.create4x4(new Triplex(1.0, 2.0, 3.0), Quaternion.createRotation(new Triplex(0.0, 1.0, 0.0), Math.PI));
            Matrix expected = Matrix.create4x4(
                    -1.0, 0.0, 0.0, -1.0,
                    0.0, 1.0, 0.0, 2.0,
                    0.0, 0.0, -1.0, -3.0,
                    0.0, 0.0, 0.0, 1.0);
            assertTrue(approximatelyEqual(expected, actual));
        }


    }
}
