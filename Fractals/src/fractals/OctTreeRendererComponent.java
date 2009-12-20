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
        final double theta = System.currentTimeMillis() * 0.0001;

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
        double[][] distances = new double[height][];
        for (int iy = 0; iy < height; iy++) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            distances[iy] = new double[width];

            projectLine(width, height, size, iy, theta, segmentation, distances);
            if (iy >= 2) {
                shadeLine(width, distances, iy - 1, g, backgroundColor, size);
                distances[iy - 2] = null;
            }
        }
    }

    private static void projectLine(final int width, final int height, final double size, int iy, final double theta, final OctTree segmentation, double[][] distances) {
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
            distances[iy][ix] = result;
        }
    }

    private static double lookupValue(double[][] distances, int x, int y, int dx, int dy)
    {
        double result = distances[y + dy][x + dx];
        if (Double.isNaN(result)) {
            result = distances[y][x];
        }
        return result;
    }

    private static void shadeLine(final int width, double[][] distances, int iy, final Graphics g, final Color backgroundColor, final double size) {
        for (int ix = 1; ix < width - 1; ix++) {
            final double z = distances[iy][ix];
            if (Double.isNaN(z)) {
                g.setColor(backgroundColor);
            } else {
                final double dzdx = (lookupValue(distances, ix, iy, 1, 0) - lookupValue(distances, ix, iy, -1, 0)) / 2.0 * size / z;
                final double dzdy = (lookupValue(distances, ix, iy, 0, 1) - lookupValue(distances, ix, iy, 0, -1)) / 2.0 * size / z;
                final double shade = 1.0 / Math.sqrt(1.0 + dzdx * dzdx + dzdy * dzdy);
                final Color color = new Color((float) (shade*shade), (float) (shade*shade), (float) (shade));
                g.setColor(color);
            }
            g.fillRect(ix, iy, 1, 1);
        }
    }

}
