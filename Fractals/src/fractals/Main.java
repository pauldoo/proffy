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

package fractals;

import java.awt.Color;
import javax.swing.JFrame;

public final class Main
{
    private Main()
    {
    }

    private static final class Evaluator implements Runnable
    {
        final OctTreeRendererComponent renderComponent;

        public Evaluator(OctTreeRendererComponent renderComponent) {
            this.renderComponent = renderComponent;
        }

        public void run() {
            try {
                OctTree tree = OctTree.createEmpty();
                for (int level = 1; level <= 8; level++) {
                    final long startTime = System.currentTimeMillis();

                    final int resolution = 2 << level;
                    for (int iz = -resolution; iz < resolution; iz++) {
                        for (int iy = -resolution; iy < resolution; iy++) {
                            for (int ix = -resolution; ix < resolution; ix++) {
                                double x = (ix + 0.5) / resolution;
                                double y = (iy + 0.5) / resolution;
                                double z = (iz + 0.5) / resolution;

                                boolean inside = Mandelbulb.evaluate(new Triplex(x * 1.5, y * 1.5, z * 1.5));
                                double scale = 0.5 / resolution;
                                tree = tree.repSetRegion(x - scale, y - scale, z - scale, x + scale, y + scale, z + scale, inside);
                            }
                        }
                    }

                    final long endTime = System.currentTimeMillis();

                    final int nodeCount = tree.nodeCount();
                    System.out.println("Level " + level + ", resolution " + resolution + ", nodeCount " + nodeCount + ", nodeCount/resolution^2 " + (nodeCount / (resolution * resolution)) + ", time " + (endTime - startTime) + "ms");
                    renderComponent.setSegmentation(tree);
                    Thread.sleep(2000);
                }
                renderComponent.setBackgroundColor(Color.GREEN);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args)
    {
        final OctTreeRendererComponent renderComponent = new OctTreeRendererComponent();
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(renderComponent);
        frame.setSize(250, 250);
        frame.setResizable(true);
        frame.setVisible(true);

        new Thread(new Evaluator(renderComponent)).start();
    }
}
