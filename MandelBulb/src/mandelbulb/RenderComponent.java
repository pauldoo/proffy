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

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 *
 * @author pauldoo
 */
public final class RenderComponent extends JComponent {

    @Override
    public final void paintComponent(Graphics g)
    {
        paintComponent((Graphics2D)g);
    }

    private final void paintComponent(Graphics2D g)
    {
        OctTreeRenderer.render(segmentation, g, getWidth() / 2.0);
        repaint();
    }

    public void setSegmentation(OctTree segmentation)
    {
        this.segmentation = segmentation;
        repaint();
    }

    private OctTree segmentation = OctTree.createEmpty().repSetRegion(0.0, 0.0, 0.0, 0.5, 0.5, 0.5, true);
}
