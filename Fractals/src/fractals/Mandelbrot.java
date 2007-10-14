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


public final class Mandelbrot
{
    private static final long serialVersionUID = 2111799820114618095L;
    
    public void renderTile(double scale, Tile tile)
    {
        for (int iy = tile.getMinY(); iy <= tile.getMaxY(); iy++) {
            for (int ix = tile.getMinX(); ix <= tile.getMaxX(); ix++) {
                double x = ix * scale - 3.0 * 0.75;
                double y = iy * scale - 1.5 * 0.75;
                int v = iterateUntilEscapes(x, y);
                tile.setValue(ix, iy, v);
            }
        }
    }
    
    private int iterateUntilEscapes(double cR, double cI)
    {
        // z0 = c;
        double zR = cR;
        double zI = cI;
        
        int v;
        for (v = 0; v < 10000 && (zR*zR + zI*zI) < 20; v++) {
            // z => z^2 + c
            double zRn = zR*zR - zI*zI + cR;
            double zIn = 2*zR*zI + cI;
            zR = zRn;
            zI = zIn;
        }
        if (v == 10000) v = 0;
        return v;
    }
}
