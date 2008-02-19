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

package fractals;

import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public final class MainApplet extends JApplet
{
    private static final long serialVersionUID = 1011945883208164505L;
    
    @Override
    public void start()
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
        if (true) {
            return new BackwardsIterationJuliaView();
        }

        TileProvider<RenderableTile> source = null;
        
        String fractalType = parameters.get("FractalType");
        if (fractalType.equals("MandelbrotSet")) {
            source = new RenderFilter(new MandelbrotSet(1000), 0.02);
        } else if (fractalType.equals("JuliaSet")) {
            source = new RenderFilter(new JuliaSet(-0.726895347709114071439, 0.188887129043845954792), 0.01);
        } else {
            throw new IllegalArgumentException("Unknown fractal type: " + fractalType);
        }
            
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JLabel statusLabel = new JLabel();
        statusPanel.add(statusLabel);

        CanvasView view = new CanvasView(800, 600, source, statusLabel);

        panel.add(view, BorderLayout.CENTER);
        //panel.add(statusPanel, BorderLayout.SOUTH);
        view.startUpdateThread();
        view.startRenderingThreads();        
        return panel;
    }
    
    public static void main(String[] args)
    {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("FractalType", "MandelbrotSet");
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(createMainComponent(parameters));
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
