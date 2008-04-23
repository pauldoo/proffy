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

/**
    Implementation of the PointSet interface using PR Quadtrees.
*/
final class QuadTreePointSet implements PointSet
{
    private final Node rootNode;
    
    public QuadTreePointSet()
    {
        this.rootNode = new EmptyNode(new Point2D.Double(0.0, 0.0), new Point2D.Double(1.0, 1.0));
    }
    
    private QuadTreePointSet(Node rootNode)
    {
        this.rootNode = rootNode;
    }
    
    public PointSet add(Point2D.Double point)
    {
        return new QuadTreePointSet(rootNode.add(point));
    }

    public Point2D.Double findClosest(Point2D.Double point)
    {
        return rootNode.findClosest(point);
    }
    
    /**
        Root class for internal implementation classes. 
    */
    private static abstract class Node
    {
        /**
            Most negative corner of the bounds of this node, inclusive.
        */
        protected final Point2D.Double min;
        
        /**
            Most positive corner of the bounds of this node, exclusive.
        */
        protected final Point2D.Double max;
        
        Node(Point2D.Double min, Point2D.Double max)
        {
            this.min = min;
            this.max = max;
            assert max.x > min.x;
            assert ((max.x + min.x) / 2) != min.x;
            assert ((max.x + min.x) / 2) != max.x;
            assert max.y > min.y;
            assert ((max.y + min.y) / 2) != min.y;
            assert ((max.y + min.y) / 2) != max.y;
        }
        
        /**
            Adds a point to the quadtree, possibly extending
            the quadtree in the process.
        */
        abstract Node add(Point2D.Double point);
        
        /**
            Adds a point to the quadtree, never extending
            the quadtree in the process.
        */
        abstract Node addInside(Point2D.Double point);
        
        abstract Point2D.Double findClosest(Point2D.Double point);

        /**
            Returns true iff the given point is within the bounds of the node.
        */
        protected boolean isPointWithinBounds(Point2D.Double point)
        {
            return
                    point.x >= min.x && point.x < max.x &&
                    point.y >= min.y && point.y < max.y;
        }
        
        /**
            Returns the center point of the node (also the top left corner of the 4th quadrant).
        */
        protected Point2D.Double getCenter()
        {
            return new Point2D.Double((min.x + max.x) / 2, (min.y + max.y) / 2);
        }
    }
    
    /**
        A node in the tree that is empty and contains no points or further
        child nodes.
    */
    private static final class EmptyNode extends Node
    {
        EmptyNode(Point2D.Double min, Point2D.Double max)
        {
            super(min, max);
        }

        @Override
        Node add(Point2D.Double point)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        Node addInside(Point2D.Double point)
        {
            return new LeafNode(min, max, point);
        }

        @Override
        Point2D.Double findClosest(Point2D.Double point)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    /**
        A node in the tree that itself contains no points but has 4 child nodes.
    */
    private static final class InnerNode extends Node
    {
        private final Node subNodeA;
        private final Node subNodeB;
        private final Node subNodeC;
        private final Node subNodeD;

        InnerNode(Point2D.Double min, Point2D.Double max)
        {
            super(min, max);
            
            subNodeA = new EmptyNode(
                    min,
                    getCenter());
            subNodeB = new EmptyNode(
                    new Point2D.Double(getCenter().x, min.y),
                    new Point2D.Double(max.x, getCenter().y));                    
            subNodeC = new EmptyNode(
                    new Point2D.Double(min.x, getCenter().y),
                    new Point2D.Double(getCenter().x, max.y));
            subNodeD = new EmptyNode(
                    getCenter(),
                    max);
        }
        
        private InnerNode(
                Point2D.Double min,
                Point2D.Double max,
                Node subNodeA,
                Node subNodeB,
                Node subNodeC,
                Node subNodeD)
        {
            super(min, max);
            
            this.subNodeA = subNodeA;
            this.subNodeB = subNodeB;
            this.subNodeC = subNodeC;
            this.subNodeD = subNodeD;
        }
        
        @Override
        Node add(Point2D.Double point)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        Node addInside(Point2D.Double point)
        {
            switch (Utilities.quadrant(getCenter(), point)) {
                case A:
                    return new InnerNode(min, max, subNodeA.addInside(point), subNodeB, subNodeC, subNodeD);
                case B:
                    return new InnerNode(min, max, subNodeA, subNodeB.addInside(point), subNodeC, subNodeD);
                case C:
                    return new InnerNode(min, max, subNodeA, subNodeB, subNodeC.addInside(point), subNodeD);
                case D:
                    return new InnerNode(min, max, subNodeA, subNodeB, subNodeC, subNodeD.addInside(point));
                default:
                    throw new RuntimeException();
            }
        }

        @Override
        Point2D.Double findClosest(Point2D.Double point)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    /**
        A node in the tree that contains one point and no other children nodes.
    */
    private static final class LeafNode extends Node
    {
        private final Point2D.Double point;
        
        LeafNode(Point2D.Double min, Point2D.Double max, Point2D.Double point)
        {
            super(min, max);
            this.point = point;
            assert isPointWithinBounds(point);
        }
        
        @Override
        Node add(Point2D.Double point)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        Node addInside(Point2D.Double point)
        {
            return (new InnerNode(min, max)).addInside(this.point).addInside(point);
        }

        @Override
        Point2D.Double findClosest(Point2D.Double point)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    private static enum Quadrant {
        A, B, C, D;
    }
    
    private static final class Utilities
    {
        private Utilities()
        {
        }
        
        static Quadrant quadrant(Point2D.Double origin, Point2D.Double point)
        {
            final int quadrant =
                    ((point.getX() >= origin.getX()) ? 1 : 0) +
                    ((point.getY() >= origin.getY()) ? 2 : 0);
            switch (quadrant) {
                case 0:
                    return Quadrant.A;
                case 1:
                    return Quadrant.B;
                case 2:
                    return Quadrant.C;
                case 3:
                    return Quadrant.D;
                default:
                    throw new RuntimeException();
            }
        }
    }    
}
