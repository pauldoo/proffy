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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public final class MandelbrotSet implements TileProvider<IntegerTile>
{
    public IntegerTile getTile(TilePosition pos)
    {
        IntegerTile tile = new IntegerTile(pos);
        for (int iy = pos.getMinY(); iy <= pos.getMaxY(); iy++) {
            for (int ix = pos.getMinX(); ix <= pos.getMaxX(); ix++) {
                final double r = ix * pos.relativeScale() / 800;
                final double i = iy * pos.relativeScale() / 800;
                int v = iterateUntilEscapes(r, i);
                tile.setValue(ix, iy, v);
            }
        }
        return tile;
    }
    
    private int iterateUntilEscapes(double cR, double cI)
    {
        // z0 = c;
        final Complex c = new Complex(cR, cI);
        Complex z = c;
        
        int v;
        for (v = 0; v < 1000 && z.magnitudeSquared() <= 4; v++) {
            // z => z^2 + c
            z = z.multiply(z).add(c);
        }
        if (v == 1000) v = 0;
        return v;
    }
}
