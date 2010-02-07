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

/**
    Stores all properties of a camera in 3D space.
    The representation is intentionally more restrictive than
    a complexe 3x4 or 4x4 matrix in order to preserve certain
    properties.

    The default orientation of a camera (with no rotation) is to point
    towards +Z.  X and Y in this orientation align with X and Y after
    projection.
*/
final class Camera3D
{
    public final Triplex position;
    public final Quaternion rotation;

    Camera3D(final Triplex position, final Quaternion rotation)
    {
        this.position = position;
        this.rotation = rotation.normalize();
    }

    /**
        Constructs a 4x4 matrix representing
        the complete transform from real world
        coordinates to screen space coordinates.
    */
    Matrix toProjectionMatrix()
    {
        Matrix transform = Matrix.create4x4(position.negate(), rotation);

        Matrix result = Matrix.multiply(
            createProjectionMatrix(),
            transform);
        return result;
    }

    /**
        The simplest perspective projection matrix that makes
        inverting the complete world to screen matrix easy.
    */
    private static Matrix createProjectionMatrix()
    {
        return Matrix.create4x4(
                1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 1.0,
                0.0, 0.0, 1.0, 0.0);
    }

    final Camera3D replicateAddRotation(Quaternion rotationStep)
    {
        return new Camera3D(
                this.position,
                Quaternion.multiply(this.rotation, rotationStep));
    }

    final Camera3D replicateAddShift(Triplex shift)
    {
        return new Camera3D(
                Triplex.add(this.position, shift),
                this.rotation);
    }
}
