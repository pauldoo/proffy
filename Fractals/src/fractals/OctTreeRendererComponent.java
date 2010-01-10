/*
    Copyright (C) 2009, 2010  Paul Richards.

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
import java.awt.geom.AffineTransform;

/**
*/
final class OctTreeRendererComponent extends BackgroundRenderingComponent {

    private static final long serialVersionUID = 4417921502019642371L;

    private OctTree segmentation = OctTree.createEmpty().repSetRegion(0.0, 0.0, 0.0, 0.5, 0.5, 0.5, true);
    private Color backgroundColor = Color.RED;
    private static final int superSample = 2;
    private static final int subSample = 4;

    public OctTreeRendererComponent() {
        super(superSample);
    }

    @Override
    protected void render(Graphics2D g) throws InterruptedException {
        Utilities.setGraphicsToLowQuality(g);

        AffineTransform originalTransform = g.getTransform();
        final double theta = 2.5;//System.currentTimeMillis() * 0.0001;

        for (int downscale = subSample * superSample; downscale >= 1; downscale /= 2) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            g.setTransform(originalTransform);
            g.transform(AffineTransform.getScaleInstance(downscale, downscale));
            doRender(
                    segmentation,
                    theta,
                    g,
                    super.getSupersampledWidth() / downscale,
                    super.getSupersampledHeight() / downscale,
                    Math.min(super.getSupersampledWidth(), super.getSupersampledHeight()) / 2.0 / downscale,
                    backgroundColor);

            super.bufferIsNowOkayToBlit();
        }

        g.setTransform(originalTransform);
    }

    public void setSegmentation(OctTree segmentation)
    {
        this.segmentation = segmentation;
        super.rerender();
    }

    public void setBackgroundColor(Color color)
    {
        this.backgroundColor = color;
        super.rerender();
    }


    private static void doRender(
            final OctTree segmentation,
            final double theta,
            final Graphics g,
            final int width,
            final int height,
            final double size,
            final Color backgroundColor) throws InterruptedException
    {
        for (int iy = 0; iy < height; iy++) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            for (int ix = 0; ix < width; ix++) {
                final double tx = 0.0;
                final double ty = 0.0;
                final double tz = -1.5;
                final double tdx = (ix - width/2.0) / size;
                final double tdy = (iy - height/2.0) / size;
                final double tdz = 1.0;
                final double x = Math.cos(theta) * tx - Math.sin(theta) * tz;
                final double y = ty;
                final double z = Math.sin(theta) * tx + Math.cos(theta) * tz;
                final double dx = Math.cos(theta) * tdx - Math.sin(theta) * tdz;
                final double dy = tdy;
                final double dz = Math.sin(theta) * tdx + Math.cos(theta) * tdz;
                final double result = segmentation.firstHit(x, y, z, dx, dy, dz);

                if (Double.isNaN(result)) {
                    g.setColor(backgroundColor);
                } else {
                    final Triplex position = new Triplex(
                        x + result * dx,
                        y + result * dy,
                        z + result * dz);
                    final Triplex normal = Mandelbulb.computeNormal(position, Mandelbulb.maxIterations-2);
                    final double shade = Math.max(normal.x * 0.0 + normal.y * -0.5 + normal.z * 0.5, 0.0);
                    final Color color = new Color((float) (shade), (float) (shade), (float) (shade));
                    g.setColor(color);
                }
                g.fillRect(ix, iy, 1, 1);

            }
        }
    }


}
