/*
    Copyright (C) 2007  Paul Richards.

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

public class JuliaSet implements TileProvider<IntegerTile>
{
    private final double cReal;
    private final double cImag;
    
    public JuliaSet(double real, double imag)
    {
        this.cReal = real;
        this.cImag = imag;
    }
    
    public IntegerTile getTile(TilePosition position)
    {
        IntegerTile tile = new IntegerTile(position);
        for (int iy = position.getMinY(); iy <= position.getMaxY(); iy++) {
            for (int ix = position.getMinX(); ix <= position.getMaxX(); ix++) {
                final double r = ix * position.relativeScale() / 800;
                final double i = iy * position.relativeScale() / 800;
                int v = iterateUntilEscapes(r, i);
                tile.setValue(ix, iy, v);
            }
        }
        return tile;
    }

    private int iterateUntilEscapes(double zR, double zI)
    {
        int v;
        for (v = 0; v < 2000 && (zR*zR + zI*zI) < 20; v++) {
            // z => z^2 + c
            double zRn = zR*zR - zI*zI + cReal;
            double zIn = 2*zR*zI + cImag;
            zR = zRn;
            zI = zIn;
        }
        if (v == 2000) v = 0;
        return v;
    }
}
