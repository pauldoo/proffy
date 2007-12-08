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

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JApplet;
import javax.swing.JOptionPane;

public final class MainApplet extends JApplet
{
    private static final long serialVersionUID = 1011945883208164505L;
    
    public MainApplet()
    {
    }

    public void start()
    {
        super.start();
        try {
            String fractalType = this.getParameter("type");
            //Class fractalClass = Class.forName("fractals." + fractalType);
            
            TileProvider<RenderableTile> source = null;
            if (fractalType.equals("MandelbrotSet")) {
                source = new RenderFilter(new MandelbrotSet(), 0.02);
            } else if (fractalType.equals("JuliaSet")) {
                source = new RenderFilter(new JuliaSet(-0.726895347709114071439, 0.188887129043845954792), 0.01);
            } else {
                throw new IllegalArgumentException("Unknown fractal type: " + fractalType);
            }

            CanvasView view = new CanvasView(800, 600, source);
            this.getContentPane().add(view);
            view.startUpdateThread();
            view.startRenderingThreads();
        } catch (Exception e) {
            StringWriter message = new StringWriter();
            e.printStackTrace(new PrintWriter(message));
            JOptionPane.showInternalMessageDialog(this.getContentPane(), message.toString(), e.toString(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
