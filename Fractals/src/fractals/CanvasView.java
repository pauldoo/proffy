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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

public class CanvasView extends JComponent implements Runnable
{
    private static final long serialVersionUID = 6622327481400970118L;
    
    private final CollectionOfTiles canvas;
    private final TileProvider<RenderableTile> source;
    private final BlockingQueue<TilePosition> tileQueue;
    private final Set<TilePosition> visited;
    private final JLabel statusLabel;
    
    private AffineTransform transform = new AffineTransform();
    
    public CanvasView(int width, int height, TileProvider<RenderableTile> source, JLabel statusLabel)
    {
        this.canvas = new CollectionOfTiles();
        this.source = source;
        this.tileQueue = new LinkedBlockingQueue<TilePosition>();
        this.visited = new HashSet<TilePosition>();
        this.statusLabel = statusLabel;
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CanvasViewInputHandler listener = new CanvasViewInputHandler(this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addMouseWheelListener(listener);
        this.addKeyListener(listener);
        
        this.setFocusable(true);
        this.setDoubleBuffered(true);
    }
    
    public void startRenderingThreads()
    {
        int threads = Runtime.getRuntime().availableProcessors() + 1;
        for (int i = 1; i <= threads; i++) {
            Thread t = new Thread(this);
            t.start();
        }
    }
    
    public void startUpdateThread()
    {
        final CanvasView self = this;
        Runnable r = new Runnable() {
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(500);
                        if (canvas.updatedSinceLastBlit()) {
                            self.repaint();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        
        (new Thread(r)).start();
    }
    
    public void run()
    {
        try {
            while(true) {
                TilePosition pos = tileQueue.take();
                //System.out.println(Thread.currentThread() + ": " + pos);
                RenderableTile t = source.getTile(pos);
                canvas.addTile(t);
            }
        } catch (InterruptedException e) {
        }
    }

    public void zoomBy(int scales)
    {
        double scaleFactor = Math.pow(1.2, scales);
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
        long time = -System.currentTimeMillis();
        Rectangle bounds = g.getClipBounds();
        g.setColor(Color.ORANGE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

//        System.out.println("Before:");
//        System.out.println("\t" + g.getTransform());
//        System.out.println("\t" + g.getClipBounds());
        g.transform(transform);
//        System.out.println("After:");
//        System.out.println("\t" + g.getTransform());
//        System.out.println("\t" + g.getClipBounds());
        
        List<TilePosition> remainingTiles = canvas.blitImmediately(g);
        
        for (TilePosition pos: remainingTiles) {
            if (!visited.contains(pos)) {
                visited.add(pos);
                tileQueue.add(pos);
            }
        }
        
        statusLabel.setText("Remaining tiles: " + tileQueue.size());
        
        time += System.currentTimeMillis();
//        System.out.println(this.getClass().getName() + ".paint() took: " + time + "ms");
    }
}
