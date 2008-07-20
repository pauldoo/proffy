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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

final class BifurcationDiagram extends BackgroundRenderingComponent
{
    private static final long serialVersionUID = 6987190714076485299L;
    
    private double[] controlPointValues = defaultControlPointValues();
    
    private BifurcationDiagram()
    {
        super(2);
    }
    
    static JComponent createComponent()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        BifurcationDiagram component = new BifurcationDiagram();
        panel.add(component, BorderLayout.CENTER);
        panel.add(component.createEditCurveButton(), BorderLayout.SOUTH);
                
        return panel;
    }
    
    private JButton createEditCurveButton()
    {
        JButton result = new JButton("Edit Function");
        result.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                editCurve();
            }
        });
        return result;
    }
    
    private void editCurve()
    {
        CurveEditor editor = new CurveEditor(Utilities.copyDoubleArray(controlPointValues));
        final int result = JOptionPane.showOptionDialog(this, editor.asComponent(), "Edit the function", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            //JOptionPane.showMessageDialog(this, editor.asComponent());
            this.controlPointValues = editor.getControlPointValues();
            super.rerender();
        }
    }

    @Override
    protected void render(Graphics2D g) throws InterruptedException
    {
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getSupersampledWidth(), getSupersampledHeight());
        super.bufferIsNowOkayToBlit();
        g.setColor(Color.WHITE);
        
        final InterpolatingCubicSpline spline = new InterpolatingCubicSpline(controlPointValues);
        for (int xInt = 0; xInt < getSupersampledWidth(); xInt++) {
            final double x = ((xInt + 0.5) / getSupersampledWidth()) + 0.5;
            double y = 0.5;
            for (int i = 0; i < 1000; i++) {
                y = x * spline.sample(y * controlPointValues.length);
                if (i > 100) {
                    final int yInt = (int)Math.round(y * getSupersampledHeight());
                    if (yInt >= 0 && yInt < getSupersampledHeight()) {
                        g.fillRect(xInt, yInt, 1, 1);
                    }
                }
            }
        }
    }
    
    private static double[] defaultControlPointValues()
    {
        double[] result = new double[20];
        for (int i = 0; i < result.length; i++) {
            final double x = (i + 0.5) / result.length;
            final double y = x * (1 - x);
            result[i] = y * 4;
        }
        return result;
    }
}
