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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.event.MouseInputListener;

/**
    Renders an OctTree in 3D.
*/
final class OctTreeRendererComponent extends BackgroundRenderingComponent implements MouseInputListener
{

    private static final long serialVersionUID = 4417921502019642371L;

    private OctTree segmentation = OctTree.createEmpty().repSetRegion(0.0, 0.0, 0.0, 0.5, 0.5, 0.5, true);
    private Color backgroundColor = Color.RED;
    private final NormalProvider normalProvider;
    private Camera3D camera = new Camera3D(new Triplex(0.0, 0.0, -1.5), Quaternion.identityRotation());
    private Point previousDragPoint = null;
    private double shiftDistance = Double.NaN;

    private static final int superSample = 2;
    private static final int subSample = 16;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (previousDragPoint != null) {
            final Point currentDragPoint = e.getPoint();
            final int width = getWidth();
            final int height = getHeight();
            final double halfSize = Math.max(width, height) / 2.0;
            final double x1 = (previousDragPoint.x - (width / 2.0)) / halfSize;
            final double y1 = (previousDragPoint.y - (height / 2.0)) / halfSize;
            final double x2 = (currentDragPoint.x - (width / 2.0)) / halfSize;
            final double y2 = (currentDragPoint.y - (height / 2.0)) / halfSize;
            final Matrix invertedProjectionMatrix = Matrix.invert4x4(camera.toProjectionMatrix());
            final Triplex previous = recoverDirectionVector(invertedProjectionMatrix, x1, y1).normalize();
            final Triplex current = recoverDirectionVector(invertedProjectionMatrix, x2, y2).normalize();

            previousDragPoint = currentDragPoint;
            if (Double.isNaN(shiftDistance) == false) {
                final Triplex displacement = Triplex.multiply(Triplex.subtract(current, previous).negate(), shiftDistance);
                camera = camera.replicateAddShift(displacement);
            } else {
                final Triplex axis = Triplex.crossProduct(previous, current).normalize();
                final double angle = Math.acos(Triplex.dotProduct(previous, current));
                Quaternion update = Quaternion.createRotation(axis, angle);
                camera = camera.replicateAddRotation(update);
            }
            super.rerender();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        previousDragPoint = e.getPoint();
        if (e.isShiftDown()) {
            final Point currentDragPoint = e.getPoint();
            final int width = getWidth();
            final int height = getHeight();
            final double halfSize = Math.max(width, height) / 2.0;
            final double x1 = (previousDragPoint.x - (width / 2.0)) / halfSize;
            final double y1 = (previousDragPoint.y - (height / 2.0)) / halfSize;

            final Matrix invertedProjectionMatrix = Matrix.invert4x4(camera.toProjectionMatrix());
            final Triplex cameraCenter = recoverCameraCenter(invertedProjectionMatrix);

            final Triplex rayVector = recoverDirectionVector(invertedProjectionMatrix, x1, y1).normalize();
            shiftDistance = segmentation.firstHit(
                cameraCenter.x,
                cameraCenter.y,
                cameraCenter.z,
                rayVector.x,
                rayVector.y,
                rayVector.z);
        } else {
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        previousDragPoint = null;
        shiftDistance = Double.NaN;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
        Callback interface used by OctTreeRendererComponent when
        the surface normal at a point is needed.

        This normal could be estimated using the structure of the OctTree
        segmentation, but in some cases the normal can be computed
        analytically by some other means.
    */
    static interface NormalProvider
    {
        /**
            Should calculate the surface normal for the
            given point (which will only roughly be on the surface).

            The returned value should be normalized.
        */
        Triplex normalAtPosition(Triplex p);
    }

    public OctTreeRendererComponent(final NormalProvider normalProvider) {
        super(superSample);
        this.normalProvider = normalProvider;
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    @Override
    protected void render(Graphics2D g) throws InterruptedException {
        Utilities.setGraphicsToLowQuality(g);

        AffineTransform originalTransform = g.getTransform();
        final double theta = 2.5;//System.currentTimeMillis() * 0.0001;

        final Matrix projectionMatrix = camera.toProjectionMatrix();

        for (int downscale = subSample * superSample; downscale >= 1; downscale /= 2) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            g.setTransform(originalTransform);
            g.transform(AffineTransform.getScaleInstance(downscale, downscale));
            doRender(
                    segmentation,
                    normalProvider,
                    projectionMatrix,
                    g,
                    super.getSupersampledWidth() / downscale,
                    super.getSupersampledHeight() / downscale,
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

    private static Triplex recoverCameraCenter(
            Matrix invertedProjectionMatrix)
    {
        return Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(0.0, 0.0, 1.0, 0.0)).toTriplex();
    }

    /**
        Given a coordinate in screen space (typically [(-1, -1), (1, 1)]),
        returns the direction of the ray in world space.
    */
    private static Triplex recoverDirectionVector(
            Matrix invertedProjectionMatrix,
            double x,
            double y)
    {
        Triplex position = Matrix.multiply(invertedProjectionMatrix, Matrix.create4x1(x, y, 1.0, 1.0)).toTriplex();
        return Triplex.subtract(position, recoverCameraCenter(invertedProjectionMatrix));
    }

    private static void doRender(
            final OctTree segmentation,
            final NormalProvider normalProvider,
            final Matrix projectionMatrix,
            final Graphics g,
            final int width,
            final int height,
            final Color backgroundColor) throws InterruptedException
    {
        final Matrix invertedProjectionMatrix = Matrix.invert4x4(projectionMatrix);
        final Triplex cameraCenter = recoverCameraCenter(invertedProjectionMatrix);
        final double halfSize = Math.max(width, height) / 2.0;
        final Triplex lightDirection = recoverDirectionVector(invertedProjectionMatrix, 0.0, 0.5).negate().normalize();

        for (int iy = 0; iy < height; iy++) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            for (int ix = 0; ix < width; ix++) {
                final double sx = (ix - width/2.0) / halfSize;
                final double sy = (iy - height/2.0) / halfSize;
                final Triplex rayVector = recoverDirectionVector(invertedProjectionMatrix, sx, sy);

                final double result = segmentation.firstHit(
                        cameraCenter.x,
                        cameraCenter.y,
                        cameraCenter.z,
                        rayVector.x,
                        rayVector.y,
                        rayVector.z);

                if (Double.isNaN(result)) {
                    g.setColor(backgroundColor);
                } else {
                    try {
                        final Triplex position = Triplex.add(cameraCenter, Triplex.multiply(rayVector, result));
                        final Triplex normal = normalProvider.normalAtPosition(position);
                        final double shade = Math.max(Triplex.dotProduct(normal, lightDirection), 0.0);
                        final Color color = new Color((float) (shade), (float) (shade), (float) (shade));
                        g.setColor(color);
                    } catch (NotANumberException ex) {
                        g.setColor(Color.BLUE);
                    }
                }
                g.fillRect(ix, iy, 1, 1);
            }
        }
    }


}

