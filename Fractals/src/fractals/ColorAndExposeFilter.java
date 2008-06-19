/*
    Copyright (C) 2008  Paul Richards.

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


final class ColorAndExposeFilter implements TileProvider<RenderableTile>
{
    private final TileProvider<IntegerTile> source;
    private final int numberOfColors;
    private final double exposure;
    
    ColorAndExposeFilter(TileProvider<IntegerTile> source, int numberOfColors, double exposure)
    {
        this.source = source;
        this.numberOfColors = numberOfColors;
        this.exposure = exposure;
    }

    public RenderableTile getTile(TilePosition position)
    {
        IntegerTile sourceTile = source.getTile(position);
        RenderableTile result = new RenderableTile(position);
        for (int y = position.getMinY(); y <= position.getMaxY(); y++) {
            for (int x = position.getMinX(); x <= position.getMaxX(); x++) {
                final int integerValue = sourceTile.getValue(x, y);
                final float hue = ((float)(integerValue % numberOfColors)) / numberOfColors;
                final float v = (float)(Utilities.expose(integerValue / numberOfColors, exposure));
                result.setPixel(x, y, Color.HSBtoRGB(hue, 1.0f, v));
            }
        }
        return result;
    }
}
