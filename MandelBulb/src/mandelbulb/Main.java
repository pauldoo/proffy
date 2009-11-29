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

package mandelbulb;

import javax.swing.JFrame;

public final class Main
{
    private Main()
    {
    }

    private final static class Evaluator implements Runnable
    {
        final RenderComponent renderComponent;

        public Evaluator(RenderComponent renderComponent) {
            this.renderComponent = renderComponent;
        }

        public void run() {
            try {
                OctTree tree = OctTree.createEmpty();
                for (int level = 1; level <= 6; level++) {
                    final int resolution = 2 << level;
                    for (int iz = -resolution; iz < resolution; iz++) {
                        for (int iy = -resolution; iy < resolution; iy++) {
                            for (int ix = -resolution; ix < resolution; ix++) {
                                double x = (ix + 0.5) / resolution;
                                double y = (iy + 0.5) / resolution;
                                double z = (iz + 0.5) / resolution;

                                double t = 0.8 - Math.sqrt(x*x + y*y);
                                boolean inside = (t*t + z*z) <= 0.03;
                                double scale = 0.5 / resolution;
                                tree = tree.repSetRegion(x - scale, y - scale, z - scale, x + scale, y + scale, z + scale, inside);
                            }
                        }
                    }

                    final int nodeCount = tree.nodeCount();
                    System.out.println("Level " + level + ", resolution " + resolution + ", nodeCount " + nodeCount + ", nodeCount/resolution^2 " + (nodeCount / (resolution * resolution)));
                    renderComponent.setSegmentation(tree);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args)
    {
        final RenderComponent renderComponent = new RenderComponent();
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(renderComponent);
        frame.setSize(200, 200);
        frame.setResizable(false);
        frame.setVisible(true);

        new Thread(new Evaluator(renderComponent)).start();
    }
}
