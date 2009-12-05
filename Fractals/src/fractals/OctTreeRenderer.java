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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public final class OctTreeRenderer
{
    private OctTreeRenderer()
    {
    }

    public static void render(
            final OctTree segmentation,
            final Graphics g,
            final double size,
            final Color backgroundColor)
    {
        final long startTime = System.currentTimeMillis();

        final double theta = System.currentTimeMillis() * 0.0001;

        final Rectangle bounds = g.getClipBounds();
        double[][] distances = new double[bounds.height][];
        for (int iy = bounds.y; iy < (bounds.y + bounds.height); iy++) {
            distances[iy - bounds.y] = new double[bounds.width];
            for (int ix = bounds.x; ix < (bounds.x + bounds.width); ix++) {
                final double tx = 0.0;
                final double ty = 0.0;
                final double tz = -1.5;
                final double tdx = (ix - size) / size;
                final double tdy = (iy - size) / size;
                final double tdz = 1.0;

                final double x = Math.cos(theta) * tx - Math.sin(theta) * tz;
                final double y = ty;
                final double z = Math.sin(theta) * tx + Math.cos(theta) * tz;
                final double dx = Math.cos(theta) * tdx - Math.sin(theta) * tdz;
                final double dy = tdy;
                final double dz = Math.sin(theta) * tdx + Math.cos(theta) * tdz;

                final double result = segmentation.firstHit(x, y, z, dx, dy, dz);
                distances[iy - bounds.y][ix - bounds.x] = result;
            }
        }

        for (int iy = bounds.y + 1; iy < (bounds.y + bounds.height) - 1; iy++) {
            for (int ix = bounds.x + 1; ix < (bounds.x + bounds.width) - 1; ix++) {
                final double z = distances[iy - bounds.y][ix - bounds.x];
                if (Double.isNaN(z)) {
                    g.setColor(backgroundColor);
                } else {
                    final double dzdx = (distances[iy - bounds.y][ix - bounds.x + 1] - distances[iy - bounds.y][ix - bounds.x - 1]) / 2.0 * size / z;
                    final double dzdy = (distances[iy - bounds.y + 1][ix - bounds.x] - distances[iy - bounds.y - 1][ix - bounds.x]) / 2.0 * size / z;
                    final double shade = 1.0 / Math.sqrt(1.0 + dzdx*dzdx + dzdy*dzdy);
                    final Color color = new Color((float)(shade*shade), (float)(shade*shade), (float)(shade));
                    g.setColor(color);
                }
                g.fillRect(ix, iy, 1, 1);
            }
        }

        final long endTime = System.currentTimeMillis();

        //System.out.println("Render took: " + (endTime - startTime) + "ms");
    }
}
