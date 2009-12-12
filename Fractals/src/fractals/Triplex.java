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

final class Triplex
{
    public final double x;
    public final double y;
    public final double z;

    Triplex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    Triplex squashNaNs()
    {
        return new Triplex(
            Double.isNaN(x) ? 0.0 : x,
            Double.isNaN(y) ? 0.0 : y,
            Double.isNaN(z) ? 0.0 : z);
    }

    static Triplex add(Triplex a, Triplex b)
    {
        return new Triplex(
                a.x + b.x,
                a.y + b.y,
                a.z + b.z);
    }

    static Triplex power(Triplex a, double n)
    {
        if (n == 8.0) {
            return power8(a);
        } else {
            final double r = Math.sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
            final double theta = Math.atan2( Math.sqrt(a.x*a.x + a.y*a.y), a.z );
            final double phi = Math.atan2(a.y, a.x);
            return new Triplex(
                    Math.pow(r, n) * Math.sin(theta * n) * Math.cos(phi * n),
                    Math.pow(r, n) * Math.sin(theta * n) * Math.sin(phi * n),
                    Math.pow(r, n) * Math.cos(theta * n));
        }
    }

    private static Triplex power8(Triplex a)
    {
        final double w2 = a.x * a.x + a.y * a.y;
        final double r2 = w2 + a.z * a.z;
        final double w = Math.sqrt(w2);
        final double r = Math.sqrt(r2);
        final double cPhi = a.x / w;
        final double sPhi = a.y / w;
        final double cTheta = a.z / r;
        final double sTheta = w / r;
        final Complex phi8 = new Complex(cPhi, sPhi);
        Complex.squareReplace(phi8);
        Complex.squareReplace(phi8);
        Complex.squareReplace(phi8);
        final Complex theta8 = new Complex(cTheta, sTheta);
        Complex.squareReplace(theta8);
        Complex.squareReplace(theta8);
        Complex.squareReplace(theta8);
        final double r4 = r2 * r2;
        final double r8 = r4 * r4;
        return new Triplex(
            r8 * phi8.getReal() * theta8.getImaginary(),
            r8 * phi8.getImaginary() * theta8.getImaginary(),
            r8 * theta8.getReal()).squashNaNs();
    }
}
