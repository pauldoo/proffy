/*
    Copyright (C) 2007, 2008, 2010  Paul Richards.

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

import fractals.math.Complex;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
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
    
    private Complex constant;
    
    public static JComponent createView()
    {
        JPanel result = new JPanel();
        result.setLayout(new BorderLayout());
        JLayeredPane layeredPane = new JLayeredPane();
        
        layeredPane.setLayout(new SpecializedLayoutManager());
        
        Image backgroundImage = MandelbrotSet.quickMandelbrotRender(viewMin, viewMax, new Dimension(600, 400));

        BackwardsIterationJuliaView backwardsIterationJuliaView = new BackwardsIterationJuliaView();
        layeredPane.add(new StrechyImage(backgroundImage), new Integer(0));
        layeredPane.add(backwardsIterationJuliaView, new Integer(1));
        layeredPane.add(new DraggableSpot(backwardsIterationJuliaView), new Integer(2));
        result.add(layeredPane, BorderLayout.CENTER);
        
        JButton button = new JButton("View \"proper\" rendering");
        button.addActionListener(new ButtonListener(backwardsIterationJuliaView));
        result.add(button, BorderLayout.SOUTH);
        
        return result;
    }
    
    private BackwardsIterationJuliaView()
    {
        constant = new Complex(0.4, 0.3);
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
    
    public Complex getConstant()
    {
        return this.constant.clone();
    }
        
    public void paint(Graphics2D g)
    {
        final Dimension size = getSize();
        final Complex c = constant.clone();
        final Complex half = new Complex(0.5, 0.0);
        
        Random random = new Random();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0.0f, 1.0f, 0.0f, 0.5f));

        for (int j = 0; j < 100; j++) {
            Complex z = new Complex(random.nextGaussian(), random.nextGaussian());
            for (int i = 0; i < 100; i++) {
                final Complex previousZ = z.clone();
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
                
                if (previousZ.subtract(z).magnitudeSquared() < 0.001) {
                    break;
                }
            }
        }
    }
    
    public void updateSpotPosition(Point2D p)
    {
        double x = p.getX() * (viewMax.getReal() - viewMin.getReal()) / getSize().width + viewMin.getReal();
        double y = p.getY() * (viewMax.getImaginary() - viewMin.getImaginary()) / getSize().height + viewMin.getImaginary();
        setConstant(new Complex(x, y));
    }
}

final class SpecializedLayoutManager implements LayoutManager
{
    public void addLayoutComponent(String name, Component comp)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeLayoutComponent(Component comp)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Dimension preferredLayoutSize(Container parent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Dimension minimumLayoutSize(Container parent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void layoutContainer(Container parent)
    {
        for (Component c: parent.getComponents()) {
            if (c instanceof StrechyImage) {
                c.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            } else if (c instanceof BackwardsIterationJuliaView) {
                c.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            } else if (c instanceof DraggableSpot) {
                c.setBounds(parent.getWidth() / 2 - 7, parent.getHeight() / 2 - 7, 14, 14);
            } else {
                throw new IllegalArgumentException("Not expecting to layout: " + c.toString());
            }
        }
    }
}


final class DraggableSpot extends JComponent implements MouseInputListener, ComponentListener
{
    private static final long serialVersionUID = 392823523233732646L;
    
    private final BackwardsIterationJuliaView backwardsIterationJuliaView;
    private Point previousPoint = null;
    
    public DraggableSpot(BackwardsIterationJuliaView backwardsIterationJuliaView)
    {
        this.backwardsIterationJuliaView = backwardsIterationJuliaView;
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
    }
    
    @Override
    public void paint(Graphics g)
    {
        paint((Graphics2D)g);
    }
    
    private void paint(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.ORANGE);
        g.fill(new Ellipse2D.Double(0, 0, w, h));
    }
    
    private void updateBounds()
    {
        Rectangle r = getBounds();
        backwardsIterationJuliaView.updateSpotPosition(new Point2D.Double(r.x + r.width * 0.5, r.y + r.height * 0.5));
    }

    public void mouseDragged(MouseEvent e)
    {
        //System.out.println(e);
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            updateDrag(e.getPoint());
        }
    }

    public void mouseMoved(MouseEvent e)
    {
    }
    
    private void updateDrag(Point currentPoint)
    {
        int dispX = currentPoint.x - previousPoint.x;
        int dispY = currentPoint.y - previousPoint.y;
       
        Rectangle bounds = getBounds();
        bounds.translate(dispX, dispY);
        setBounds(bounds);
        //previousPoint = currentPoint;
    }
    
    public void mouseClicked(MouseEvent e)
    {
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
            updateDrag(e.getPoint());
            previousPoint = null;
        }
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void componentResized(ComponentEvent e)
    {
        updateBounds();
    }

    public void componentMoved(ComponentEvent e)
    {
        updateBounds();
    }

    public void componentShown(ComponentEvent e)
    {
    }

    public void componentHidden(ComponentEvent e)
    {
    }
}

final class StrechyImage extends JComponent
{
    private static final long serialVersionUID = 1264801009066714104L;

    private final Image image;

    public StrechyImage(Image image)
    {
        this.image = image;
    }
    
    @Override
    public void paint(Graphics g)
    {
        paint((Graphics2D)g);
    }
    
    private void paint(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        g.drawImage(image, 0, 0, w, h, null);
    }
}

final class ButtonListener implements ActionListener
{
    final BackwardsIterationJuliaView backwardsIterationJuliaView;
    
    ButtonListener(BackwardsIterationJuliaView view)
    {
        this.backwardsIterationJuliaView = view;
    }

    public void actionPerformed(ActionEvent e)
    {
        Complex c = backwardsIterationJuliaView.getConstant();
        
        TileProvider<RenderableTile> source = null;
        source = new RenderFilter(MandelbrotSet.createJuliaSet(1000, c), 0.02);
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JLabel statusLabel = new JLabel();
        statusPanel.add(statusLabel);

        CanvasView view = new CanvasView(800, 600, source);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.addWindowListener(view.createWindowListenerForThreadManagement());
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}