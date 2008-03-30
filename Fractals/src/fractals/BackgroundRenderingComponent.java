/*
    Copyright (C) 2008  Paul Richards.

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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
    Abstract base class for components that want to do rendering in the
    background and not in their main paint method.
 
    Useful for components that can't do a decent quality rendering at
    interactive speed.
*/
abstract class BackgroundRenderingComponent extends JComponent
{
    /**
        The current buffer that "paint()" will use.  Is being concurrently
        written to by the background rendering thread.
    */
    private BufferedImage buffer = null;
    
    /**
        The background thread for rendering.
    */
    private Thread backgroundThread = null;
    
    /**
        Copies the buffered image to the screen, and does so quickly.
    */
    @Override
    public final void paintComponent(Graphics g)
    {
        paintComponent((Graphics2D)g);
    }
    
    private final void paintComponent(Graphics2D g)
    {
        final boolean repaintAgainLater = isRendering();
        if (buffer != null) {
            g.drawImage(buffer, 0, 0, null);
        }
        if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
            rerender();
        }
        if (repaintAgainLater) {
            new Thread(new Runnable(){
                public void run()
                {
                    try {
                        Thread.sleep(400);
                        repaint();
                    } catch (InterruptedException ex)
                    {
                    }
                }
            }).start();
        }
    }
    
    /**
        Implementors of this class are expected to render progressively
        higher quality images to the graphics context.  Periodically the image
        which is backed by this 
     
        The implementor is expected to check Thread.interrupted() fairly regularly.
    */
    protected abstract void render(Graphics2D g) throws InterruptedException;
    
    /**
        Restarts the background rendering thread with a new buffer.
    */
    public final void rerender()
    {
        stopBackgroundThread();
        Runnable runner = new Runnable() {
            public void run()
            {
                Graphics2D g = null;
                try {
                    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    if (Thread.interrupted()) throw new InterruptedException();
                    g = (Graphics2D)buffer.getGraphics();
                    render(g);
                } catch (InterruptedException ex) {
                } finally {
                    if (g != null) {
                        g.dispose();
                    }
                    backgroundThread = null;
                }
            }
        };
        if (backgroundThread != null) {
            throw new IllegalStateException("There should be no background thread");
        }
        backgroundThread = new Thread(runner);
        Utilities.setToBackgroundThread(backgroundThread);
        backgroundThread.start();
    }
    
    private final void stopBackgroundThread()
    {
        Thread t = backgroundThread;
        try {
            if (t != null) {
                t.interrupt();
                t.join();
            }
            backgroundThread = null;
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
        Returns true iff there is a background thread still running.
    */
    private final boolean isRendering()
    {
        Thread t = backgroundThread;
        return t != null && t.isAlive();
    }
}
