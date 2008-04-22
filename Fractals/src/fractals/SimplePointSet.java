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

import java.awt.geom.Point2D;
import java.util.HashSet;

/**
    Ideally this class would be implemented using a PR Quadtree but for now
    it's a simple Hash.
*/
final class SimplePointSet implements PointSet
{
    private final HashSet<Point2D.Double> set;
    
    SimplePointSet()
    {
        this.set = new HashSet<Point2D.Double>();
    }
    
    public PointSet add(Point2D.Double point)
    {
        set.add((Point2D.Double)point.clone());
        return this;
    }
    
    public Point2D.Double findClosest(Point2D.Double point)
    {
        Point2D.Double result = null;
        for (Point2D.Double p: set) {
            if (result == null || point.distanceSq(p) < point.distanceSq(result)) {
                result = p;
            }
        }
        return (Point2D.Double)result.clone();
    }
}
