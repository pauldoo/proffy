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

package algorithmx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
    An implementation of the "DancingLinks" algorithm:
    http://en.wikipedia.org/wiki/Dancing_Links
*/
public final class DancingLinks {

    public static final class MatrixHeader {
        /**
            First column in matrix, or null if matrix is empty.
        */
        private ColumnHeader fRootColumnHeader;
    }

    public static interface IInsertable
    {
        public void insert();
    }

    /**
        There is one ColumnHeader per column in the matrix.  They form a
        circular doubly-linked list so that all columns can be easily enumerated.
    */
    private static final class ColumnHeader implements IInsertable {
        /**
            Previous column.
        */
        private ColumnHeader fLeft;

        /**
            Next column.
        */
        private ColumnHeader fRight;

        /**
            First one-bit in column or null if column is empty.
        */
        private Node fRootNode;

        /**
            Total number of one-bits in column.
            (Size of doubly-linked list which fRoot is a part of.)
        */
        private int fOnesCount;

        /**
            Matrix this column belongs to.
        */
        private MatrixHeader fMatrixHeader;

        public void insert() {
            insertColumnHeader(this);
        }
    }

    /**
        Represents a single one bit in the sparse matrix.
    */
    private static final class Node implements IInsertable {
        Node(int rowNumber) {
            this.fRowNumber = rowNumber;
        }

        final int fRowNumber;

        /**
            Next one-bit above in this column.
        */
        private Node fUp = this;

        /**
            Next one-bit below in this column
        */
        private Node fDown = this;

        /**
            Next one-bit to the left in this row.
        */
        private Node fLeft = this;

        /**
            Next one-bit tot he right in this row.
        */
        private Node fRight = this;

        /**
            Column this one-bit belongs to.
        */
        private ColumnHeader fColumnHeader;

        public void insert() {
            insertNode(this);
        }
    }




    public static SparseBinaryMatrix constructFromDenseMatrix(final boolean[][] matrix) {
        if (matrix.length == 0 ||  matrix[0].length == 0) {
            throw new IllegalArgumentException("Must be non-empty.");
        }
        final int rowCount = matrix.length;
        final int columnCount = matrix[0].length;
        for (int row = 0; row < rowCount; row++) {
            if (matrix[row].length != columnCount) {
                throw new IllegalArgumentException("Rows must be of equal length.");
            }
        }

        SparseBinaryMatrix result = new SparseBinaryMatrix(rowCount, columnCount);

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (matrix[row][column]) {
                    result.setBit(new SparseBinaryMatrix.BitLocation(row, column), true);
                }
            }
        }
        return result;
    }

    private static MatrixHeader constructFromSparseMatrix(final SparseBinaryMatrix sparseMatrix) {
        MatrixHeader result = new MatrixHeader();

        ColumnHeader[] headers = new ColumnHeader[sparseMatrix.columnCount()];
        for (int col = 0; col < sparseMatrix.columnCount(); col++) {
            headers[col] = new ColumnHeader();
            headers[col].fMatrixHeader = result;
        }
        for (int col = 0; col < sparseMatrix.columnCount(); col++) {
            headers[col].fLeft = headers[(col + sparseMatrix.columnCount() - 1) % sparseMatrix.columnCount()];
            headers[col].fRight = headers[(col + sparseMatrix.columnCount() + 1) % sparseMatrix.columnCount()];
        }
        
        Node[] rowRootNodes = new Node[sparseMatrix.rowCount()];
        for (SparseBinaryMatrix.BitLocation bit: sparseMatrix.allOnes()) {
            Node node = new Node(bit.fRow);
            node.fColumnHeader = headers[bit.fColumn];

            if (node.fColumnHeader.fRootNode != null) {
                node.fUp = node.fColumnHeader.fRootNode.fUp; // Last node in column
                node.fDown = node.fColumnHeader.fRootNode; // First node in column
            }

            if (rowRootNodes[bit.fRow] == null) {
                rowRootNodes[bit.fRow] = node;
            } else {
                node.fLeft = rowRootNodes[bit.fRow].fLeft; // Last node in row
                node.fRight = rowRootNodes[bit.fRow]; // First node in row
            }

            insertNode(node);
        }

        result.fRootColumnHeader = headers[0];
        return result;
    }

    private static void removeNode(Node node) {
        if (node.fUp.fDown != node ||
            node.fDown.fUp != node ||
            node.fLeft.fRight != node ||
            node.fRight.fLeft != node) {
            throw new IllegalStateException("Node is not part of list");
        }
        node.fUp.fDown = node.fDown;
        node.fDown.fUp = node.fUp;
        node.fLeft.fRight = node.fRight;
        node.fRight.fLeft = node.fLeft;
        node.fColumnHeader.fOnesCount --;
        if (node.fColumnHeader.fRootNode == node) {
            node.fColumnHeader.fRootNode = node.fDown;
            if (node.fColumnHeader.fRootNode == node) {
                node.fColumnHeader.fRootNode = null;
            }
        }
    }

    private static void insertNode(Node node) {
        node.fUp.fDown = node;
        node.fDown.fUp = node;
        node.fLeft.fRight = node;
        node.fRight.fLeft = node;
        node.fColumnHeader.fOnesCount ++;
        if (node.fColumnHeader.fRootNode == null) {
            node.fColumnHeader.fRootNode = node;
        }
    }

    private static void removeColumnHeader(ColumnHeader column) {
        if ((column.fRootNode == null) != (column.fOnesCount == 0)) {
            throw new IllegalStateException("ColumnHeader is out of sync");
        }

        if (column.fOnesCount != 0) {
            throw new IllegalStateException("Column must be empty before being removed");
        }
        if (column.fLeft.fRight != column ||
            column.fRight.fLeft != column) {
            throw new IllegalStateException("ColumnHeader is not part of list");
        }
        column.fLeft.fRight = column.fRight;
        column.fRight.fLeft = column.fLeft;
        if (column.fMatrixHeader.fRootColumnHeader == column) {
            column.fMatrixHeader.fRootColumnHeader = column.fRight;
            if (column.fMatrixHeader.fRootColumnHeader == column) {
                column.fMatrixHeader.fRootColumnHeader = null;
            }
        }
    }

    private static void insertColumnHeader(ColumnHeader column) {
        column.fLeft.fRight = column;
        column.fRight.fLeft = column;
        if (column.fMatrixHeader.fRootColumnHeader == null) {
            column.fMatrixHeader.fRootColumnHeader = column;
        }
    }

    /**
        Removes all nodes in a row.  May leave behind empty columns.
    */
    private static void removeRow(Node node, Stack<IInsertable> undoStack) {
        List<Node> nodesToRemove = new ArrayList<Node>();
        Node i = node;
        do {
            nodesToRemove.add(i);
            i = i.fRight;
        } while (i != node);
        for (Node n: nodesToRemove) {
            removeNode(n);
            undoStack.push(n);
        }
    }

    /**
        Remove column and all rows with a one in this column.

        Returns a stack of nodes and ColumnHeaders which can be re-inserted to undo this
        operation.
    */
    private static void coverColumn(ColumnHeader column, Stack<IInsertable> undoStack) {
        if ((column.fRootNode == null) != (column.fOnesCount == 0)) {
            throw new IllegalStateException("ColumnHeader is out of sync");
        }

        while (column.fRootNode != null) {
            removeRow(column.fRootNode, undoStack);
        }
        removeColumnHeader(column);
        undoStack.push(column);
    }

    private static void solve(final MatrixHeader matrixHeader, Set<Set<Integer>> solutions, Set<Integer> partialSolution) {
        if (matrixHeader.fRootColumnHeader == null) {
            solutions.add(new HashSet<Integer>(partialSolution));
        } else {

            ColumnHeader selectedColumn = matrixHeader.fRootColumnHeader;
            {
                ColumnHeader header = matrixHeader.fRootColumnHeader;
                do {
                    if (header.fOnesCount < selectedColumn.fOnesCount) {
                        selectedColumn = header;
                    }
                    header = header.fRight;
                } while (header != matrixHeader.fRootColumnHeader);
            }

            // For each row that had a one in this column - try including it in the solution
            Node selectedRow = selectedColumn.fRootNode;
            for (int i = 0; i < selectedColumn.fOnesCount; i++) {
                // Include this row in the solution
                partialSolution.add(selectedRow.fRowNumber);

                // Cover all columns which have a 1 in the selected row
                Stack<IInsertable> undoStack = new Stack<IInsertable>();
                {
                    List<ColumnHeader> columnsToCover = new ArrayList<ColumnHeader>();
                    Node node = selectedRow;
                    do {
                        columnsToCover.add(node.fColumnHeader);
                        node = node.fRight;
                    } while (node != selectedRow);
                    for (ColumnHeader col: columnsToCover) {
                        coverColumn(col, undoStack);
                    }
                }

                solve(matrixHeader, solutions, partialSolution);

                while (undoStack.empty() == false) {
                    final IInsertable obj = undoStack.pop();
                    obj.insert();
                }

                partialSolution.remove(selectedRow.fRowNumber);

                selectedRow = selectedRow.fDown;
            }
        }
    }

    public static Set<Set<Integer>> solve(SparseBinaryMatrix matrix)
    {
        MatrixHeader matrixHeader = constructFromSparseMatrix(matrix);
        Set<Integer> emptySolution = new HashSet<Integer>();
        Set<Set<Integer>> solutions = new HashSet<Set<Integer>>();
        solve(matrixHeader, solutions, emptySolution);
        return solutions;
    }
}
