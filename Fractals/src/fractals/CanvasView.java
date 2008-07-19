/*
    Copyright (C) 2007, 2008  Paul Richards.

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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;


public class CanvasView extends JComponent
{
    private static final long serialVersionUID = 6622327481400970118L;
    
    private final CollectionOfTiles canvas;
    private final TileProvider<RenderableTile> source;
    
    /**
        Mutex for modifying the renderingTasks map.
    */
    private final Object lockThing = new Object();
    
    /**
        All tiles that have ever been submitted to the thread pool gizmo for
        rendering.  Entries are not removed after the rendering has finished,
        and are instead only removed once the tile has been evicted from the
        cache.
    */
    private final Map<TilePosition, Future> renderingTasks = new HashMap<TilePosition, Future>();
    
    /**
        Periodic repaint task.
    */
    private Future updateTask;
    
    private AffineTransform transform = new AffineTransform();
    
    CanvasView(int width, int height, TileProvider<RenderableTile> source)
    {
        // Configure the canvas with 6 megapixels of cache
        this.canvas = new CollectionOfTiles((6 * 1000000) / (TilePosition.SIZE * TilePosition.SIZE));
        this.source = source;
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CanvasViewInputHandler listener = new CanvasViewInputHandler(this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addMouseWheelListener(listener);
        this.addKeyListener(listener);
        
        this.setFocusable(true);
        this.setDoubleBuffered(true);
    }
    
    private ScheduledFuture startUpdateTask()
    {
        final CanvasView self = this;
        Runnable r = new Runnable() {
            public void run() {
                if (canvas.wouldLookBetterWithAnotherBlit()) {
                    self.repaint();
                }
            }
        };

        return Utilities.getLightThreadPool().scheduleWithFixedDelay(r, 500, 500, TimeUnit.MILLISECONDS);
    }
    
    private final class RenderTileRunner implements Runnable
    {
        private final TilePosition position;
        
        RenderTileRunner(TilePosition position)
        {
            this.position = position;
        }
        
        public void run()
        {
            RenderableTile t = source.getTile(position);
            TilePosition removed = canvas.addTile(t);
            if (removed != null) {
                synchronized(lockThing) {
                    renderingTasks.remove(removed);
                }
            }
        }
    }

    public void zoomBy(int scales)
    {
        double scaleFactor = Math.pow(1.6, scales);
        AffineTransform zoomTransform = new AffineTransform();
        zoomTransform.translate(400, 300);
        zoomTransform.scale(scaleFactor, scaleFactor);
        zoomTransform.translate(-400, -300);
        transform.preConcatenate(zoomTransform);
        repaint();
    }
    
    public void moveBy(int dispX, int dispY)
    {
        AffineTransform translateTransform = new AffineTransform();
        translateTransform.translate(dispX, dispY);
        transform.preConcatenate(translateTransform);
        repaint();
    }
    
    public void rotateBy(double angleInRadians)
    {
        AffineTransform rotateTransform = new AffineTransform();
        rotateTransform.translate(400, 300);
        rotateTransform.rotate(angleInRadians);
        rotateTransform.translate(-400, -300);
        transform.preConcatenate(rotateTransform);
        repaint();
    }
    
    @Override
    public void paint(Graphics g)
    {
        paint((Graphics2D)g);
    }

    public void paint(Graphics2D g)
    {
        Rectangle bounds = g.getClipBounds();
        g.setColor(Color.ORANGE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.transform(transform);
        final Set<TilePosition> neededTiles = canvas.blitImmediately(g);

        synchronized(lockThing) {
            for (Iterator<Map.Entry<TilePosition, Future> > i = renderingTasks.entrySet().iterator(); i.hasNext(); ) {
                final Map.Entry<TilePosition, Future> entry = i.next();
                
                if (neededTiles.contains(entry.getKey())) {
                    // A tile we have already queued for rendering has been requested again,
                    // so don't requeue.
                    neededTiles.remove(entry.getKey());
                } else {
                    if (entry.getValue().isDone() == false) {
                        // A tile we have queued but hasn't yet rendered is no longer required.
                        entry.getValue().cancel(true);
                        i.remove();
                    }
                }
            }
            for (TilePosition pos: neededTiles) {
                renderingTasks.put(pos, Utilities.getHeavyThreadPool().submit(new RenderTileRunner(pos)));
            }
        }
    }
    
    synchronized void stopAllThreads()
    {
        updateTask.cancel(false);
        updateTask = null;
        for (Future f: renderingTasks.values()) {
            f.cancel(true);
        }
        renderingTasks.clear();
    }
    
    synchronized void startAllThreads()
    {
        updateTask = startUpdateTask();
    }
    
    /**
        This component has a number of background threads which it uses for rendering.
        These must be terminated when the component is no longer visible.  The best way I
        can see to do this is to attach a WindowListener to the top level window.  The user of
        this class is expected to call this method and attach the returned window listener to
        the top level window.
    */
    WindowListener createWindowListenerForThreadManagement()
    {
        return new WindowListener(){
            public void windowOpened(WindowEvent e)
            {
                startAllThreads();
            }

            public void windowClosing(WindowEvent e)
            {
            }

            public void windowClosed(WindowEvent e)
            {
                stopAllThreads();
            }

            public void windowIconified(WindowEvent e)
            {
            }

            public void windowDeiconified(WindowEvent e)
            {
            }

            public void windowActivated(WindowEvent e)
            {
            }

            public void windowDeactivated(WindowEvent e)
            {
            }
        };
    }
}
