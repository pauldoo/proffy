/*
    Copyright (C) 2007  Paul Richards.

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

package tuner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
    A swing component that displays a line graph of the freqencies.
*/
final class FrequencyCurve extends JComponent implements SpectralView
{
    private static final long serialVersionUID = 8161190134655084490L;
    
    private double targetFrequency;
    private SinglePickup[] pickups;
    
    public FrequencyCurve()
    {
        this.setDoubleBuffered(true);
        setTargetFrequency(261);
        setMinimumSize(new Dimension(400, 100));
        setPreferredSize(new Dimension(400, 100));
    }

    public synchronized void process(AudioPacket packet)
    {
        for (SinglePickup pickup: pickups) {
            pickup.process(packet);
        }
        repaint();
    }

    @Override
    public synchronized void paint(Graphics graphicsOneDee)
    {
        Graphics2D g = (Graphics2D)graphicsOneDee;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        double maximumResponse = 0;
        for (SinglePickup pickup: pickups) {
            double response = pickup.getResponseMeasure();
            if (response > maximumResponse) {
                maximumResponse = response;
            }
        }
        if (maximumResponse == 0) {
            maximumResponse = 1.0;
        }
        
        g.setBackground(Color.BLACK);

        Rectangle clipBounds = g.getClipBounds();
        g.clearRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

        Dimension size = getSize();
        for (int i = 0; i < pickups.length; i++) {
            double x = (((double)i) / (pickups.length - 1)) * (size.width - 1);
            double y = (1.0 - (pickups[i].getResponseMeasure() / maximumResponse)) * (size.height - 1);
          
            int offsetFromTarget = i - ((pickups.length - 1) / 2);
            if (offsetFromTarget == 0) {
                g.setColor(Color.WHITE);
            } else if (offsetFromTarget % 8 == 0) {
                g.setColor(Color.LIGHT_GRAY);
            } else {
                g.setColor(Color.DARK_GRAY);
            }
            g.fill(new Rectangle2D.Double(x - 1, 0, 2, size.height));
            g.setColor(Color.YELLOW);
            g.fill(new Ellipse2D.Double(x - 7, y - 3, 14, 6));
        }
    }
    
    public synchronized void setTargetFrequency(double frequency)
    {
        System.out.println("setTargetFrequency( " + frequency + " )");
        pickups = new SinglePickup[12 * 2 + 1];
        for (int i = -12; i <= 12; i++) {
            final double semitoneDelta = i / 8.0;
            double pickupFrequency = frequency * Math.pow(2.0, semitoneDelta / 12);
            pickups[i+12] = new SinglePickup(pickupFrequency, 4.0);
        }
        repaint();
    }
}
