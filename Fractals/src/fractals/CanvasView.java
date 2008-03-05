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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class CanvasView extends JComponent implements Runnable
{
    private static final long serialVersionUID = 6622327481400970118L;
    
    private final CollectionOfTiles canvas;
    private final TileProvider<RenderableTile> source;
    private final Object lockThing = new Object();
    private final Collection<Thread> threads = new ArrayList<Thread>();
    
    /**
        Queue of tiles to render next.  Should not contain any tiles that are in the notToBeRenderedAgain set.
    */
    private final Queue<TilePosition> tileQueue = new LinkedList<TilePosition>();

    /**
        All tile positions that are either in the process of being rendered, or have already been rendered.
        Does not include those tile positions that may be waiting in the queue.
    */
    private final Set<TilePosition> notToBeRenderedAgain = new HashSet<TilePosition>();
    
    private final JLabel statusLabel;
    
    private AffineTransform transform = new AffineTransform();
    
    CanvasView(int width, int height, TileProvider<RenderableTile> source, JLabel statusLabel)
    {
        // Configure the canvas with 6 megapixels of cache
        this.canvas = new CollectionOfTiles((6 * 1000000) / (TilePosition.SIZE * TilePosition.SIZE));
        this.source = source;
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
    
    private Collection<Thread> startRenderingThreads()
    {
        Collection<Thread> result = new ArrayList<Thread>();
        int threadCount = Runtime.getRuntime().availableProcessors();
        for (int i = 1; i <= threadCount; i++) {
            Thread t = new Thread(this);
            int currentPriority = t.getPriority();
            t.setPriority(currentPriority - 1);
            //System.out.println("Before: " + currentPriority + " ... After: " + t.getPriority());
            t.start();
            result.add(t);
        }
        return result;
        //System.out.println("Main thread: " + Thread.currentThread().getPriority());
    }
    
    private Thread startUpdateThread()
    {
        final CanvasView self = this;
        Runnable r = new Runnable() {
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(500);
                        if (canvas.wouldLookBetterWithAnotherBlit()) {
                            self.repaint();
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().toString() + ": Terminating normally.");
                } catch (Exception e) {
                    StringWriter message = new StringWriter();
                    e.printStackTrace(new PrintWriter(message));
                    JOptionPane.showInternalMessageDialog(self, message.toString(), e.toString(), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        Thread result = new Thread(r);
        result.start();
        return result;
    }
    
    public void run()
    {
        try {
            while(true) {
                TilePosition pos = null;
                synchronized(lockThing) {
                    while (pos == null) {
                        try {
                            pos = tileQueue.remove();
                        } catch (NoSuchElementException e) {
                            lockThing.wait();
                        }
                    }
                    notToBeRenderedAgain.add(pos);
                }
                RenderableTile t = source.getTile(pos);
                TilePosition removed = canvas.addTile(t);
                if (removed != null) {
                    synchronized(lockThing) {
                        notToBeRenderedAgain.remove(removed);
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().toString() + ": Terminating normally.");
        } catch (Exception e) {
            StringWriter message = new StringWriter();
            e.printStackTrace(new PrintWriter(message));
            JOptionPane.showInternalMessageDialog(this, message.toString(), e.toString(), JOptionPane.ERROR_MESSAGE);
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
        //System.out.println("Paint thread: " + Thread.currentThread().getPriority());
        //long time = -System.currentTimeMillis();
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
        
        Collection<TilePosition> remainingTiles = canvas.blitImmediately(g);
        synchronized(lockThing) {
            remainingTiles.removeAll(notToBeRenderedAgain);
            tileQueue.clear();
            tileQueue.addAll(remainingTiles);
            if (!tileQueue.isEmpty()) {
                lockThing.notifyAll();
            }
        }
        
        statusLabel.setText("Remaining tiles: " + tileQueue.size() + " / " + canvas.getMaximumCapacity());
        
        //time += System.currentTimeMillis();
//        System.out.println(this.getClass().getName() + ".paint() took: " + time + "ms");
    }
    
    synchronized void stopAllThreads()
    {
        for (Thread t: threads) {
            t.interrupt();
        }
        threads.clear();
    }
    
    synchronized void startAllThreads()
    {
        if (threads.isEmpty() == false) {
            stopAllThreads();
        }
        threads.addAll(startRenderingThreads());
        threads.add(startUpdateThread());
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
