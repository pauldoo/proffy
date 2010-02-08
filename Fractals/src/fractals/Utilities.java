/*
    Copyright (C) 2008, 2009, 2010  Paul Richards.

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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public final class Utilities
{
    private static final ScheduledExecutorService lightThreadPool =
            new ScheduledThreadPoolExecutor(2, new MyThreadFactory(Thread.NORM_PRIORITY));
    private static final ScheduledExecutorService heavyThreadPool =
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), new MyThreadFactory((Thread.NORM_PRIORITY + Thread.MIN_PRIORITY) / 2));
    private static final ScheduledExecutorService backgroundThreadPool =
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), new MyThreadFactory(Thread.MIN_PRIORITY));

    private static final class MyThreadFactory implements ThreadFactory
    {
        final int priority;

        MyThreadFactory(int priority)
        {
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread result = new Thread(r);
            result.setPriority(priority);
            result.setDaemon(true);
            return result;
        }
    }

    private Utilities()
    {
    }
    
    static void setGraphicsToHighQuality(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    static void setGraphicsToLowQuality(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    }
    
    /**
        Simplistic exposure function that returns 1.0 - e^(-exposure * value).
    */
    static double expose(double value, double exposure)
    {
        return 1.0 - Math.exp(-exposure * value);
    }
    
    /**
        A thread pool intended for light background tasks (e.g. to triggering repaints).
    */
    static ScheduledExecutorService getLightThreadPool()
    {
        return lightThreadPool;
    }
    
    /**
        A thread pool intended for heavy tasks that benefit image quality
        in the short term (e.g. rendering, projecting).
    */
    static ScheduledExecutorService getHeavyThreadPool()
    {
        return heavyThreadPool;
    }

    /**
        A thread pool intended for heavy tasks that benefit image quality
        in the long term (e.g. further sampling expected to take many seconds).
    */
    static ScheduledExecutorService getBackgroundThreadPool()
    {
        return backgroundThreadPool;
    }
    
    /**
        Clamps the value 'x' to be in the range [min, max].
    */
    static int clamp(final int min, final int x, final int max)
    {
        if (max < min) {
            throw new IllegalArgumentException("Max is less than min");
        }
        return Math.max(min, Math.min(max, x));
    }
    
    /**
        Copies an array of doubles, returns the new copy.
    */
    static double[] copyDoubleArray(double[] source)
    {
        final double[] result = new double[source.length];
        System.arraycopy(source, 0, result, 0, source.length);
        return result;
    }

    /**
        Asserts that the value is not NaN, then returns it.
    */
    public static double assertNotNaN(double x) throws NotANumberException
    {
        if (Double.isNaN(x)) {
            throw new NotANumberException();
        }
        return x;
    }
}
