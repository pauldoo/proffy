package escape;

/**
    6 wide
    5 deep
*/
public final class State implements Comparable<State>
{
    private static final int WIDTH = 6;
    private static final int HEIGHT = 5;
    private static final int EMPTY_VALUE = 0;

    public int compareTo(State o) {
        if (this != o) {
            for (int i = 0; i < HEIGHT; i++) {
                if (this.fBoard[i] != o.fBoard[i]) {
                    for (int j = 0; j < WIDTH; j++) {
                        int a = this.fBoard[i][j];
                        int b = o.fBoard[i][j];
                        if (a != b) {
                            return (a - b);
                        }
                    }
                }
            }
        }
        return 0;
    }

    private State()
    {
        fBoard = new short[HEIGHT][];
        for (int i = 0; i < fBoard.length; i++) {
            fBoard[i] = new short[WIDTH];
        }
    }

    private State(State other, int row, int col, short value)
    {
        fBoard = new short[HEIGHT][];
        System.arraycopy(
                other.fBoard, 0,
                this.fBoard, 0,
                HEIGHT);
        fBoard[row] = new short[WIDTH];
        System.arraycopy(
                other.fBoard[row], 0,
                this.fBoard[row], 0,
                WIDTH);
        this.fBoard[row][col] = value;
    }

    static State createEmpty()
    {
        return new State();
    }

    State repSetCell(int row, int col, int value)
    {
        State result = new State(this, row, col, (short)value);
        return result;
    }

    State repMoveUp(int value)
    {
        State result = this;
        for (int i = 0; i <= HEIGHT-1; i++) {
            for (int j = 0; j <= WIDTH-1; j++) {
                if (result.getCell(i, j) == value) {
                    if (i > 0 && result.getCell(i - 1, j) == EMPTY_VALUE) {
                        result = result.repSetCell(i, j, EMPTY_VALUE).repSetCell(i - 1, j, value);
                    } else {
                        return null;
                    }
                }
            }
        }
        return result;
    }

    State repMoveDown(int value)
    {
        State result = this;
        for (int i = HEIGHT-1; i >= 0; i--) {
            for (int j = 0; j <= WIDTH-1; j++) {
                if (result.getCell(i, j) == value) {
                    if (i < HEIGHT-1 && result.getCell(i + 1, j) == EMPTY_VALUE) {
                        result = result.repSetCell(i, j, EMPTY_VALUE).repSetCell(i + 1, j, value);
                    } else {
                        return null;
                    }
                }
            }
        }
        return result;
    }

    State repMoveLeft(int value)
    {
        State result = this;
        for (int j = 0; j <= WIDTH-1; j++) {
            for (int i = 0; i <= HEIGHT-1; i++) {
                if (result.getCell(i, j) == value) {
                    if (j > 0 && result.getCell(i, j - 1) == EMPTY_VALUE) {
                        result = result.repSetCell(i, j, EMPTY_VALUE).repSetCell(i, j - 1, value);
                    } else {
                        return null;
                    }
                }
            }
        }
        return result;
    }
    State repMoveRight(int value)
    {
        State result = this;
        for (int j = WIDTH-1; j >= 0; j--) {
            for (int i = 0; i <= HEIGHT-1; i++) {
                if (result.getCell(i, j) == value) {
                    if (j < WIDTH-1 && result.getCell(i, j + 1) == EMPTY_VALUE) {
                        result = result.repSetCell(i, j, EMPTY_VALUE).repSetCell(i, j + 1, value);
                    } else {
                        return null;
                    }
                }
            }
        }
        return result;
    }

    boolean isSolved()
    {
        return
            getCell(2, 5) == 1 &&
            getCell(3, 5) == 1;
    }

    private short getCell(int row, int col)
    {
        return fBoard[row][col];
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                int v = getCell(i, j);
                if (v == EMPTY_VALUE) {
                    result.append('.');
                } else {
                    result.append(v);
                }
            }
            result.append('\n');
        }
        return result.toString();
    }

    private final short[][] fBoard;
}
