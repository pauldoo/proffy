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

package mandelbulb;

/**
    Immutable binary segmentation in 3D.
*/
public class OctTree
{
    public static OctTree createEmpty()
    {
        return new LeafNode(
                -1.0, -1.0, -1.0,
                1.0, 1.0, 1.0,
                false);
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