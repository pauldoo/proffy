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

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

/**
    Mutable class which stores the completed output
    for a rendering.  This is essentially a mutating set
    of Tiles.
*/
public final class Canvas
{
    final Collection<Tile> tiles;
    boolean updatedSinceLastBlit;
    
    public Canvas()
    {
        tiles = new ArrayList<Tile>();
    }
    
    public void blitImmediately(Graphics g, double exposure)
    {
        Collection<Tile> tilesCopy;
        synchronized(this) {
            tilesCopy = new ArrayList<Tile>(tiles);
            updatedSinceLastBlit = false;
        }
        long time = -System.currentTimeMillis();
        for (Tile t: tilesCopy) {
            t.render(g, exposure);
        }
        time += System.currentTimeMillis();
        System.out.println("blitImmediately() took: " + time + "ms");
    }
    
    public boolean updatedSinceLastBlit()
    {
        synchronized(this) {
            return updatedSinceLastBlit;
        }
    }
    
    public void addTile(Tile t)
    {
        synchronized(this) {
            tiles.add(t);
            updatedSinceLastBlit = true;
        }
    }
}
