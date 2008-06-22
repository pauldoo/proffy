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

/**
    Hardcoded to render the newton raphson fractal for z^4 + z^3 - 1 = 0.
*/
final class NewtonRaphson implements TileProvider<IntegerTile>
{
    /**
        These have been precomputed using maxima:

        (%i14) float(solve(z^4 + z^3 - 1));<br/>
        (%o14) [z = - 1.380277569097613, z = .8191725133961627,<br/> 
        z = - .9144736629677245 %i - 0.219447472149275,<br/>
        z = .9144736629677245 %i - 0.219447472149275]<br/>
    */
    private static final Complex[] roots = new Complex[]{
        new Complex(- 1.380277569097613, 0.0),
        new Complex(.8191725133961627, 0.0),
        new Complex(- 0.219447472149275, - .9144736629677245),
        new Complex(0.219447472149275, .9144736629677245)
    };
    
    private static final Complex minusOne = new Complex(-1.0, 0.0);
    private static final Complex two = new Complex(2.0, 0.0);
    private static final Complex three = new Complex(3.0, 0.0);
    private static final Complex four = new Complex(4.0, 0.0);

    final int maxIterations;
    
    NewtonRaphson(int maxIterations)
    {
        this.maxIterations = maxIterations;
    }
    
    static JComponent createView()
    {
        TileProvider<RenderableTile> source = new ColorAndExposeFilter(new NewtonRaphson(50), roots.length, 0.08);
        CanvasView view = new CanvasView(800, 600, source);
        view.startAllThreads();
        return view;
    }
    
    private static int iterateUntilFinished(final double x, final double y, final int maxIterations)
    {
        Complex z = new Complex(x, y);
        int i;
        for (i = 0; i < maxIterations; i++) {
            for (int j = 0; j < roots.length; j++) {
                final Complex root = roots[j];
                if (root.subtract(z).magnitude() <= 1e-6) {
                    return i * roots.length + j;
                }
            }
            Complex step = calculateStep(z);
            if (step.magnitude() < 1e-6) {
                break;
            }
            Complex.subtractReplace(z, step);
        }
        return 0;
    }
    
    private static Complex calculateStep(final Complex x)
    {
        // (z^4 + z^3 - 1) / (4x^3 + 3x^2)
        Complex numerator = minusOne.clone();

        Complex temp = x.multiply(x);
        Complex denom = temp.multiply(three);
        
        Complex.multiplyReplace(temp, x);
        Complex.addReplace(numerator, temp);
        Complex.addReplace(denom, temp.multiply(four));
        
        Complex.multiplyReplace(temp, x);
        Complex.addReplace(numerator, temp);

        Complex.divideReplace(numerator, denom);
        
        return numerator;
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
