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

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
    Simplistic sparse binary matrix.
*/
public final class SparseBinaryMatrix {

    public static final class BitLocation implements Comparable<BitLocation> {
        public final int fRow;
        public final int fColumn;

        public BitLocation(int row, int column) {
            fRow = row;
            fColumn = column;
        }

        public int compareTo(BitLocation other) {
            if (this == other) {
                return 0;
            } else if (this.fRow != other.fRow) {
                return (new Integer(this.fRow)).compareTo(other.fRow);
            } else {
                return (new Integer(this.fColumn)).compareTo(other.fColumn);
            }
        }
    }

    private final int fRows;
    private final int fColumns;
    private final SortedSet<BitLocation> fOnes;

    public SparseBinaryMatrix(final int rows, final int columns) {
        this.fRows = rows;
        this.fColumns = columns;
        this.fOnes = new TreeSet<BitLocation>();
    }

    public int rowCount() {
        return fRows;
    }

    public int columnCount() {
        return fColumns;
    }

    public void setBit(final BitLocation location, boolean value) {
        if (location.fRow >= 0 && location.fRow < fRows &&
            location.fColumn >= 0 && location.fColumn < fColumns) {
            if (value) {
                fOnes.add(location);
            } else {
                fOnes.remove(location);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean getBit(final BitLocation location) {
        if (location.fRow >= 0 && location.fRow < fRows &&
            location.fColumn >= 0 && location.fColumn < fColumns) {
            return fOnes.contains(location);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public SortedSet<BitLocation> allOnes() {
        return Collections.synchronizedSortedSet(fOnes);
    }

    @Override
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("Rows: " + fRows + "\n");
        result.append("Columns: " + fColumns + "\n");
        for (int row = 0; row < fRows; row++) {
            for (int column = 0; column < fColumns; column++) {
                boolean value = getBit(new BitLocation(row, column));
                result.append(value ? '1' : '0');
                if (column != fColumns - 1) {
                    result.append(",");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }
}
