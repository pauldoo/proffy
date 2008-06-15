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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;

/**
    Abstract base class for components that want to do rendering in the
    background and not in their main paint method.
    <p>
    Useful for components that can't do a decent quality rendering at
    interactive speed or want to incrementally add more to their canvas
    without having to worry about the threading.
*/
abstract class BackgroundRenderingComponent extends JComponent
{
    /**
        The current buffer that "paint()" will use.  Is being concurrently
        written to by the background rendering thread.
    */
    private BufferedImage buffer = null;
    
    /**
        Future object kept in order to cancel the background thread.
    */
    private Future renderer = null;
    
    /**
        Object used as an event for the bufferIsNowOkayToBlit method.
     
        @see #bufferIsNowOkayToBlit()
    */
    private Object bufferFirstBlitEventObject = null;

    /**
        Future object kept in order to cancel the background thread
        that calls repaint in order to cause the buffer to be reblitted.
    */
    private Future repainter = null;
    
    
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
        if (bufferFirstBlitEventObject != null) {
            try {
                synchronized (bufferFirstBlitEventObject) {
                    bufferFirstBlitEventObject.wait(200);
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } finally {
                bufferFirstBlitEventObject = null;
            }
        }
        if (buffer != null) {
            g.drawImage(buffer, 0, 0, null);
        }
        if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
            rerender();
        }
        if (repaintAgainLater) {
            if (repainter != null) {
                repainter.cancel(false);
                repainter = null;
            }
            Runnable runnable = new Runnable(){
                public void run()
                {
                    repaint();
                }
            };
            repainter = Utilities.getThreadPool().schedule(runnable, 200, TimeUnit.MILLISECONDS);
        }
    }
        
    /**
        Implementors of this class are expected to render progressively
        higher quality images to the graphics context.  Periodically the image
        which backs the Graphics2D is painted to the screen to make the
        incremental rendering visible to the user.
        <p>
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
                    if (getWidth() == 0 || getHeight() == 0) {
                        return;
                    }
                    bufferFirstBlitEventObject = new Object();
                    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    if (Thread.interrupted()) throw new InterruptedException();
                    g = (Graphics2D)buffer.getGraphics();
                    render(g);
                    bufferIsNowOkayToBlit();
                    repaint();
                } catch (InterruptedException ex) {
                } finally {
                    if (g != null) {
                        g.dispose();
                    }
                }
            }
        };
        if (renderer != null) {
            throw new IllegalStateException("There should be no background thread");
        }
        renderer = Utilities.getThreadPool().submit(runner);
        repaint();
    }
    
    public final void stopBackgroundThread()
    {
        if (renderer != null) {
            renderer.cancel(true);
            renderer = null;
        }
    }
    
    /**
        Returns true iff there is a background thread still running.
    */
    private final boolean isRendering()
    {
        return renderer != null && renderer.isDone() == false;
    }
    
    /**
        To avoid certain types of flicker we don't blit the buffer within
        "paintComponent()" until after the background thread has
        signalled that it is now happy for the contents to be made visible.
        <p>
        Usually the background rendering thread will allow this as soon
        as it has filled the canvas with the background colour and rendered
        anything that is quick to do.
        <p>
        Called by the derrived class.
        <p>
        This is also just a hint, after a fixed time period this class will blit the
        buffer regardless of whether the derrived class is ready for it or not.
    */
    protected final void bufferIsNowOkayToBlit()
    {
        if (bufferFirstBlitEventObject != null) {
            synchronized (bufferFirstBlitEventObject) {
                bufferFirstBlitEventObject.notifyAll();
            }
        }
    }
}
