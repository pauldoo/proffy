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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

/**
    This class renders a rough sketch of a Julia fractal using the
    backwards iterator technique to generate an IFS like view.
*/
final class BackwardsIterationJuliaView extends JComponent
{
    private static final long serialVersionUID = 2075009885338468014L;

    /// Complex coordinate of the top left corner of the view.
    private static final Complex viewMin = new Complex(-2.5, -1.5);
    /// Complex coordinate of the bottom right corner of the view.
    private static final Complex viewMax = new Complex(1.5, 1.5);
    
    /**
        Static picture of a Mandelbrot set, used to illustrate that each point
        on the mandelbrot set corresponds to a different julia set.
    */
    private static final Image backgroundImage = MandelbrotSet.quickRender(viewMin, viewMax, new Dimension(600, 400));
    
    private Complex constant;
    
    public BackwardsIterationJuliaView()
    {
        constant = new Complex(0.4, 0.3);
        
        MouseInputListener listener = new InputHandler();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }
    
    @Override
    public void paint(Graphics g)
    {
        paint((Graphics2D)g);
    }
    
    private void setConstant(Complex c)
    {
        this.constant = c;
        repaint();
    }
        
    public void paint(Graphics2D g)
    {
        final Dimension size = getSize();
        final Complex c = constant.clone();
        final Complex half = new Complex(0.5, 0.0);
        
        Random random = new Random();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(backgroundImage, 0, 0, size.width, size.height, null);
        g.setColor(Color.GREEN);

        for (int j = 0; j < 100; j++) {
            Complex z = new Complex(0.0, 0.0);
            for (int i = 0; i < 100; i++) {
                final Complex savedZ = z.clone();
                z = z.subtract(c).power(half);
                if (random.nextBoolean()) {
                    z = z.negate();
                }

                if (i >= 5) {
                    {
                        double x = (((z.getReal() - viewMin.getReal()) / (viewMax.getReal() - viewMin.getReal())) * size.width);
                        double y = (((z.getImaginary() - viewMin.getImaginary()) / (viewMax.getImaginary() - viewMin.getImaginary())) * size.height);
                        Shape shape = new Ellipse2D.Double(x, y, 2, 2);
                        g.fill(shape);
                        //g.drawRect(x, y, 1, 1);
                    }
                }
            }
        }
    }
    
    public final class InputHandler implements MouseInputListener
    {
        public void mouseClicked(MouseEvent e)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mousePressed(MouseEvent e)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseReleased(MouseEvent e)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseEntered(MouseEvent e)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseExited(MouseEvent e)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseDragged(MouseEvent e)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseMoved(MouseEvent e)
        {
            double x = (e.getX() + 0.5) * (viewMax.getReal() - viewMin.getReal()) / getSize().width + viewMin.getReal();
            double y = (e.getY() + 0.5) * (viewMax.getImaginary() - viewMin.getImaginary()) / getSize().height + viewMin.getImaginary();
            setConstant(new Complex(x, y));
        }
    }
}
