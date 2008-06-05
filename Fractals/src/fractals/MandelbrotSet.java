/*
    Copyright (C) 2007, 2008  Paul Richards.

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
    Mandelbrot and Julia sets, implemented by thinking about them both as
    different cuts through the same 4D volume.
*/
final class MandelbrotSet
{
    private final Vector4 origin;
    private final Vector4 dx;
    private final Vector4 dy;
    
    MandelbrotSet(Vector4 origin, Vector4 dx, Vector4 dy) {
        this.origin = origin;
        this.dx = dx;
        this.dy = dy;
    }
    
    private static TileProvider<IntegerTile> createMandelbrotSet(int maxIterations)
    {
        return new MandelbrotSetTileProvider(new MandelbrotSet(
                new Vector4(0, 0, 0, 0),
                new Vector4(0, 0, 1, 0),
                new Vector4(0, 0, 0, 1)), maxIterations);
    }
    
    static TileProvider<IntegerTile> createJuliaSet(int maxIterations, Complex constant)
    {
        return new MandelbrotSetTileProvider(new MandelbrotSet(
                new Vector4(0, 0, constant.getReal(), constant.getImaginary()),
                new Vector4(1, 0, 0, 0),
                new Vector4(0, 1, 0, 0)), maxIterations);
    }
    
    static JComponent createView()
    {
        TileProvider<RenderableTile> source = new RenderFilter(MandelbrotSet.createMandelbrotSet(1000), 0.02);
        CanvasView view = new CanvasView(800, 600, source);
        view.startAllThreads();
        return view;
    }
    
    static JComponent createMandelbrot4dView()
    {
        JPanel result = new JPanel();
        result.setLayout(new BorderLayout());
        
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new GridLayout(2, 3));
        
        final MandelbrotSlice[] components = new MandelbrotSlice[]{
                new MandelbrotSlice(new Vector4(1, 0, 0, 0), new Vector4(0, 1, 0, 0)),
                new MandelbrotSlice(new Vector4(1, 0, 0, 0), new Vector4(0, 0, 1, 0)),
                new MandelbrotSlice(new Vector4(1, 0, 0, 0), new Vector4(0, 0, 0, 1)),
                new MandelbrotSlice(new Vector4(0, 1, 0, 0), new Vector4(0, 0, 1, 0)),
                new MandelbrotSlice(new Vector4(0, 1, 0, 0), new Vector4(0, 0, 0, 1)),
                new MandelbrotSlice(new Vector4(0, 0, 1, 0), new Vector4(0, 0, 0, 1))
        };
        for (MandelbrotSlice component: components) {
            viewPanel.add(component);
        }
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 4));
        
        final JSlider[] sliders = new JSlider[]{
            new JSlider(JSlider.VERTICAL, 0, 1000, 500),
            new JSlider(JSlider.VERTICAL, 0, 1000, 500),
            new JSlider(JSlider.VERTICAL, 0, 1000, 500),
            new JSlider(JSlider.VERTICAL, 0, 1000, 500)
        };
        for (JSlider slider: sliders) {
            controlPanel.add(slider);
            slider.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e) {
                    JSlider slider = (JSlider)e.getSource();
                    double sliderValue = ((slider.getValue() + 0.5) / 1000) * 2 - 1;
                    Vector4 origin = components[0].getOrigin();
                    if (slider == sliders[0]) {
                        origin = new Vector4(sliderValue, origin.getB(), origin.getC(), origin.getD());
                    } else if (slider == sliders[1]) {
                        origin = new Vector4(origin.getA(), sliderValue, origin.getC(), origin.getD());
                    } else if (slider == sliders[2]) {
                        origin = new Vector4(origin.getA(), origin.getB(), sliderValue, origin.getD());
                    } else if (slider == sliders[3]) {
                        origin = new Vector4(origin.getA(), origin.getB(), origin.getC(), sliderValue);
                    } else {
                        throw new RuntimeException("Unreachable!");
                    }
                    for (MandelbrotSlice component: components) {
                        component.setOrigin(origin);
                    }
                }
            });
        }        
                
        result.add(viewPanel, BorderLayout.CENTER);
        result.add(controlPanel, BorderLayout.EAST);
        
        return result;
    }

    int iterateUntilEscapes(double x, double y, int maxIterations)
    {
        Vector4 p = origin.add(dx.multiply(x)).add(dy.multiply(y));
        return iterate4dSequence(p, maxIterations);
    }    
   
    static int iterate4dSequence(
            final Vector4 p,
            final int maxIterations)
    {
        Complex z = new Complex(p.getA(), p.getB());
        final Complex constant = new Complex(p.getC(), p.getD());
        
        int v;
        for (v = 0; v < maxIterations && z.magnitudeSquared() <= 4; v++) {
            Complex.multiplyReplace(z, z);
            Complex.addReplace(z, constant);
        }
        return v % maxIterations;
    }
    
    static BufferedImage quickMandelbrotRender(Complex min, Complex max, Dimension imageSize)
    {
        BufferedImage result = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_BYTE_GRAY);
        MandelbrotSet me = new MandelbrotSet(
                new Vector4(0, 0, 0, 0),
                new Vector4(0, 0, 1, 0),
                new Vector4(0, 0, 0, 1));
        for (int y = 0; y < imageSize.height; y++) {
            final double cI = ((y + 0.5) / imageSize.height) * (max.getImaginary() - min.getImaginary()) + min.getImaginary();
            for (int x = 0; x < imageSize.width; x++) {
                final double cR = ((x + 0.5) / imageSize.width) * (max.getReal() - min.getReal()) + min.getReal();
                int v = me.iterateUntilEscapes(cR, cI, 32);
                int rgb = (v << 3) | (v << 11) | (v << 19); 
                result.setRGB(x, y, rgb);
            }
        }
        return result;
    }      
}

final class MandelbrotSetTileProvider implements TileProvider<IntegerTile>
{
    private final MandelbrotSet source;
    private final int maxIterations;
    
    MandelbrotSetTileProvider(MandelbrotSet source, int maxIterations)
    {
        this.source = source;
        this.maxIterations = maxIterations;
    }
    
    public IntegerTile getTile(TilePosition pos)
    {
        IntegerTile tile = new IntegerTile(pos);
        for (int iy = pos.getMinY(); iy <= pos.getMaxY(); iy++) {
            for (int ix = pos.getMinX(); ix <= pos.getMaxX(); ix++) {
                final double r = ix * pos.relativeScale() / 200 - 2.5;
                final double i = iy * pos.relativeScale() / 200 - 1.5;
                int v = source.iterateUntilEscapes(r, i, maxIterations);
                tile.setValue(ix, iy, v);
            }
        }
        return tile;
    }
}

final class MandelbrotSlice extends BackgroundRenderingComponent
{
    private static final long serialVersionUID = 1931567046579817883L;
    
    private Vector4 origin;
    private final Vector4 dx;
    private final Vector4 dy;
    
    MandelbrotSlice(Vector4 dx, Vector4 dy) {
        this.origin = new Vector4(0, 0, 0, 0);
        this.dx = dx;
        this.dy = dy;
    }
    
    @Override
    protected void render(Graphics2D g) throws InterruptedException
    {
        double width = getWidth();
        double height = getHeight();
        final MandelbrotSet source = new MandelbrotSet(origin, dx, dy);
        for (int y = 0; y < height; y+=10) {
            for (int x = 0; x < width; x+=10) {
                double xAsFraction = ((x + 0.5) / width) * 4 - 2;
                double yAsFraction = ((y + 0.5) / height) * 4 - 2;
                int count = source.iterateUntilEscapes(xAsFraction, yAsFraction, 32);
                float v = (float)Utilities.expose(count, 0.1);
                g.setColor(new Color(v, v, v));
                g.fillRect(x, y, 10, 10);
                //Thread.sleep(1);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        }
    }
    
    Vector4 getOrigin()
    {
        return this.origin;
    }
    
    void setOrigin(Vector4 origin)
    {
        this.origin = origin;
        super.rerender();
    }
}
