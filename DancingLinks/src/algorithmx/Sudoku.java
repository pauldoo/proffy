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
import java.util.List;
import java.util.Set;

/**
    Solves sudoku puzzles using DancingLinks.
*/
public final class Sudoku {
    public static final class Board {
        private final int[][] fTiles;

        public Board() {
            fTiles = new int[9][];
            for (int i = 0; i < fTiles.length; i++) {
                fTiles[i] = new int[9];
            }
        }

        public void setTile(int row, int column, int value) {
            if (value >= 0 && value <= 9) {
                fTiles[row][column] = value;
            } else {
                throw new IllegalArgumentException();
            }
        }

        public int getTile(int row, int column) {
            return fTiles[row][column];
        }

        @Override
        public String toString()
        {
            StringBuffer result = new StringBuffer();
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    int value = getTile(row, column);
                    if (value >= 1) {
                        result.append(value);
                    } else {
                        result.append(' ');
                    }
                    if (column == 2 || column == 5) {
                        result.append("|");
                    }
                }
                result.append("\n");
                if (row == 2 || row == 5) {
                    result.append("---+---+---\n");
                }
            }
            return result.toString();
        }
    }

    public static SparseBinaryMatrix boardAsSparseMatrix(final Board board) {
        // Force one number per cell
        final int CELL_CONSTRAINT_BEGIN = 0;
        final int CELL_CONSTRAINT_END = CELL_CONSTRAINT_BEGIN + 9 * 9;

        // Force each row to only have a since instance of each number 1 through 9
        final int ROW_CONSTRAINT_BEGIN = CELL_CONSTRAINT_END;
        final int ROW_CONSTRAINT_END = ROW_CONSTRAINT_BEGIN + 9 * 9;

        // Force each column to only have a single instance of each number 1 through 9
        final int COLUMN_CONSTRAINT_BEGIN = ROW_CONSTRAINT_END;
        final int COLUMN_CONSTRAINT_END = COLUMN_CONSTRAINT_BEGIN + 9 * 9;

        // Force each column to only have a single instance of each number 1 through 9
        final int BOX_CONSTRAINT_BEGIN = COLUMN_CONSTRAINT_END;
        final int BOX_CONSTRAINT_END = BOX_CONSTRAINT_BEGIN + 9 * 9;

        // Preseeded constraint, one per cell of the board
        final int KNOWN_CONSTRAINT_BEGIN = BOX_CONSTRAINT_END;
        final int KNOWN_CONSTRAINT_END = KNOWN_CONSTRAINT_BEGIN + 9 * 9;

        // Preseed control constraint, just one
        final int CONTROL_CONSTRAINT = KNOWN_CONSTRAINT_END;

        final int TOTAL_CONSTRAINT_COUNT = CONTROL_CONSTRAINT + 1;

        final int TOTAL_ROW_COUNT = 9 * 9 * 9 + 1;

        SparseBinaryMatrix result = new SparseBinaryMatrix(9 * 9 * 9 + 1, TOTAL_CONSTRAINT_COUNT);
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                for (int value = 1; value <= 9; value++) {
                    final int matrixRow = row * 9 * 9 + column * 9 + (value-1);

                    result.setBit(new SparseBinaryMatrix.BitLocation(
                            matrixRow,
                            CELL_CONSTRAINT_BEGIN + row * 9 + column), true);
                    result.setBit(new SparseBinaryMatrix.BitLocation(
                            matrixRow,
                            ROW_CONSTRAINT_BEGIN + row * 9 + (value - 1)), true);
                    result.setBit(new SparseBinaryMatrix.BitLocation(
                            matrixRow,
                            COLUMN_CONSTRAINT_BEGIN + column * 9 + (value - 1)), true);
                    result.setBit(new SparseBinaryMatrix.BitLocation(
                            matrixRow,
                            BOX_CONSTRAINT_BEGIN + ((row / 3) * 3 + (column / 3)) * 9 + (value - 1)), true);

                    if (board.getTile(row, column) == value) {
                        result.setBit(new SparseBinaryMatrix.BitLocation(
                            matrixRow,
                            KNOWN_CONSTRAINT_BEGIN + row * 9 + column), true);
                    }
                }

                if (board.getTile(row, column) == 0) {
                    result.setBit(new SparseBinaryMatrix.BitLocation(
                            TOTAL_ROW_COUNT - 1,
                            KNOWN_CONSTRAINT_BEGIN + row * 9 + column), true);
                }
            }
        }

        result.setBit(new SparseBinaryMatrix.BitLocation(
                TOTAL_ROW_COUNT - 1,
                CONTROL_CONSTRAINT), true);


         return result;
    }

    public static List<Board> solve(Board initial) {
        SparseBinaryMatrix matrix = boardAsSparseMatrix(initial);
        Set<Set<Integer>> solutions = DancingLinks.solve(matrix);

        List<Board> result = new ArrayList<Board>();
        for (Set<Integer> solution: solutions) {
            if (solution.size() != 9 * 9 + 1) {
                throw new IllegalStateException("Solution has unexpected number of elements");
            }
            Board board = new Board();
            for (Integer matrixRow: solution) {
                if (matrixRow < 9 * 9 * 9) {
                    int row = matrixRow / (9 * 9);
                    int column = (matrixRow / 9) % 9;
                    int value = (matrixRow % 9) + 1;
                    board.setTile(row, column, value);
                }
            }
            result.add(board);
        }
        return result;
    }
}
