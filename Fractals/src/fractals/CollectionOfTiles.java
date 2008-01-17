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
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
    Mutable class which stores the completed output
    for a rendering.  This is essentially a mutating set
    of Tiles.
*/
public final class CollectionOfTiles
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
    
    public CollectionOfTiles()
    {
        tiles = new TreeSet<RenderableTile>(new LowestZoomFirstComparator());
    }
    
    /**
        Given an AffineTransformation attempts to figure out the scale it applies.

        Assumes that the transform does not contain any shear.
    */
    private static double recoverScale(AffineTransform transform)
    {
        int scaleType = transform.getType() & AffineTransform.TYPE_MASK_SCALE;
        if (scaleType == 0 || scaleType == AffineTransform.TYPE_UNIFORM_SCALE) {
            return Math.sqrt(transform.getDeterminant());
        } else {
            throw new IllegalArgumentException("AffineTransform is not a \"uniform scale\" or \"identity\" transform: " + transform.getType());
        }
    }
    
    private static Rectangle calculateZoomCorrectedBounds(Rectangle clipBounds, int bestScaleIndex)
    {
        double xmin = clipBounds.x;
        double ymin = clipBounds.y;
        double xmax = clipBounds.x + clipBounds.width;
        double ymax = clipBounds.y + clipBounds.height;
        xmin *= Math.pow(TilePosition.SCALE_POWER, bestScaleIndex);
        ymin *= Math.pow(TilePosition.SCALE_POWER, bestScaleIndex);
        xmax *= Math.pow(TilePosition.SCALE_POWER, bestScaleIndex);
        ymax *= Math.pow(TilePosition.SCALE_POWER, bestScaleIndex);
        return new Rectangle(
                (int)Math.floor(xmin),
                (int)Math.floor(ymin),
                (int)(Math.ceil(xmax) - Math.floor(xmin)),
                (int)(Math.ceil(ymax) - Math.floor(ymin)));
    }

    
    /**
        Uses currently available tiles to blit over the Graphics2D object's clip bounds.
        Returns the list of tiles that if rendered would have made this blit look nicer.
    */
    public List<TilePosition> blitImmediately(Graphics2D g)
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
//        System.out.println(this.getClass().getName() + ".blitImmediately() took: " + time + "ms");
        
        AffineTransform forwardTransform = g.getTransform();
        
        double magnification = recoverScale(forwardTransform);
        int bestScaleIndex = (int)Math.ceil(Math.log(magnification) / Math.log(TilePosition.SCALE_POWER));
        
//        System.out.println("bestScaleIndex: " + bestScaleIndex);
        
        Rectangle bounds = calculateZoomCorrectedBounds(g.getClipBounds(), bestScaleIndex);
//        System.out.println(bounds);
        
        List<TilePosition> remainingTiles = new ArrayList<TilePosition>();
        int minX = bounds.x - ((bounds.x % TilePosition.SIZE) + TilePosition.SIZE) % TilePosition.SIZE;
        int minY = bounds.y - ((bounds.y % TilePosition.SIZE) + TilePosition.SIZE) % TilePosition.SIZE;
        for (int y = minY; y < bounds.y + bounds.height; y += TilePosition.SIZE) {
            for (int x = minX; x < bounds.x + bounds.width; x += TilePosition.SIZE) {
                TilePosition pos = new TilePosition(x / TilePosition.SIZE, y / TilePosition.SIZE, bestScaleIndex);
                remainingTiles.add(pos);
            }
        } 
        return remainingTiles;
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
