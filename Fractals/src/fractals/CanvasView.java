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
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JComponent;

public class CanvasView extends JComponent implements Runnable
{
    private static final long serialVersionUID = 6622327481400970118L;
    
    private final Canvas canvas;
    private final TileProvider<RenderableTile> source;
    private final Queue<TilePosition> tileQueue;
    
    public CanvasView(int width, int height)
    {
        this.canvas = new Canvas();
        this.source = new RenderFilter(new Mandelbrot(), 0.02);
        this.tileQueue = new LinkedList<TilePosition>();
        for (int y = 0; y < height; y += TilePosition.SIZE) {
            for (int x = 0; x < width; x += TilePosition.SIZE) {
                tileQueue.add(new TilePosition(x / TilePosition.SIZE, y / TilePosition.SIZE, 0));
            }
        }
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
        while(true) {
            TilePosition pos;
            synchronized(tileQueue) {
                pos = tileQueue.poll();
            }
            if (pos == null) {
                return;
            } else {
                RenderableTile t = source.getTile(pos);
                canvas.addTile(t);
            }
        }
    }
    
    public void paint(Graphics g)
    {
        Rectangle bounds = g.getClipBounds();
        g.setColor(Color.PINK);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        canvas.blitImmediately(g);
    }
}
