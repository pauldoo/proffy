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

import fractals.math.Matrix;
import fractals.math.Quaternion;
import fractals.math.Triplex;
import junit.framework.TestCase;

/**
 *
 * @author pauldoo
 */
public class Camera3DTest extends TestCase {
    
    public Camera3DTest(String testName) {
        super(testName);
    }

    public void testProjectionInvertability() {
        {
            Triplex position = new Triplex(2.0, 3.0, 5.0);
            Camera3D camera = new Camera3D(position, Quaternion.identityRotation());
            Matrix projectionMatrix = camera.toProjectionMatrix();
            Matrix invertedProjectionMatrix = Matrix.invert4x4(projectionMatrix);
            Triplex recoveredPosition = Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(0.0, 0.0, 1.0, 0.0)).toTriplex();
            assertTrue(recoveredPosition.approximatelyEquals(position));
            Triplex recoveredForward = Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(0.0, 0.0, 1.0, 1.0)).toTriplex();
            assertTrue(recoveredForward.approximatelyEquals(Triplex.add(position, new Triplex(0.0, 0.0, 1.0))));
            Triplex recoveredForwardRight = Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(1.0, 0.0, 1.0, 1.0)).toTriplex();
            assertTrue(recoveredForwardRight.approximatelyEquals(Triplex.add(position, new Triplex(1.0, 0.0, 1.0))));
            Triplex recoveredForwardUp = Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(0.0, 1.0, 1.0, 1.0)).toTriplex();
            assertTrue(recoveredForwardUp.approximatelyEquals(Triplex.add(position, new Triplex(0.0, 1.0, 1.0))));
        }
        {
            Triplex position = new Triplex(7.0, -8.0, 9.0);
            Camera3D camera = new Camera3D(position, Quaternion.createRotation(new Triplex(1.0, 0.0, 0.0), 4.0));
            Matrix projectionMatrix = camera.toProjectionMatrix();
            Matrix invertedProjectionMatrix = Matrix.invert4x4(projectionMatrix);
            Triplex recoveredPosition = Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(0.0, 0.0, 1.0, 0.0)).toTriplex();
            assertTrue(recoveredPosition.approximatelyEquals(position));
        }
    }

}
