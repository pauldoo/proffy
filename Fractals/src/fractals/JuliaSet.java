/*
    Copyright (C) 2007, 2008  Paul Richards.

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

class JuliaSet implements TileProvider<IntegerTile>
{
    private final int maxIterations;
    private final Complex constant;
    
    JuliaSet(int maxIterations, Complex constant)
    {
        this.maxIterations = maxIterations;
        this.constant = constant.clone();
    }
    
    public IntegerTile getTile(TilePosition position)
    {
        IntegerTile tile = new IntegerTile(position);
        for (int iy = position.getMinY(); iy <= position.getMaxY(); iy++) {
            for (int ix = position.getMinX(); ix <= position.getMaxX(); ix++) {
                final double r = ix * position.relativeScale() / 200 - 2.0;
                final double i = iy * position.relativeScale() / 200 - 1.5;
                int v = iterateUntilEscapes(r, i);
                tile.setValue(ix, iy, v);
            }
        }
        return tile;
    }

    private int iterateUntilEscapes(double zR, double zI)
    {
        return MandelbrotSet.iterate4dSequence(
                zR, zI,
                constant.getReal(), constant.getImaginary(),
                maxIterations);
    }
}
