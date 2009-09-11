/*
    Copyright (C) 2009  Paul Richards.

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

package sknat.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import sknat.model.UnitProperties;

/**
    Simple Swing widget that renders a single unit scaled to fit.
*/
public final class UnitView extends JComponent
{
    private UnitProperties unitProperties = null;

    public UnitView()
    {
        setPreferredSize(new Dimension(200, 200));
    }

    public void setUnitProperties(UnitProperties properties)
    {
        this.unitProperties = properties;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        paintComponentImpl((Graphics2D)g);
    }

    private void paintComponentImpl(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


        g.setBackground(Color.BLACK);
        Rectangle clipBounds = g.getClipBounds();
        g.clearRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

        g.setColor(Color.red);
        if (unitProperties == null) {
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        } else {
            final Stroke defaultStroke = g.getStroke();
            {
                g.translate(getWidth() * 0.5, getHeight() * 0.5);
                final double scale = Math.min(getWidth(), getHeight()) * 0.5 * 0.5;
                g.scale(scale, scale);
                g.rotate(System.currentTimeMillis() * 0.001 * 0.2);
            }

            {
                g.setStroke(new BasicStroke((float)(unitProperties.defense * 0.24 + 0.01)));
                Shape body = new Ellipse2D.Double(-1.0, -1.0, 2.0, 2.0);
                g.draw(body);
            }

            {
                g.setStroke(new BasicStroke((float)(unitProperties.attack * 0.5 + 0.01)));
                Shape gun = new Line2D.Double(0.25, 0.0, 1.25, 0.0);
                g.draw(gun);
            }

            {
                g.setStroke(new BasicStroke(0.01f));
                Shape engine = new Arc2D.Double(-1.0, -1.0, 2.0, 2.0, 0.0, unitProperties.agility * 360, Arc2D.OPEN);
                g.draw(engine);
            }
        }

        repaint();
    }
}
