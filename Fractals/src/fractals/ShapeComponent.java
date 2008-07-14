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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.JComponent;

/**
    Simple component that paints a static Shape object.
*/
final class ShapeComponent extends JComponent
{
    private static final long serialVersionUID = 2630737940019331686L;
    
    private final Shape shape;
    private final Color color;
    
    ShapeComponent(Shape shape, Color color)
    {
        this.shape = shape;
        this.color = color;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        paintComponent((Graphics2D)g);
    }
    
    private void paintComponent(Graphics2D g)
    {
        Utilities.setGraphicsToHighQuality(g);
        g.setColor(color);
        g.fill(shape);
    }
}
