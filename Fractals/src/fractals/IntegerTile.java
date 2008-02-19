/*
    Copyright (C) 2007  Paul Richards.

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

final class IntegerTile extends Tile
{
    private final int[][] values;
    
    /** Creates a new instance of IntegerTile */
    public IntegerTile(TilePosition position)
    {
        super(position);
        this.values = new int[TilePosition.SIZE][];
        for (int i = 0; i < TilePosition.SIZE; i++) {
            this.values[i] = new int[TilePosition.SIZE];
        }
    }
    
    public void setValue(final int x, final int y, final int value)
    {
        int offsetX = x - position.getMinX();
        int offsetY = y - position.getMinY();
        if (offsetX >= 0 && offsetX < TilePosition.SIZE && offsetY >= 0 && offsetY < TilePosition.SIZE) {
            values[offsetY][offsetX] = value;
        } else {
            throw new IllegalArgumentException("Out of bounds");
        }
    }
    
    public int getValue(final int x, final int y)
    {
        int offsetX = x - position.getMinX();
        int offsetY = y - position.getMinY();
        if (offsetX >= 0 && offsetX < TilePosition.SIZE && offsetY >= 0 && offsetY < TilePosition.SIZE) {
            return values[offsetY][offsetX];
        } else {
            throw new IllegalArgumentException("Out of bounds");
        }
    }
}
