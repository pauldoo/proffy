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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
    A Quadrilateral gui widget that sits without a layout manager and
    can be dragged around by the 4 corners.
*/
final class DraggableQuadrilateral extends JComponent implements MouseListener, MouseMotionListener
{
    private static final long serialVersionUID = 4705086044490724806L;
    
    private Point2D cornerA = new Point2D.Double(100, 100);
    private Point2D cornerB = new Point2D.Double(200, 100);
    private Point2D cornerC = new Point2D.Double(200, 200);
    private Point2D cornerD = new Point2D.Double(100, 200);
    
    private boolean isBeingHoveredOver = false;
    private Point dragStart = null;
    
    DraggableQuadrilateral()
    {
        setOpaque(false);
        setRequestFocusEnabled(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        paintComponent((Graphics2D)g);
    }
    
    public void paintComponent(Graphics2D g)
    {
        Utilities.setGraphicsToHighQuality(g);
        
        g.setColor(isBeingHoveredOver ? Color.RED : Color.BLACK);
        g.setStroke(getNormalStroke());
        g.draw(getShape());
    }
    
    private static Stroke getNormalStroke()
    {
        return new BasicStroke(2.0f);
    }
    
    private static Stroke getSelectingStroke()
    {
        return new BasicStroke(10.0f);
    }
    
    private Shape getShape()
    {
        Polygon result = new Polygon();
        result.addPoint((int)Math.round(cornerA.getX()), (int)Math.round(cornerA.getY()));
        result.addPoint((int)Math.round(cornerB.getX()), (int)Math.round(cornerB.getY()));
        result.addPoint((int)Math.round(cornerC.getX()), (int)Math.round(cornerC.getY()));
        result.addPoint((int)Math.round(cornerD.getX()), (int)Math.round(cornerD.getY()));
        return result;
    }
    
    Shape getSelectingOutlineShape()
    {
        return getSelectingStroke().createStrokedShape(getShape());
    }
    
    private static Point2D displacePoint(Point2D p, double dx, double dy)
    {
        return new Point2D.Double(p.getX() + dx, p.getY() + dy);
    }
    
    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        if (getSelectingOutlineShape().contains(e.getPoint())) {
            dragStart = e.getPoint();
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        dragStart = null;
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
        if (dragStart != null) {
            Point p = e.getPoint();
            double dx = p.x - dragStart.x;
            double dy = p.y - dragStart.y;
            dragStart = p;
            cornerA = displacePoint(cornerA, dx, dy);
            cornerB = displacePoint(cornerB, dx, dy);
            cornerC = displacePoint(cornerC, dx, dy);
            cornerD = displacePoint(cornerD, dx, dy);
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        boolean nowBeingHoveredOver = getSelectingOutlineShape().contains(e.getPoint());
        if (nowBeingHoveredOver != isBeingHoveredOver) {
            isBeingHoveredOver = nowBeingHoveredOver;
            repaint();
        }
        if (!isBeingHoveredOver) {
            /*
                This appears to be a bit of a hack.  We move ourselves to the back
                of the stack and so when the mouse moves by the next pixel a different
                object will get a chance to do hit detection.
                So with N objects on the canvas, each one only gets a chance to check
                for mouse floating every N pixels of mouse movement.
            */
            JLayeredPane canvas = (JLayeredPane)getParent();
            Component c = canvas.getComponentAt(e.getPoint());
            canvas.moveToBack(this);
        }
    }
}
