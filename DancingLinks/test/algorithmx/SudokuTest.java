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

import algorithmx.Sudoku.Board;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;


public class SudokuTest {
    @Test
    public void testSolve() {
        Sudoku.Board board = new Board();
        board.setTile(0, 0, 5);
        board.setTile(0, 1, 3);
        board.setTile(0, 4, 7);
        board.setTile(1, 0, 6);
        board.setTile(1, 3, 1);
        board.setTile(1, 4, 9);
        board.setTile(1, 5, 5);
        board.setTile(2, 1, 9);
        board.setTile(2, 2, 8);
        board.setTile(2, 7, 6);

        board.setTile(3, 0, 8);
        board.setTile(3, 4, 6);
        board.setTile(3, 8, 3);
        board.setTile(4, 0, 4);
        board.setTile(4, 3, 8);
        board.setTile(4, 5, 3);
        board.setTile(4, 8, 1);
        board.setTile(5, 0, 7);
        board.setTile(5, 4, 2);
        board.setTile(5, 8, 6);

        board.setTile(6, 1, 6);
        board.setTile(6, 6, 2);
        board.setTile(6, 7, 8);
        board.setTile(7, 3, 4);
        board.setTile(7, 4, 1);
        board.setTile(7, 5, 9);
        board.setTile(7, 8, 5);
        board.setTile(8, 4, 8);
        board.setTile(8, 7, 7);
        board.setTile(8, 8, 9);

        System.out.println(board);
        Collection<Sudoku.Board> solutions = Sudoku.solve(board);
        for (Sudoku.Board b: solutions) {
            System.out.println(b + "\n");
        }
        assertTrue(solutions.size() == 1);

        board.setTile(3, 4, 0);
        board.setTile(4, 3, 0);
        board.setTile(4, 5, 0);
        board.setTile(5, 4, 0);
        System.out.println(board);
        solutions = Sudoku.solve(board);
        for (Sudoku.Board b: solutions) {
            System.out.println(b + "\n");
        }
        assertTrue(solutions.size() == 63);
    }
}