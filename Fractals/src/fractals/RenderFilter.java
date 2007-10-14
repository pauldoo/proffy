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

import java.awt.Color;

public final class RenderFilter implements TileProvider<RenderableTile>
{
    private final TileProvider<IntegerTile> source;
    private final double exposure;
    
    public RenderFilter(TileProvider<IntegerTile> source, double exposure)
    {
        this.source = source;
        this.exposure = exposure;
    }

    public RenderableTile getTile(TilePosition position)
    {
        IntegerTile sourceTile = source.getTile(position);
        RenderableTile result = new RenderableTile(position);
        for (int y = position.getMinY(); y <= position.getMaxY(); y++) {
            for (int x = position.getMinX(); x <= position.getMaxX(); x++) {
                float v = (float)(1.0 - Math.exp(-exposure * sourceTile.getValue(x, y)));
                result.setPixel(x, y, new Color(v, v, v).getRGB());
            }
        }
        return result;
    }
}
