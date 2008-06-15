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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

final class Utilities
{
    private static final ScheduledExecutorService threadPool;
    static {
        final int threadCount = 10;

        final ThreadFactory threadFactory = new ThreadFactory(){
            public Thread newThread(Runnable r) {
                Thread result = new Thread(r);
                setToBackgroundThread(result);
                return result;
            }
        };

        threadPool = new ScheduledThreadPoolExecutor(threadCount, threadFactory);
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
        Lowers the priority of the thread and sets it to be a daemon thread.
    */
    static void setToBackgroundThread(Thread t)
    {
        int currentPriority = t.getPriority();
        t.setPriority(currentPriority - 1);
        t.setDaemon(true);
    }
    
    /**
        Simplistic exposure function that returns 1.0 - e^(-exposure * value).
    */
    static double expose(double value, double exposure)
    {
        return 1.0 - Math.exp(-exposure * value);
    }
    
    static ScheduledExecutorService getThreadPool()
    {
        return threadPool;
    }
}
