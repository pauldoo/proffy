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
import junit.framework.TestCase;

public final class QuadTreePointSetTest extends TestCase
{
    public void testCreation()
    {
        PointSet p = new QuadTreePointSet();
    }
    
    public void testSimpleAddition()
    {
        {
            PointSet p = new QuadTreePointSet().add(new Point2D.Double(0.0, 0.0));
            assertNotNull(p);
            assertEquals(new Point2D.Double(0.0, 0.0), p.findClosest(new Point2D.Double(0.0, 0.0)));
            assertEquals(new Point2D.Double(0.0, 0.0), p.findClosest(new Point2D.Double(1.0, 0.0)));
        }

        {
            PointSet p = new QuadTreePointSet().add(new Point2D.Double(1.0, 0.0));
            assertNotNull(p);
            assertEquals(new Point2D.Double(1.0, 0.0), p.findClosest(new Point2D.Double(0.0, 0.0)));
            assertEquals(new Point2D.Double(1.0, 0.0), p.findClosest(new Point2D.Double(1.0, 0.0)));
        }
    }
}
