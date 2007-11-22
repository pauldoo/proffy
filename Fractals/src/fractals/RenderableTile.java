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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class RenderableTile extends Tile 
{
    private final BufferedImage image;
    
    public RenderableTile(TilePosition position)
    {
        super(position);
        image = new BufferedImage(TilePosition.SIZE, TilePosition.SIZE, BufferedImage.TYPE_INT_RGB);
    }
    
    public void render(Graphics2D g)
    {
        if (false) {
            boolean success = g.drawImage(image, position.getMinX(), position.getMinY(), null);
            if (!success) {
                throw new RuntimeException("Failed to blit");
            }
        } else {
            AffineTransform saved = g.getTransform();
            g.scale(position.relativeScale(), position.relativeScale());
            g.translate(position.getMinX(), position.getMinY());
            g.drawImage(image, 0, 0, null);
            g.setTransform(saved);
        }
    }
    
    public void setPixel(int x, int y, int rgb)
    {
        image.setRGB(x - position.getMinX(), y - position.getMinY(), rgb);
    }
}
