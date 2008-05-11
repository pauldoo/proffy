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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
    Implements an Iterated Function System (IFS) fractal where the
    user is able to define the different transforms that are used.
*/
final class IteratedFunctionSystem extends BackgroundRenderingComponent implements DraggableQuadrilateral.Listener
{
    private static final long serialVersionUID = 5488481579716517944L;
    
    public static final int GAP_FROM_EDGE = 60;

    private final JLayeredPane quadrilateralsPane;
    private final List<DraggableQuadrilateral> draggableQuadrilaterals = new ArrayList<DraggableQuadrilateral>();
    
    static JComponent createView()
    {
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        
        JLayeredPane panel = new JLayeredPane();
        JLayeredPane nestedPanel = new JLayeredPane();
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panel.setLayout(new EverythingGetsOverlayedLayout());
        nestedPanel.setLayout(new EverythingGetsOverlayedLayout());
        IteratedFunctionSystem ifs = new IteratedFunctionSystem(nestedPanel);
        panel.add(ifs, JLayeredPane.DEFAULT_LAYER);
        panel.add(nestedPanel, JLayeredPane.PALETTE_LAYER);
        root.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = ifs.createButtonPanel();
        root.add(buttonPanel, BorderLayout.NORTH);

        ifs.removeAllDraggableQuadrilaterals();
        
        return root;
    }

    IteratedFunctionSystem(JLayeredPane containerPane)
    {
        this.quadrilateralsPane = containerPane;
        setOpaque(false);
    }
    
    private JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel();
        LayoutManager layout = new FlowLayout();
        buttonPanel.setLayout(layout);
        
        {
            JButton addButton = new JButton("Add new transform");
            addButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    addDraggableQuadrilateral();
                }
            });
            buttonPanel.add(addButton);
        }
        
        {
            JButton resetButton = new JButton("Reset applet");
            resetButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    removeAllDraggableQuadrilaterals();
                }
            });
            buttonPanel.add(resetButton);
        }
        return buttonPanel;
    }
    
    private void addDraggableQuadrilateral()
    {
        stopBackgroundThread();
        DraggableQuadrilateral quad = new DraggableQuadrilateral();
        quad.addListener(this);
        quadrilateralsPane.add(quad);
        quadrilateralsPane.doLayout();
        draggableQuadrilaterals.add(quad);
        rerender();
    }
    
    private void removeAllDraggableQuadrilaterals()
    {
        stopBackgroundThread();
        quadrilateralsPane.removeAll();
        draggableQuadrilaterals.clear();
        addDraggableQuadrilateral();
        rerender();
    }
    
    protected void render(Graphics2D g) throws InterruptedException
    {
        Utilities.setGraphicsToHighQuality(g);

        g.setColor(Color.BLACK);
        final double width = getWidth();
        final double height = getHeight();

        {
            Shape border = new Rectangle2D.Double(GAP_FROM_EDGE, GAP_FROM_EDGE, width - GAP_FROM_EDGE*2, height - GAP_FROM_EDGE*2);
            g.setStroke(new BasicStroke());
            g.draw(border);
        }
        super.bufferIsNowOkayToBlit();

        if (draggableQuadrilaterals.isEmpty()) {
            return;
        }
        
        Random generator = new Random();
        Point2D.Double point = new Point2D.Double(generator.nextDouble() * width, generator.nextDouble() * height);

        for (int j = 0; j < 1000; j++) {
            for (int i = 0; i < 1000; i++) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                int quadIndex = generator.nextInt(draggableQuadrilaterals.size());
                DraggableQuadrilateral quad = draggableQuadrilaterals.get(quadIndex);
                if (quad.getShape().contains(generator.nextDouble() * (width - GAP_FROM_EDGE*2) + GAP_FROM_EDGE, generator.nextDouble() * (height - GAP_FROM_EDGE*2) + GAP_FROM_EDGE) == false) {
                    i--;
                    continue;
                }
                Point2D.Double cornerA = quad.getCornerA();
                Point2D.Double cornerB = quad.getCornerB();
                Point2D.Double cornerC = quad.getCornerC();
                Point2D.Double cornerD = quad.getCornerD();

                point.x = (point.x - GAP_FROM_EDGE) / (width - GAP_FROM_EDGE*2);
                point.y = (point.y - GAP_FROM_EDGE) / (height - GAP_FROM_EDGE*2);
                double weightA = (1.0 - point.x) * (1.0 - point.y);
                double weightB = (point.x) * (1.0 - point.y);
                double weightC = (point.x) * (point.y);
                double weightD = (1.0 - point.x) * (point.y);
                point = new Point2D.Double(
                        weightA * cornerA.x + weightB * cornerB.x + weightC * cornerC.x + weightD * cornerD.x,
                        weightA * cornerA.y + weightB * cornerB.y + weightC * cornerC.y + weightD * cornerD.y);

                if (i >= 50) {
                    Shape shape = new Ellipse2D.Double(point.x - 0.25, point.y - 0.25, 0.5, 0.5);
                    g.fill(shape);
                }
            }
        }
    }

    public void draggableQuadrilateralHasMoved(DraggableQuadrilateral source)
    {
        this.rerender();
    }
}
