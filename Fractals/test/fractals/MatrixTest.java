/*
    Copyright (C) 2009  Paul Richards.

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

    public void testCreate() {
        {
            Matrix a = Matrix.create3x5(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0);
            assertEquals(a.rows(), 3);
            assertEquals(a.columns(), 5);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    assertEquals(a.get(i, j), i * 5 + j + 1.0);
                }
            }
        }

        {
            Matrix b = Matrix.create5x3(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0, 120.0, 130.0, 140.0, 150.0);
            assertEquals(b.rows(), 5);
            assertEquals(b.columns(), 3);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(b.get(i, j), (i * 3 + j + 1.0) * 10.0);
                }
            }
        }

        {
            Matrix c = Matrix.create5x5(-10.0, -20.0, -30.0, -40.0, -50.0, -60.0, -70.0, -80.0, -90.0, -100.0, -110.0, -120.0, -130.0, -140.0, -150.0, -160.0, -170.0, -180.0, -190.0, -200.0, -210.0, -220.0, -230.0, -240.0, -250.0);
            assertEquals(c.rows(), 5);
            assertEquals(c.columns(), 5);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    assertEquals(c.get(i, j), (i * 5 + j + 1.0) * -10.0);
                }
            }
        }
    }

    public void testMultiply() {
        assertTrue(false); // Not yet written
    }
}
