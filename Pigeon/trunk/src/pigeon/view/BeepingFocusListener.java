/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.view;

import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JFormattedTextField;

/**
    Emits a beep when Swing components loose focus and their content
    is invalid in some way.

    Currently only checks JFormattedTextField components.
*/
final class BeepingFocusListener extends FocusAdapter
{
    public void focusLost(FocusEvent e)
    {
        if (e.getSource() instanceof JFormattedTextField) {
            JFormattedTextField source = (JFormattedTextField)e.getSource();
            if (!source.isEditValid()) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
