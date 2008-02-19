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
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
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
    
    private static final Complex viewMin = new Complex(-2.5, -1.5);
    private static final Complex viewMax = new Complex(1.5, 1.5);
    
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
                        int x = (int)Math.round(((z.getReal() - viewMin.getReal()) / (viewMax.getReal() - viewMin.getReal())) * size.width);
                        int y = (int)Math.round(((z.getImaginary() - viewMin.getImaginary()) / (viewMax.getImaginary() - viewMin.getImaginary())) * size.height);
                        g.drawRect(x, y, 1, 1);
                    }
                }
                if (random.nextBoolean()) {
                    z = savedZ;
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
            double x = (e.getX() * 4.0) / getSize().width - 2.0;
            double y = (e.getY() * 4.0) / getSize().height - 2.0;
            setConstant(new Complex(x, y));
        }
    }
}
