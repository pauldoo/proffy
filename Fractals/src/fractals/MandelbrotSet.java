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

/**
    Mandelbrot and Julia sets, implemented by thinking about them both as
    different cuts through the same 4D volume.
*/
final class MandelbrotSet implements TileProvider<IntegerTile>
{
    private final int maxIterations;
    private final Vector4 origin;
    private final Vector4 dx;
    private final Vector4 dy;
    
    private MandelbrotSet(int maxIterations, Vector4 origin, Vector4 dx, Vector4 dy) {
        this.maxIterations = maxIterations;
        this.origin = origin;
        this.dx = dx;
        this.dy = dy;
    }
    
    static MandelbrotSet createMandelbrotSet(int maxIterations)
    {
        return new MandelbrotSet(
                maxIterations,
                new Vector4(0, 0, 0, 0),
                new Vector4(0, 0, 1, 0),
                new Vector4(0, 0, 0, 1));
    }
    
    static MandelbrotSet createJuliaSet(int maxIterations, Complex constant)
    {
        return new MandelbrotSet(
                maxIterations,
                new Vector4(0, 0, constant.getReal(), constant.getImaginary()),
                new Vector4(1, 0, 0, 0),
                new Vector4(0, 1, 0, 0));
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
    
    private int iterateUntilEscapes(double x, double y)
    {
        Vector4 p = origin.add(dx.multiply(x)).add(dy.multiply(y));
        return iterate4dSequence(p, maxIterations);
    }
    
    private static int iterate4dSequence(
            final Vector4 p,
            final int maxIterations)
    {
        Complex z = new Complex(p.getA(), p.getB());
        final Complex constant = new Complex(p.getC(), p.getD());
        
        int v;
        for (v = 0; v < maxIterations && z.magnitudeSquared() <= 4; v++) {
            Complex.multiplyReplace(z, z);
            Complex.addReplace(z, constant);
        }
        return v % maxIterations;
    }
    
    static BufferedImage quickMandelbrotRender(Complex min, Complex max, Dimension imageSize)
    {
        BufferedImage result = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_BYTE_GRAY);
        MandelbrotSet me = createMandelbrotSet(32);
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
