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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class MainApplet extends JApplet
{
    private static final long serialVersionUID = 1011945883208164505L;
    
    @Override
    public void init()
    {
        super.start();
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("FractalType", this.getParameter("FractalType"));
        
            this.getContentPane().add(createMainComponent(parameters));
        } catch (Exception e) {
            StringWriter message = new StringWriter();
            e.printStackTrace(new PrintWriter(message));
            JOptionPane.showInternalMessageDialog(this.getContentPane(), message.toString(), e.toString(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static JComponent createMainComponent(Map<String, String> parameters)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String fractalType = parameters.get("FractalType");
        if (fractalType.equals("MandelbrotSet")) {
            panel.add(MandelbrotSet.createView(), BorderLayout.CENTER);
        } else if (fractalType.equals("JuliaSet")) {
            panel.add(BackwardsIterationJuliaView.createView(), BorderLayout.CENTER);
        } else if (fractalType.equals("IteratedFunctionSystem")) {
            panel.add(IteratedFunctionSystem.createView(), BorderLayout.CENTER);
        } else if (fractalType.equals("DiffusionLimitedAggregation")) {
            panel.add(DiffusionLimitedAggregation.createView(), BorderLayout.CENTER);
        } else if (fractalType.equals("Mandelbrot4D")) {
            panel.add(MandelbrotSet.createMandelbrot4dView(), BorderLayout.CENTER);
        } else if (fractalType.equals("NewtonRaphson")) {
            panel.add(NewtonRaphson.createView(), BorderLayout.CENTER);
        } else if (fractalType.equals("BifurcationDiagram")) {
            panel.add(BifurcationDiagram.createComponent(), BorderLayout.CENTER);
        } else {            
            throw new IllegalArgumentException("Unknown fractal type: " + fractalType);
        }

        return panel;
    }
    
    public static void main(String[] args)
    {
        Map<String, String> parameters = new HashMap<String, String>();
        //parameters.put("FractalType", "MandelbrotSet");
        //parameters.put("FractalType", "JuliaSet");
        //parameters.put("FractalType", "IteratedFunctionSystem");
        //parameters.put("FractalType", "DiffusionLimitedAggregation");
        //parameters.put("FractalType", "Mandelbrot4D");
        //parameters.put("FractalType", "NewtonRaphson");
        parameters.put("FractalType", "BifurcationDiagram");
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(createMainComponent(parameters));
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
