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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
    Mutable class which stores the completed output
    for a rendering.  This is essentially a mutating set
    of Tiles.
*/
public final class Canvas
{
    /**
        Comparator that ensures tiles with the highest zoom level appear last.
    */
    private static final class LowestZoomFirstComparator implements Comparator<Tile>
    {
        public int compare(Tile o1, Tile o2)
        {
            int zoomDifference = o1.getPosition().getZoomIndex() - o2.getPosition().getZoomIndex();
            if (zoomDifference != 0) {
                return zoomDifference;
            } else {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        }
    }
    
    final SortedSet<RenderableTile> tiles;
    boolean updatedSinceLastBlit;
    
    public Canvas()
    {
        tiles = new TreeSet<RenderableTile>(new LowestZoomFirstComparator());
    }
    
    public void blitImmediately(Graphics2D g)
    {
        Collection<RenderableTile> tilesCopy;
        synchronized(this) {
            tilesCopy = new ArrayList<RenderableTile>(tiles);
            updatedSinceLastBlit = false;
        }
        long time = -System.currentTimeMillis();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        for (RenderableTile t: tilesCopy) {
            t.render(g);
        }
        time += System.currentTimeMillis();
        System.out.println(this.getClass().getName() + ".blitImmediately() took: " + time + "ms");
    }
    
    public boolean updatedSinceLastBlit()
    {
        synchronized(this) {
            return updatedSinceLastBlit;
        }
    }
    
    public void addTile(RenderableTile t)
    {
        synchronized(this) {
            tiles.add(t);
            updatedSinceLastBlit = true;
        }
    }
}
