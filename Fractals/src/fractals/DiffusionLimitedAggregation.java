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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

final class DiffusionLimitedAggregation extends BackgroundRenderingComponent
{
    private static final long serialVersionUID = 3769090052648725711L;
    
    private static final double PARTICLE_RADIUS = 0.25;
    private static final double RELEASE_GAP = PARTICLE_RADIUS * 10;
    private static final double STICKINESS = 1.0;
    
    DiffusionLimitedAggregation()
    {
    }
    
    static JComponent createView()
    {
        return new DiffusionLimitedAggregation();
    }
    
    @Override
    protected void render(Graphics2D g) throws InterruptedException
    {
        Utilities.setGraphicsToHighQuality(g);
        g.setColor(Color.RED);
        PointSet pointSet = new QuadTreePointSet();
        
        final double width = getWidth();
        final double height = getHeight();
        final Point2D.Double center = new Point2D.Double(width/2, height/2);
        pointSet = fixatePoint(center, g, pointSet);
        
        double currentReleaseRadius = RELEASE_GAP;        
        
        while (currentReleaseRadius <= Math.min(width, height) / 2) {
            Point2D.Double p = generateRandomStartPoint(center, currentReleaseRadius);
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                final Point2D.Double closestPoint = pointSet.findClosest(p);
                final double closestPointDistance = closestPoint.distance(p);
                
                if (closestPointDistance <= PARTICLE_RADIUS * 2.0) {
                    if (Math.random() < STICKINESS) {
                        pointSet = fixatePoint(p, g, pointSet);
                        currentReleaseRadius = Math.max(
                                currentReleaseRadius,
                                p.distance(center) + RELEASE_GAP);
                        break;
                    }
                }
                
                final double stepSize = Math.max(PARTICLE_RADIUS * 0.25, closestPointDistance - PARTICLE_RADIUS * 3);
                final double stepAngle = Math.random() * Math.PI * 2.0;
                p.setLocation(
                        p.getX() + Math.cos(stepAngle) * stepSize,
                        p.getY() + Math.sin(stepAngle) * stepSize);
                
                p = clampRadius(center, p, currentReleaseRadius + RELEASE_GAP);
            }
        }
    }
    
    private static PointSet fixatePoint(Point2D.Double point, Graphics2D g, PointSet pointSet)
    {
        g.fill(new Ellipse2D.Double(point.getX() - PARTICLE_RADIUS, point.getY() - PARTICLE_RADIUS, PARTICLE_RADIUS * 2, PARTICLE_RADIUS * 2));
        return pointSet.add(point);
    }
    
    private static Point2D.Double generateRandomStartPoint(Point2D.Double center, double releaseRadius)
    {
        double a = Math.random() * Math.PI * 2;
        return new Point2D.Double(center.getX() + Math.cos(a) * releaseRadius, center.getY() + Math.sin(a) * releaseRadius);
    }
    
    /**
        Clamps a point to be within a certain distance of a given center.
    */
    private static Point2D.Double clampRadius(Point2D.Double center, Point2D.Double point, double maximumRadius)
    {
        if (point.distance(center) > maximumRadius) {
            double dx = point.getX() - center.getX();
            double dy = point.getY() - center.getY();
            double correction = maximumRadius / point.distance(center);
            dx *= correction;
            dy *= correction;
            return new Point2D.Double(center.getX() + dx, center.getY() + dy);
        } else {
            return point;
        }
    }
}
