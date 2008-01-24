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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
    
    private enum State {
        HAVE_NEW_TILES,
        NEED_HIGH_QUALITY_RENDER,
        UP_TO_DATE
    }
    
    private final Object lockThing = new Object();
    private final int maximumCapacity;
    private final List<RenderableTile> tiles = new LinkedList<RenderableTile>();
    
    State state = State.HAVE_NEW_TILES;
    
    public CollectionOfTiles(int maximumCapacity)
    {
        this.maximumCapacity = maximumCapacity;
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
    
    private static Rectangle calculateZoomCorrectedBounds(Rectangle2D clipBounds, int bestScaleIndex)
    {
        double xmin = clipBounds.getMinX();
        double ymin = clipBounds.getMinY();
        double xmax = clipBounds.getMaxX();
        double ymax = clipBounds.getMaxY();
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
    Set<TilePosition> blitImmediately(Graphics2D g)
    {
        // The copy we make must be sorted so that we paint them in the correct order.
        final SortedSet<RenderableTile> tilesCopy = new TreeSet<RenderableTile>(new LowestZoomFirstComparator());
        boolean doHighQualityRender = false;
        synchronized(lockThing) {
            tilesCopy.addAll(tiles);
            
            if (state == State.HAVE_NEW_TILES) {
                // Do a quick render as we are still getting new tiles, and mark ourselves as requiring a better blit.
                state = State.NEED_HIGH_QUALITY_RENDER;
            } else if (state == State.NEED_HIGH_QUALITY_RENDER) {
                // No new tiles arrived recently, so lets do a high quality render since the last blit was low quality.
                doHighQualityRender = true;
                state = State.UP_TO_DATE;
            } else if (state == State.UP_TO_DATE) {
                // No new tiles, and our last blit was high quality.  Most likely a user drag then, so do a quick render and
                // mark ourselves as requiring a better one.
                state = State.NEED_HIGH_QUALITY_RENDER;
            } else {
                throw new IllegalArgumentException("Invalid state");
            }
        }

        if (doHighQualityRender) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }
        for (RenderableTile t: tilesCopy) {
            t.render(g);
        }
//        System.out.println(this.getClass().getName() + ".blitImmediately() took: " + time + "ms");
        
        AffineTransform forwardTransform = g.getTransform();
        
        double magnification = recoverScale(forwardTransform);
        int bestScaleIndex = (int)Math.ceil(Math.log(magnification) / Math.log(TilePosition.SCALE_POWER));
        
//        System.out.println("bestScaleIndex: " + bestScaleIndex);
        
        Rectangle bounds = calculateZoomCorrectedBounds(g.getClip().getBounds2D(), bestScaleIndex);
//        System.out.println(bounds);
        
        Set<TilePosition> remainingVisibleTiles = new HashSet<TilePosition>();
        int minX = bounds.x - ((bounds.x % TilePosition.SIZE) + TilePosition.SIZE) % TilePosition.SIZE;
        int minY = bounds.y - ((bounds.y % TilePosition.SIZE) + TilePosition.SIZE) % TilePosition.SIZE;
        //System.out.println(bounds);
        for (int y = minY; y < bounds.y + bounds.height; y += TilePosition.SIZE) {
            for (int x = minX; x < bounds.x + bounds.width; x += TilePosition.SIZE) {
                TilePosition pos = new TilePosition(x / TilePosition.SIZE, y / TilePosition.SIZE, bestScaleIndex);
                remainingVisibleTiles.add(pos);
            }
        }
        //System.out.println("Visible tiles: " + remainingVisibleTiles.size());
        synchronized(lockThing) {
            // Iterate over all tiles in our collection, if any of them are currently visible then
            // move them to the end of the purge queue.
            for (int i = 0; i < tiles.size(); i++) {
                RenderableTile tile = tiles.get(i);
                boolean isVisible = remainingVisibleTiles.remove(tile.getPosition());
                if (isVisible) {
                    tiles.remove(i);
                    tiles.add(tile);
                    i--;
                }
            }
        }
        return remainingVisibleTiles;
    }
    
    public boolean wouldLookBetterWithAnotherBlit()
    {
        synchronized(lockThing) {
            return state != State.UP_TO_DATE;
        }
    }
    
    /**
        Adds a tile to the canvas.  If a different tile had to be purged to make
        space for this one, the position of the purged tile is returned (so it can
        presumably be made eligable for re-rendering if needed.)
    */
    TilePosition addTile(RenderableTile t)
    {
        Tile purgedTile = null;
        synchronized(lockThing) {
            tiles.add(t);
            if (tiles.size() > maximumCapacity) {
                purgedTile = tiles.remove(0);
            }
            state = State.HAVE_NEW_TILES;
        }
        return (purgedTile != null) ? (purgedTile.getPosition()) : null;
    }
    
    int getMaximumCapacity()
    {
        return maximumCapacity;
    }
}
