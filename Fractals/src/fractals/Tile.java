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

import java.awt.Color;
import java.awt.Graphics;

/**
    A fixed size area of a rendered fractal.  The storage format
    is that convenient for the fractal algorithm, not necessarily convenient
    for rendering.
*/
public final class Tile
{
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    private final int minX;
    private final int minY;
    private final int[][] values;

    public Tile(final int minX, final int minY) {
        this.minX = minX;
        this.minY = minY;
        this.values = new int[HEIGHT][];
        for (int i = 0; i < HEIGHT; i++) {
            this.values[i] = new int[WIDTH];
        }
    }
    
    public void setValue(final int x, final int y, final int value)
    {
        int offsetX = x - minX;
        int offsetY = y - minY;
        if (offsetX >= 0 && offsetX < WIDTH && offsetY >= 0 && offsetY < HEIGHT) {
            values[offsetY][offsetX] = value;
        } else {
            throw new IllegalArgumentException("Out of bounds");
        }
    }
    
    public void render(Graphics g, double exposure)
    {
        if (g.hitClip(minX, minY, WIDTH, HEIGHT)) {
            for (int offsetY = 0; offsetY < HEIGHT; offsetY++) {
                for (int offsetX = 0; offsetX < WIDTH; offsetX++) {
                    final float v = (float)(1.0 - Math.exp(-exposure * values[offsetY][offsetX]));
                    final int x = minX + offsetX;
                    final int y = minY + offsetY;
                    g.setColor(new Color(v, v, v));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    public int getMinX()
    {
        return minX;
    }

    public int getMinY()
    {
        return minY;
    }
    
    public int getMaxX()
    {
        return getMinX() + WIDTH - 1;
    }
    
    public int getMaxY()
    {
        return getMinY() + HEIGHT - 1;
    }
}
