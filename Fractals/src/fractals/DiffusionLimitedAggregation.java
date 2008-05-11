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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

final class DiffusionLimitedAggregation
{
    private static final double PARTICLE_RADIUS = 0.75;
    private static final double STICKINESS = 1.0;
    
    private static final Color COLOR_EXISTING = new Color(0x606096);
    private static final Color COLOR_ADDED = new Color(0x480266);
    private static final Color COLOR_REMOVED = new Color(0x280266);
    
    private PointSet pointSet;
    private final double width;
    private final double height;
    
    private Point2D.Double currentReleasePoint = null;
    private boolean releaseAntiParticles = false;
    
    static JComponent createView()
    {
        return new DiffusionLimitedAggregationComponent();
    }
    
    DiffusionLimitedAggregation(double width, double height)
    {
        this.pointSet = new QuadTreePointSet();
        this.width = width;
        this.height = height;
        Point2D.Double center = new Point2D.Double(width/2, height/2);
        fixatePoint(center, null);
    }

    void renderExisting(Graphics2D graphics) throws InterruptedException
    {
        for (Point2D.Double p: pointSet) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            renderPoint(p, COLOR_EXISTING, graphics);
        }
    }
    
    void renderMore(Graphics2D graphics) throws InterruptedException
    {
        while (true) {
            if (releaseAntiParticles && pointSet.size() <= 1) {
                break;
            }
            
            //System.out.print(".");
            Point2D.Double p = currentReleasePoint;
            if (p == null) {
                break;
            }
            p = (Point2D.Double)p.clone();
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                final Point2D.Double closestPoint = pointSet.findClosest(p);
                final double closestPointDistance = closestPoint.distance(p);
                
                if (closestPointDistance <= PARTICLE_RADIUS * 2.0) {
                    if (Math.random() < STICKINESS) {
                        if (releaseAntiParticles) {
                            annihilatePoint(closestPoint, graphics);
                            break;
                        } else {
                            fixatePoint(p, graphics);
                            break;
                        }
                    }
                }
                
                final double stepSize = Math.max(PARTICLE_RADIUS * 0.25, closestPointDistance - PARTICLE_RADIUS * 3);
                final double stepAngle = Math.random() * Math.PI * 2.0;
                p.setLocation(
                        p.getX() + Math.cos(stepAngle) * stepSize,
                        p.getY() + Math.sin(stepAngle) * stepSize);
                
                p = clampPosition(p);
            }
        }
    }
    
    synchronized void setCurrentReleasePoint(Point2D.Double point, boolean releaseAntiParticles)
    {
        this.currentReleasePoint = (point == null) ? null : (Point2D.Double)point.clone();
        this.releaseAntiParticles = releaseAntiParticles;
    }
    
    private void annihilatePoint(Point2D.Double point, Graphics2D graphics)
    {
        if (graphics != null) {
            renderPoint(point, COLOR_REMOVED, graphics);
        }
        pointSet = pointSet.remove(point);
    }

    private void fixatePoint(Point2D.Double point, Graphics2D graphics)
    {
        Point2D.Double closestExisting = pointSet.findClosest(point);
        if (closestExisting == null || closestExisting.distance(point) > 1e-6) {
            if (graphics != null) {
                renderPoint(point, COLOR_ADDED, graphics);
            }
            pointSet = pointSet.add(point);
        }
    }
    
    private static void renderPoint(Point2D.Double point, Color color, Graphics2D graphics)
    {
        graphics.setColor(color);
        graphics.fill(new Ellipse2D.Double(point.getX() - PARTICLE_RADIUS, point.getY() - PARTICLE_RADIUS, PARTICLE_RADIUS * 2, PARTICLE_RADIUS * 2));
    }
    
    /**
        Clamps a point to be within a certain distance of a given center.
    */
    private Point2D.Double clampPosition(Point2D.Double point)
    {
        if (point.x >= 0 && point.x < width && point.y >= 0 && point.y < height) {
            return point;
        } else {
            return new Point2D.Double(
                    Math.max(0, Math.min(point.x, width-1)),
                    Math.max(0, Math.min(point.y, height-1)));
        }
    }
}

final class DiffusionLimitedAggregationComponent extends BackgroundRenderingComponent
{
    private static final long serialVersionUID = 3769090052648725711L;
    
    private DiffusionLimitedAggregation dla = null;

    DiffusionLimitedAggregationComponent()
    {
        addMouseListener(new InputHandler());
        addMouseMotionListener(new InputHandler());
    }
    
    @Override
    protected void render(Graphics2D g) throws InterruptedException
    {
        Utilities.setGraphicsToHighQuality(g);
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        
        if (dla == null) {
            dla = new DiffusionLimitedAggregation(getWidth(), getHeight());
        }
        dla.renderExisting(g);
        super.bufferIsNowOkayToBlit();
        dla.renderMore(g);
    }
    
    private void setReleaseLocation(Point2D.Double p, boolean releaseAntiParticles)
    {
        final DiffusionLimitedAggregation localDla = this.dla;
        if (localDla != null) {
            localDla.setCurrentReleasePoint(p, releaseAntiParticles);
        }
        rerender();
    }
    
    final class InputHandler implements MouseInputListener
    {
        public void mouseClicked(MouseEvent e)
        {
        }

        public void mousePressed(MouseEvent e)
        {
            boolean anti = (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0;
            if (e.getButton() == MouseEvent.BUTTON1) {
                setReleaseLocation(new Point2D.Double(e.getPoint().getX(), e.getPoint().getY()), anti);
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            setReleaseLocation(null, false);
        }

        public void mouseEntered(MouseEvent e)
        {
        }

        public void mouseExited(MouseEvent e)
        {
        }

        public void mouseDragged(MouseEvent e)
        {
            boolean anti = (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0;
            if (e.getButton() == MouseEvent.BUTTON1) {
                setReleaseLocation(new Point2D.Double(e.getPoint().getX(), e.getPoint().getY()), anti);
            }
        }

        public void mouseMoved(MouseEvent e)
        {
        }
    }
}
