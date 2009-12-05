/*
    Copyright (C) 2009  Paul Richards.

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

/**
    Immutable binary segmentation in 3D.
*/
public abstract class OctTree
{
    public static OctTree createEmpty()
    {
        return new LeafNode(
                -1.0, -1.0, -1.0,
                1.0, 1.0, 1.0,
                false);
    }

    final static class HitInfo
    {
        public int fDepth;
    }

    /**
        For the parametric line (x, y, z) + t * (dx, dy, dz), compute
        the lowest value for t in the range [0, inf) that represents
        the line hitting the boundary of the set.

        Returns NaN if there is no hit.
    */
    public abstract double firstHit(
            final double x,
            final double y,
            final double z,
            final double dx,
            final double dy,
            final double dz);

    /**
        Sets the volume from [(minX, minY, minZ), (maxX, maxY, maxZ)]
        to be entirely filled or entirely unfilled.
    */
    public abstract OctTree repSetRegion(
                final double minX,
                final double minY,
                final double minZ,
                final double maxX,
                final double maxY,
                final double maxZ,
                final boolean fill);

    public abstract int nodeCount();

    protected final double firstHitWithBoundingBox(
            final double x,
            final double y,
            final double z,
            final double dx,
            final double dy,
            final double dz)
    {
        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        if (dx != 0.0) {
            final double t1 = (minX - x) / dx;
            final double t2 = (maxX - x) / dx;
            tMin = Math.max(tMin, Math.min(t1, t2));
            tMax = Math.min(tMax, Math.max(t1, t2));
        } else {
            if ((minX <= x && x < maxX) == false) {
                return Double.NaN;
            }
        }

        if (dy != 0.0) {
            final double t1 = (minY - y) / dy;
            final double t2 = (maxY - y) / dy;
            tMin = Math.max(tMin, Math.min(t1, t2));
            tMax = Math.min(tMax, Math.max(t1, t2));
        } else {
            if ((minY <= y && y < maxY) == false) {
                return Double.NaN;
            }
        }

        if (dz != 0.0) {
            final double t1 = (minZ - z) / dz;
            final double t2 = (maxZ - z) / dz;
            tMin = Math.max(tMin, Math.min(t1, t2));
            tMax = Math.min(tMax, Math.max(t1, t2));
        } else {
            if ((minZ <= z && z < maxZ) == false) {
                return Double.NaN;
            }
        }

        if (tMin <= tMax && tMax >= 0.0) {
            return Math.max(0.0, tMin);
        } else {
            return Double.NaN;
        }
    }

    private OctTree(
                final double minX,
                final double minY,
                final double minZ,
                final double maxX,
                final double maxY,
                final double maxZ)
    {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

        assert (this.minX < this.maxX && this.minY < this.maxY && this.minZ < this.maxZ);
        assert (this.maxX - this.minX == this.maxY - this.minY && this.maxX - this.minX == this.maxZ - this.minZ);
    }

    private static final class LeafNode extends OctTree
    {
        private LeafNode(
                final double minX,
                final double minY,
                final double minZ,
                final double maxX,
                final double maxY,
                final double maxZ,
                final boolean filled)
        {
            super(minX, minY, minZ, maxX, maxY, maxZ);
            this.filled = filled;
        }

        private final boolean filled;

        @Override
        public final double firstHit(double x, double y, double z, double dx, double dy, double dz) {
            if (filled) {
                return firstHitWithBoundingBox(x, y, z, dx, dy, dz);
            } else {
                return Double.NaN;
            }
        }

        @Override
        public final OctTree repSetRegion(
                double minX,
                double minY,
                double minZ,
                double maxX,
                double maxY,
                double maxZ,
                boolean fill) {
            if (this.filled == fill) {
                // Leaf already agrees, so skip.
                return this;
            } else if (
                this.maxX <= minX ||
                this.maxY <= minY ||
                this.maxZ <= minZ ||
                maxX <= this.minX ||
                maxY <= this.minY ||
                maxZ <= this.minZ) {
                // Node does not overlap, so skip.
                return this;
            } else if (
                minX <= this.minX &&
                minY <= this.minY &&
                minZ <= this.minZ &&
                this.maxX <= maxX &&
                this.maxY <= maxY &&
                this.maxZ <= maxZ) {
                // Node is entirely filled, so change.
                return new LeafNode(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, fill);
            } else {
                // Partial overlap, so split.
                final double newMinX = this.minX;
                final double newMidX = (this.minX + this.maxX) / 2.0;
                final double newMaxX = this.maxX;

                final double newMinY = this.minY;
                final double newMidY = (this.minY + this.maxY) / 2.0;
                final double newMaxY = this.maxY;

                final double newMinZ = this.minZ;
                final double newMidZ = (this.minZ + this.maxZ) / 2.0;
                final double newMaxZ = this.maxZ;

                return new InnerNode(
                        (new LeafNode(newMinX, newMinY, newMinZ, newMidX, newMidY, newMidZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMidX, newMinY, newMinZ, newMaxX, newMidY, newMidZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMinX, newMidY, newMinZ, newMidX, newMaxY, newMidZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMidX, newMidY, newMinZ, newMaxX, newMaxY, newMidZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMinX, newMinY, newMidZ, newMidX, newMidY, newMaxZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMidX, newMinY, newMidZ, newMaxX, newMidY, newMaxZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMinX, newMidY, newMidZ, newMidX, newMaxY, newMaxZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill),
                        (new LeafNode(newMidX, newMidY, newMidZ, newMaxX, newMaxY, newMaxZ, this.filled)).repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill));
            }
        }

        @Override
        public int nodeCount() {
            return 1;
        }
    }
    
    private static final class InnerNode extends OctTree
    {
        private InnerNode(
            final OctTree nodeA,
            final OctTree nodeB,
            final OctTree nodeC,
            final OctTree nodeD,
            final OctTree nodeE,
            final OctTree nodeF,
            final OctTree nodeG,
            final OctTree nodeH)
        {
            super(nodeA.minX, nodeA.minY, nodeA.minZ, nodeH.maxX, nodeH.maxY, nodeH.maxZ);
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.nodeC = nodeC;
            this.nodeD = nodeD;
            this.nodeE = nodeE;
            this.nodeF = nodeF;
            this.nodeG = nodeG;
            this.nodeH = nodeH;

            assert (this.nodeA.minX == this.nodeC.minX);
            assert (this.nodeA.minX == this.nodeE.minX);
            assert (this.nodeA.minX == this.nodeG.minX);

            assert (this.nodeA.maxX == this.nodeB.minX);
            assert (this.nodeA.maxX == this.nodeC.maxX);
            assert (this.nodeA.maxX == this.nodeD.minX);
            assert (this.nodeA.maxX == this.nodeE.maxX);
            assert (this.nodeA.maxX == this.nodeF.minX);
            assert (this.nodeA.maxX == this.nodeG.maxX);
            assert (this.nodeA.maxX == this.nodeH.minX);

            assert (this.nodeH.maxX == this.nodeB.maxX);
            assert (this.nodeH.maxX == this.nodeD.maxX);
            assert (this.nodeH.maxX == this.nodeF.maxX);


            assert (this.nodeA.minY == this.nodeB.minY);
            assert (this.nodeA.minY == this.nodeE.minY);
            assert (this.nodeA.minY == this.nodeF.minY);

            assert (this.nodeA.maxY == this.nodeB.maxY);
            assert (this.nodeA.maxY == this.nodeC.minY);
            assert (this.nodeA.maxY == this.nodeD.minY);
            assert (this.nodeA.maxY == this.nodeE.maxY);
            assert (this.nodeA.maxY == this.nodeF.maxY);
            assert (this.nodeA.maxY == this.nodeG.minY);
            assert (this.nodeA.maxY == this.nodeH.minY);

            assert (this.nodeH.maxY == this.nodeC.maxY);
            assert (this.nodeH.maxY == this.nodeD.maxY);
            assert (this.nodeH.maxY == this.nodeG.maxY);


            assert (this.nodeA.minZ == this.nodeB.minZ);
            assert (this.nodeA.minZ == this.nodeC.minZ);
            assert (this.nodeA.minZ == this.nodeD.minZ);

            assert (this.nodeA.maxZ == this.nodeB.maxZ);
            assert (this.nodeA.maxZ == this.nodeC.maxZ);
            assert (this.nodeA.maxZ == this.nodeD.maxZ);
            assert (this.nodeA.maxZ == this.nodeE.minZ);
            assert (this.nodeA.maxZ == this.nodeF.minZ);
            assert (this.nodeA.maxZ == this.nodeG.minZ);
            assert (this.nodeA.maxZ == this.nodeH.minZ);

            assert (this.nodeH.maxZ == this.nodeE.maxZ);
            assert (this.nodeH.maxZ == this.nodeF.maxZ);
            assert (this.nodeH.maxZ == this.nodeG.maxZ);
        }

        private final OctTree nodeA;
        private final OctTree nodeB;
        private final OctTree nodeC;
        private final OctTree nodeD;
        private final OctTree nodeE;
        private final OctTree nodeF;
        private final OctTree nodeG;
        private final OctTree nodeH;

        private static final void swap(OctTree[] array, int index1, int index2)
        {
            final OctTree temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }

        @Override
        public final double firstHit(double x, double y, double z, double dx, double dy, double dz) {
            double result = Double.NaN;

            if (Double.isNaN(firstHitWithBoundingBox(x, y, z, dx, dy, dz)) == false) {
                OctTree[] childrenInIntersectOrder = new OctTree[]{nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH};

                if (dx < 0.0) {
                    swap(childrenInIntersectOrder, 0, 1);
                    swap(childrenInIntersectOrder, 2, 3);
                    swap(childrenInIntersectOrder, 4, 5);
                    swap(childrenInIntersectOrder, 6, 7);
                }
                if (dy < 0.0) {
                    swap(childrenInIntersectOrder, 0, 2);
                    swap(childrenInIntersectOrder, 1, 3);
                    swap(childrenInIntersectOrder, 4, 6);
                    swap(childrenInIntersectOrder, 5, 7);
                }
                if (dz < 0.0) {
                    swap(childrenInIntersectOrder, 0, 4);
                    swap(childrenInIntersectOrder, 1, 5);
                    swap(childrenInIntersectOrder, 2, 6);
                    swap(childrenInIntersectOrder, 3, 7);
                }

                for (OctTree node: childrenInIntersectOrder) {
                    double t = node.firstHit(x, y, z, dx, dy, dz);
                    if (Double.isNaN(t) == false) {
                        result = t;
                        break;
                    }
                }
            }

            assert(Double.isNaN(result) || result >= 0.0);
            return result;
        }

        @Override
        public final OctTree repSetRegion(
                double minX,
                double minY,
                double minZ,
                double maxX,
                double maxY,
                double maxZ,
                boolean fill) {
            if (
                this.maxX <= minX ||
                this.maxY <= minY ||
                this.maxZ <= minZ ||
                maxX <= this.minX ||
                maxY <= this.minY ||
                maxZ <= this.minZ) {
                // Node does not overlap, so skip.
                return this;
            } else if (
                minX <= this.minX &&
                minY <= this.minY &&
                minZ <= this.minZ &&
                this.maxX <= maxX &&
                this.maxY <= maxY &&
                this.maxZ <= maxZ) {
                // Node is entirely filled, so change.
                return new LeafNode(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, fill);
            } else {
                final OctTree newNodeA = nodeA.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeB = nodeB.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeC = nodeC.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeD = nodeD.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeE = nodeE.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeF = nodeF.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeG = nodeG.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                final OctTree newNodeH = nodeH.repSetRegion(minX, minY, minZ, maxX, maxY, maxZ, fill);
                if ((newNodeA instanceof LeafNode) && ((LeafNode)newNodeA).filled == fill &&
                    (newNodeB instanceof LeafNode) && ((LeafNode)newNodeB).filled == fill &&
                    (newNodeC instanceof LeafNode) && ((LeafNode)newNodeC).filled == fill &&
                    (newNodeD instanceof LeafNode) && ((LeafNode)newNodeD).filled == fill &&
                    (newNodeE instanceof LeafNode) && ((LeafNode)newNodeE).filled == fill &&
                    (newNodeF instanceof LeafNode) && ((LeafNode)newNodeF).filled == fill &&
                    (newNodeG instanceof LeafNode) && ((LeafNode)newNodeG).filled == fill &&
                    (newNodeH instanceof LeafNode) && ((LeafNode)newNodeH).filled == fill) {
                    // All child nodes now totally agree.
                    return new LeafNode(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, fill);
                } else {
                    return new InnerNode(newNodeA, newNodeB, newNodeC, newNodeD, newNodeE, newNodeF, newNodeG, newNodeH);
                }
            }
        }

        @Override
        public int nodeCount() {
            return 1 +
                nodeA.nodeCount() +
                nodeB.nodeCount() +
                nodeC.nodeCount() +
                nodeD.nodeCount() +
                nodeE.nodeCount() +
                nodeF.nodeCount() +
                nodeG.nodeCount() +
                nodeH.nodeCount();
        }
    }

    /**
        Most negative X extent of this node (inclusive).
    */
    protected final double minX;

    /**
        Most negative Y extent of this node (inclusive).
    */
    protected final double minY;

    /**
        Most negative Z extent of this node (inclusive).
    */
    protected final double minZ;

    /**
        Most positive X extent of this node (exclusive).
    */
    protected final double maxX;

    /**
        Most positive Y extent of this node (exclusive).
    */
    protected final double maxY;

    /**
        Most positive Z extent of this node (exclusive).
    */
    protected final double maxZ;
}