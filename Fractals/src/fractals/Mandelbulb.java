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

final class Mandelbulb {
    static boolean evaluate(final Triplex c)
    {
        final int maxIter = 50;
        final double n = 8;

        Triplex z = new Triplex(0.0, 0.0, 0.0);
        int i;
        for (i = 0; i < maxIter && (z.x*z.x + z.y*z.y + z.z*z.z) < 100.0; i++) {
            z = Triplex.add(Triplex.power(z, n), c);
        }
        return i == maxIter;
    }
}
