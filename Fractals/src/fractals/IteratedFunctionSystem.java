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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
    Implements an Iterated Function System (IFS) fractal where the
    user is able to define the different transforms that are used.
*/
final class IteratedFunctionSystem extends BackgroundRenderingComponent implements DraggableQuadrilateral.Listener
{
    private static final long serialVersionUID = 5488481579716517944L;

    private final List<DraggableQuadrilateral> draggableQuadrilaterals = new ArrayList<DraggableQuadrilateral>();
    
    static JComponent createView()
    {
        JLayeredPane panel = new JLayeredPane();
        panel.setLayout(new EverythingGetsOverlayedLayout());
        IteratedFunctionSystem ifs = new IteratedFunctionSystem();
        DraggableQuadrilateral quadA = new DraggableQuadrilateral();
        DraggableQuadrilateral quadB = new DraggableQuadrilateral();
        DraggableQuadrilateral quadC = new DraggableQuadrilateral();
        ifs.addDraggableQuadrilateral(quadA);
        ifs.addDraggableQuadrilateral(quadB);
        ifs.addDraggableQuadrilateral(quadC);
        panel.add(quadA);
        panel.add(quadB);
        panel.add(quadC);
        panel.add(ifs);
        return panel;
    }

    IteratedFunctionSystem()
    {
        setOpaque(false);
    }
    
    void addDraggableQuadrilateral(DraggableQuadrilateral quad)
    {
        quad.addListener(this);
        draggableQuadrilaterals.add(quad);
        repaint();
    }
    
    protected void render(Graphics2D g) throws InterruptedException
    {
        Utilities.setGraphicsToHighQuality(g);

        g.setColor(Color.BLACK);
        g.setBackground(Color.LIGHT_GRAY);
        final double width = getBounds().width;
        final double height = getBounds().height;

        {
            Shape border = new Rectangle2D.Double(80, 60, width - 160, height - 120);
            g.setStroke(new BasicStroke());
            g.draw(border);
        }

        Random generator = new Random();
        Point2D.Double point = new Point2D.Double(generator.nextDouble() * width, generator.nextDouble() * height);

        for (int i = 0; i < 100000; i++) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            int quadIndex = generator.nextInt(draggableQuadrilaterals.size());
            DraggableQuadrilateral quad = draggableQuadrilaterals.get(quadIndex);
            if (quad.getShape().contains(generator.nextDouble() * (width - 160) + 80, generator.nextDouble() * (height - 120) + 60) == false) {
                i--;
                continue;
            }
            Point2D.Double cornerA = quad.getCornerA();
            Point2D.Double cornerB = quad.getCornerB();
            Point2D.Double cornerC = quad.getCornerC();
            Point2D.Double cornerD = quad.getCornerD();

            point.x = (point.x - 80) / (width - 160);
            point.y = (point.y - 60) / (height - 120);
            double weightA = (1.0 - point.x) * (1.0 - point.y);
            double weightB = (point.x) * (1.0 - point.y);
            double weightC = (point.x) * (point.y);
            double weightD = (1.0 - point.x) * (point.y);
            point = new Point2D.Double(
                    weightA * cornerA.x + weightB * cornerB.x + weightC * cornerC.x + weightD * cornerD.x,
                    weightA * cornerA.y + weightB * cornerB.y + weightC * cornerC.y + weightD * cornerD.y);

            if (i >= 100) {
                Shape shape = new Ellipse2D.Double(point.x - 0.25, point.y - 0.25, 0.5, 0.5);
                g.fill(shape);
            }
        }
    }

    public void draggableQuadrilateralHasMoved(DraggableQuadrilateral source)
    {
        this.rerender();
    }
}
