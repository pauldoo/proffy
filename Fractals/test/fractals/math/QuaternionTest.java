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

package fractals.math;

import junit.framework.TestCase;

/**
 *
 * @author pauldoo
 */
public class QuaternionTest extends TestCase {
    
    public QuaternionTest(String testName) {
        super(testName);
    }

    public void testIdentity() {
        Quaternion q = Quaternion.identityRotation();
        q = Quaternion.multiply(q, q);
        assertEquals(q, Quaternion.identityRotation());
    }

    private static boolean approximatelyEqual(Quaternion a, Quaternion b)
    {
        return
                Quaternion.subtract(a, b).magnitudeSquared() <= 1e-6 ||
                Quaternion.subtract(a.negate(), b).magnitudeSquared() <= 1e-6;
    }

    public void testCreateRotation()
    {
        {
            Quaternion q = Quaternion.createRotation(new Triplex(1.0, 0.0, 0.0), 0.0);
            assertTrue(approximatelyEqual(q, Quaternion.identityRotation()));
        }
        {
            Quaternion q = Quaternion.createRotation(new Triplex(1.0, 0.0, 0.0), Math.PI * 2.0);
            assertTrue(approximatelyEqual(q, Quaternion.identityRotation()));
        }
        {
            Quaternion q = Quaternion.createRotation(new Triplex(0.0, 1.0, 0.0), Math.PI * 2.0 / 3.0);
            q = Quaternion.multiply(Quaternion.multiply(q, q), q).normalize();
            assertTrue(approximatelyEqual(q, Quaternion.identityRotation()));
        }
        {
            Quaternion q = Quaternion.createRotation(new Triplex(0.0, 0.0, 1.0), Math.PI * 2.0 / 4.0);
            q = Quaternion.multiply(Quaternion.multiply(q, q), q).normalize();
            assertFalse(approximatelyEqual(q, Quaternion.identityRotation()));
        }
        {
            Quaternion q = Quaternion.createRotation(new Triplex(0.0, 0.0, 1.0), Math.PI * 2.0 / 4.0);
            q = Quaternion.multiply(Quaternion.multiply(Quaternion.multiply(q, q), q), q).normalize();
            assertTrue(approximatelyEqual(q, Quaternion.identityRotation()));
        }
    }
}
