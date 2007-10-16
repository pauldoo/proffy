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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class CanvasView extends JComponent implements Runnable
{
    private static final long serialVersionUID = 6622327481400970118L;
    
    private final Canvas canvas;
    private final TileProvider<RenderableTile> source;
    private final BlockingQueue<TilePosition> tileQueue;
    private final Set<TilePosition> visited;
    private int offsetX = 0;
    private int offsetY = 0;
    
    private final class Listener implements MouseInputListener
    {
        private Point previousPoint;
        
        public void mouseClicked(MouseEvent e)
        {
            //System.out.println(e);
        }

        public void mouseDragged(MouseEvent e)
        {
            //System.out.println(e);
            if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                updateMove(e.getPoint());
            }
        }

        public void mouseEntered(MouseEvent e)
        {
            //System.out.println(e);
        }

        public void mouseExited(MouseEvent e)
        {
            //System.out.println(e);
        }

        public void mouseMoved(MouseEvent e)
        {
            //System.out.println(e);
        }

        public void mousePressed(MouseEvent e)
        {
            //System.out.println(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                previousPoint = e.getPoint();
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            //System.out.println(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                updateMove(e.getPoint());
                previousPoint = null;
            }
        }
        
        private void updateMove(Point currentPoint)
        {
            int dispX = currentPoint.x - previousPoint.x;
            int dispY = currentPoint.y - previousPoint.y;
            moveBy(dispX, dispY);
            previousPoint = currentPoint;
        }
    }
    
    public CanvasView(int width, int height)
    {
        this.canvas = new Canvas();
        this.source = new RenderFilter(new Mandelbrot(), 0.02);
        this.tileQueue = new LinkedBlockingQueue<TilePosition>();
        this.visited = new HashSet<TilePosition>();
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Listener listener = new Listener();
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        //this.setDoubleBuffered(true);
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

    private void moveBy(int dispX, int dispY)
    {
        offsetX += dispX;
        offsetY += dispY;
        repaint();
        //invalidate();
    }
    
    public void paint(Graphics g)
    {
        long time = -System.currentTimeMillis();
        g.translate(offsetX, offsetY);
        Rectangle bounds = g.getClipBounds();
        g.setColor(Color.PINK);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(-1000, -1000, 1000, 1000);
        g.drawLine(-1000, 1000, 1000, -1000);
        canvas.blitImmediately(g);

        List<TilePosition> remainingTiles = new ArrayList<TilePosition>();
        int minX = bounds.x - ((bounds.x % TilePosition.SIZE) + TilePosition.SIZE) % TilePosition.SIZE;
        int minY = bounds.y - ((bounds.y % TilePosition.SIZE) + TilePosition.SIZE) % TilePosition.SIZE;
        for (int y = minY; y < bounds.y + bounds.height; y += TilePosition.SIZE) {
            for (int x = minX; x < bounds.x + bounds.width; x += TilePosition.SIZE) {
                TilePosition pos = new TilePosition(x / TilePosition.SIZE, y / TilePosition.SIZE, 0);
                if (!visited.contains(pos)) {
                    remainingTiles.add(pos);
                    visited.add(pos);
                }
            }
        }
        if (!remainingTiles.isEmpty()) {
            Collections.shuffle(remainingTiles);
            tileQueue.addAll(remainingTiles);
        }
        time += System.currentTimeMillis();
        System.out.println(this.getClass().getName() + ".paint() took: " + time + "ms");
    }
}
