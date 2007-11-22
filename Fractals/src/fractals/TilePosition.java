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

final class TilePosition implements Comparable<TilePosition>
{
    public static final int SIZE = 32;
    
    private final int indexX;
    private final int indexY;
    private final int zoomIndex;
        
    /** Creates a new instance of TilePosition */
    public TilePosition(int indexX, int indexY, int zoomIndex)
    {
        this.indexX = indexX;
        this.indexY = indexY;
        this.zoomIndex = zoomIndex;
    }

    public int hashCode()
    {
        return (indexX * 11) ^ (indexY * 13) ^ (zoomIndex * 17);
    }
    
    public boolean equals(Object o)
    {
        return equals((TilePosition)o);
    }
    
    public boolean equals(TilePosition other)
    {
        return
            this == other || (
                this.indexX == other.indexX &&
                this.indexY == other.indexY &&
                this.zoomIndex == other.zoomIndex
            );
    }

    public int compareTo(TilePosition other)
    {
        int result = 0;
        if (result == 0) {
            result = Integer.signum(this.indexX - other.indexX);
        }
        if (result == 0) {
            result = Integer.signum(this.indexY - other.indexY);
        }
        if (result == 0) {
            result = Integer.signum(this.zoomIndex - other.zoomIndex);
        }
        return result;
    }
    
    public String toString()
    {
        return "{" + getMinX() + ", " + getMinY() + ", " + getZoomIndex() + "}";
    }
        
    public double relativeScale()
    {
        return Math.pow(0.5, getZoomIndex());
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
    
    public int getZoomIndex()
    {
        return zoomIndex;
    }
}
