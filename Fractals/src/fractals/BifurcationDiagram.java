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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;

final class BifurcationDiagram extends BackgroundRenderingComponent
{
    private static final long serialVersionUID = 6987190714076485299L;
    
    private final double[] controlPointValues = defaultControlPointValues();
    
    BifurcationDiagram()
    {
        super(2);
    }
    
    static JComponent createComponent()
    {
        return new BifurcationDiagram();
    }

    @Override
    protected void render(Graphics2D g) throws InterruptedException
    {
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getSupersampledWidth(), getSupersampledHeight());
        super.bufferIsNowOkayToBlit();
        g.setColor(Color.WHITE);
        
        final InterpolatingCubicSpline spline = new InterpolatingCubicSpline(controlPointValues);
        for (int xInt = 0; xInt < getSupersampledWidth(); xInt++) {
            final double x = ((xInt + 0.5) / getSupersampledWidth()) * 1.2 + 2.8;
            double y = 0.5;
            for (int i = 0; i < 1000; i++) {
                y = x * spline.sample(y * controlPointValues.length);
                if (i > 100) {
                    final int yInt = (int)Math.round(y * getSupersampledHeight());
                    if (yInt >= 0 && yInt < getSupersampledHeight()) {
                        g.fillRect(xInt, yInt, 1, 1);
                    }
                }
            }
        }
    }
    
    private static double[] defaultControlPointValues()
    {
        double[] result = new double[20];
        for (int i = 0; i < result.length; i++) {
            final double x = (i + 0.5) / result.length;
            final double y = x * (1 - x);
            result[i] = y;
        }
        return result;
    }
}
