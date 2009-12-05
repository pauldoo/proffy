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

    private final static class Evaluator implements Runnable
    {
        final RenderComponent renderComponent;

        public Evaluator(RenderComponent renderComponent) {
            this.renderComponent = renderComponent;
        }

        private static class Triplex
        {
            public final double x;
            public final double y;
            public final double z;

            public Triplex(double x, double y, double z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }

            static Triplex add(Triplex a, Triplex b)
            {
                return new Triplex(
                        a.x + b.x,
                        a.y + b.y,
                        a.z + b.z);
            }

            static Triplex power(Triplex a, double n)
            {
                final double r = Math.sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
                final double theta = Math.atan2( Math.sqrt(a.x*a.x + a.y*a.y), a.z );
                final double phi = Math.atan2(a.y, a.x);
                return new Triplex(
                        Math.pow(r, n) * Math.sin(theta * n) * Math.cos(phi * n),
                        Math.pow(r, n) * Math.sin(theta * n) * Math.sin(phi * n),
                        Math.pow(r, n) * Math.cos(theta * n));
            }
        }

        private static boolean evaluate(final Triplex c)
        {
            final int maxIter = 50;
            final double n = 8;

            Triplex z = new Triplex(0.0, 0.0, 0.0);
            int i;
            for (i = 0; i < maxIter && (z.x*z.x + z.y*z.y + z.z*z.z) < 100.0; i++) {
                z = Triplex.add(Triplex.power(z, n), c);
            }
            return i == maxIter;
        }

        public void run() {
            try {
                OctTree tree = OctTree.createEmpty();
                for (int level = 1; level <= 7; level++) {
                    final long startTime = System.currentTimeMillis();

                    final int resolution = 2 << level;
                    for (int iz = -resolution; iz < resolution; iz++) {
                        for (int iy = -resolution; iy < resolution; iy++) {
                            for (int ix = -resolution; ix < resolution; ix++) {
                                double x = (ix + 0.5) / resolution;
                                double y = (iy + 0.5) / resolution;
                                double z = (iz + 0.5) / resolution;

                                boolean inside = evaluate(new Triplex(x * 1.5, y * 1.5, z * 1.5));
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
        final RenderComponent renderComponent = new RenderComponent();
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(renderComponent);
        frame.setSize(250, 250);
        frame.setResizable(true);
        frame.setVisible(true);

        new Thread(new Evaluator(renderComponent)).start();
    }
}
