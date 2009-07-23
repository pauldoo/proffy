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

package sknat.view;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import sknat.model.UnitProperties;

public final class UnitPropertyEditor extends JPanel
{
    private UnitProperties unitProperties;
    private JSlider agilitySlider;
    private JSlider attackSlider;
    private JSlider defenseSlider;
    private JSlider speedSlider;

    public UnitPropertyEditor(UnitProperties initial)
    {
        this.unitProperties = initial;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        agilitySlider = new JSlider(0, 100, 25);
        attackSlider = new JSlider(0, 100, 25);
        defenseSlider = new JSlider(0, 100, 25);
        speedSlider = new JSlider(0, 100, 25);

        this.add(agilitySlider);
        this.add(attackSlider);
        this.add(defenseSlider);
        this.add(speedSlider);
    }
}
