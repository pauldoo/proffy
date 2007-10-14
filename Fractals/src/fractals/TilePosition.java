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

final class TilePosition
{
    public static final int SIZE = 32;
    
    private final int indexX;
    private final int indexY;
    private final int scaleIndex;
        
    /** Creates a new instance of TilePosition */
    public TilePosition(int indexX, int indexY, int scaleIndex)
    {
        this.indexX = indexX;
        this.indexY = indexY;
        this.scaleIndex = scaleIndex;
    }
    
    public double scale()
    {
        return Math.pow(0.75, scaleIndex) / 800.0;
    }
    
    public int getMinX()
    {
        return indexX * SIZE;
    }

    public int getMinY()
    {
        return indexY * SIZE;
    }
    
    public int getMaxX()
    {
        return getMinX() + SIZE - 1;
    }
    
    public int getMaxY()
    {
        return getMinY() + SIZE - 1;
    }
}
