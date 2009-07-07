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
import org.junit.Test;
import static org.junit.Assert.*;

public class DancingLinksTest {

    private static boolean[][] createEmptyDenseMatrix(int rows, int columns) {
        boolean[][] result = new boolean[rows][];
        for (int i = 0; i < rows; i++) {
            result[i] = new boolean[columns];
        }
        return result;
    }

    @Test
    public void testSolve() {
        {
            boolean[][] matrix = createEmptyDenseMatrix(1, 1);
            matrix[0][0] = true;

            Set<Set<Integer>> solutions = DancingLinks.solve(DancingLinks.constructFromDenseMatrix(matrix));
            assertTrue(solutions.size() == 1);
            Set<Integer> solution = solutions.iterator().next();
            assertTrue(solution.size() == 1);
            assertTrue(solution.iterator().next() == 0);
        }
        {
            boolean[][] matrix = createEmptyDenseMatrix(2, 1);
            matrix[0][0] = true;
            matrix[1][0] = true;

            Set<Set<Integer>> solutions = DancingLinks.solve(DancingLinks.constructFromDenseMatrix(matrix));
            assertTrue(solutions.size() == 2);
            List<Set<Integer>> solutionsAsList = new ArrayList<Set<Integer>>(solutions);
            assertTrue(solutionsAsList.get(0).size() == 1);
            assertTrue(solutionsAsList.get(0).iterator().next() == 1);
            assertTrue(solutionsAsList.get(1).size() == 1);
            assertTrue(solutionsAsList.get(1).iterator().next() == 0);
        }
        {
            boolean[][] matrix = createEmptyDenseMatrix(2, 2);
            matrix[0][0] = true;
            matrix[1][0] = true;

            Set<Set<Integer>> solutions = DancingLinks.solve(DancingLinks.constructFromDenseMatrix(matrix));
            assertTrue(solutions.size() == 0);
        }
        {
            boolean[][] matrix = createEmptyDenseMatrix(2, 2);
            matrix[0][0] = true;
            matrix[1][1] = true;

            Set<Set<Integer>> solutions = DancingLinks.solve(DancingLinks.constructFromDenseMatrix(matrix));
            assertTrue(solutions.size() == 1);
            assertTrue(solutions.iterator().next().size() == 2);
            assertTrue((new ArrayList<Integer>(solutions.iterator().next())).get(0) == 1);
            assertTrue((new ArrayList<Integer>(solutions.iterator().next())).get(1) == 0);
        }
        {
            boolean[][] matrix = createEmptyDenseMatrix(2, 3);
            matrix[0][0] = true;
            matrix[0][1] = true;
            matrix[1][1] = true;
            matrix[1][2] = true;

            Set<Set<Integer>> solutions = DancingLinks.solve(DancingLinks.constructFromDenseMatrix(matrix));
            assertTrue(solutions.size() == 0);
        }
    }
}