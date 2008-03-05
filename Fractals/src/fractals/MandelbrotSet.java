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

import java.awt.Dimension;
import java.awt.image.BufferedImage;


final class MandelbrotSet implements TileProvider<IntegerTile>
{
    private final int maxIterations;
    
    public MandelbrotSet(int maxIterations) {
        this.maxIterations = maxIterations;
    }
    
    public IntegerTile getTile(TilePosition pos)
    {
        IntegerTile tile = new IntegerTile(pos);
        for (int iy = pos.getMinY(); iy <= pos.getMaxY(); iy++) {
            for (int ix = pos.getMinX(); ix <= pos.getMaxX(); ix++) {
                final double r = ix * pos.relativeScale() / 200 - 2.5;
                final double i = iy * pos.relativeScale() / 200 - 1.5;
                int v = iterateUntilEscapes(r, i);
                tile.setValue(ix, iy, v);
            }
        }
        return tile;
    }
    
    private int iterateUntilEscapes(double cR, double cI)
    {
        // z0 = c;
        final Complex c = new Complex(cR, cI);
        Complex z = c.clone();
        
        int v;
        for (v = 0; v < maxIterations && z.magnitudeSquared() <= 4; v++) {
            // z => z^2 + c
            Complex.multiplyReplace(z, z);
            Complex.addReplace(z, c);
        }
        if (v == maxIterations) v = 0;
        return v;
    }
    
    static BufferedImage quickRender(Complex min, Complex max, Dimension imageSize)
    {
        BufferedImage result = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_BYTE_GRAY);
        MandelbrotSet me = new MandelbrotSet(32);
        for (int y = 0; y < imageSize.height; y++) {
            final double cI = ((y + 0.5) / imageSize.height) * (max.getImaginary() - min.getImaginary()) + min.getImaginary();
            for (int x = 0; x < imageSize.width; x++) {
                final double cR = ((x + 0.5) / imageSize.width) * (max.getReal() - min.getReal()) + min.getReal();
                int v = me.iterateUntilEscapes(cR, cI);
                int rgb = (v << 3) | (v << 11) | (v << 19); 
                result.setRGB(x, y, rgb);
            }
        }
        return result;
    }      
}
