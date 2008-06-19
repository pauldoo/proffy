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

import javax.swing.JComponent;

final class NewtonRaphson implements TileProvider<IntegerTile>
{
    final static Complex one = new Complex(1.0, 0.0);
    final static Complex two = new Complex(2.0, 0.0);
    final static Complex three = new Complex(3.0, 0.0);

    final int maxIterations;
    
    NewtonRaphson(int maxIterations)
    {
        this.maxIterations = maxIterations;
    }
    
    static JComponent createView()
    {
        TileProvider<RenderableTile> source = new ColorAndExposeFilter(new NewtonRaphson(1000), 3, 0.08);
        CanvasView view = new CanvasView(800, 600, source);
        view.startAllThreads();
        return view;
    }
    
    private static int iterateUntilFinished(final double x, final double y, final int maxIterations)
    {
        Complex z = new Complex(x, y);
        int i;
        for (i = 0; i < maxIterations; i++) {
            Complex step = f(z).divide(fPrime(z));
            if (step.magnitude() <= 1e-6) {
                break;
            }
            Complex.subtractReplace(z, step);
        }
        if (i == maxIterations) {
            return 0;
        }
        
        if (z.getReal() >= 0) {
            return i * 3 + 0;
        }
        if (z.getImaginary() >= 0) {
            return i * 3 + 1;
        }
        if (z.getImaginary() <= 0) {
            return i * 3 + 2;
        }
        throw new IllegalStateException("Unrecognized root");
    }
    
    private static Complex f(final Complex x)
    {
        return x.power(three).subtract(one);
    }
    
    private static Complex fPrime(final Complex x)
    {
        return three.multiply(x.power(two));
    }
    
    public IntegerTile getTile(TilePosition pos)
    {
        IntegerTile tile = new IntegerTile(pos);
        for (int iy = pos.getMinY(); iy <= pos.getMaxY(); iy++) {
            for (int ix = pos.getMinX(); ix <= pos.getMaxX(); ix++) {
                final double r = ix * pos.relativeScale() / 200 - 2.5;
                final double i = iy * pos.relativeScale() / 200 - 1.5;
                int v = iterateUntilFinished(r, i, maxIterations);
                tile.setValue(ix, iy, v);
            }
        }
        return tile;
    }
}
