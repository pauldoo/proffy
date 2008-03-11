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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
    A layout manager that simply forces all components to the same bounds
    and to completely fill the parent container.
*/
final class EverythingGetsOverlayedLayout implements LayoutManager
{

    public void addLayoutComponent(String name, Component comp)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeLayoutComponent(Component comp)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Dimension preferredLayoutSize(Container parent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Dimension minimumLayoutSize(Container parent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void layoutContainer(Container parent)
    {
        for (Component c: parent.getComponents()) {
            c.setBounds(parent.getBounds());
        }
    }
}