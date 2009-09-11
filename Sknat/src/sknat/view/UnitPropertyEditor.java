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

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sknat.model.UnitProperties;

public final class UnitPropertyEditor extends JPanel implements ChangeListener
{
    private UnitProperties unitProperties;
    private final JSlider agilitySlider;
    private final JSlider attackSlider;
    private final JSlider defenseSlider;
    private final JSlider speedSlider;
    private final UnitView unitView;

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == agilitySlider ||
            e.getSource() == attackSlider ||
            e.getSource() == defenseSlider ||
            e.getSource() == speedSlider) {

            setUnitProperties(constructFromSliderStates());
        } else {
            throw new IllegalArgumentException("Unrecognised source");
        }
    }

    private void setUnitProperties(UnitProperties properties)
    {
        this.unitProperties = properties;
        if (unitProperties != null) {
            agilitySlider.setValue((int)Math.floor(properties.agility * 100));
            attackSlider.setValue((int)Math.floor(properties.attack * 100));
            defenseSlider.setValue((int)Math.floor(properties.defense * 100));
            speedSlider.setValue((int)Math.floor(properties.speed * 100));
        }
        unitView.setUnitProperties(properties);
    }

    /**
        Attempts to construct a new UnitProperties instance given the slider
        values.  Returns null if the sliders are set too high to make a valid
        UnitProperties instance.
    */
    private UnitProperties constructFromSliderStates()
    {
        double agility = agilitySlider.getValue() / 100.0;
        double attack = attackSlider.getValue() / 100.0;
        double defense = defenseSlider.getValue() / 100.0;
        double speed = speedSlider.getValue() / 100.0;
        if (UnitProperties.isValid(agility, attack, defense, speed)) {
            return new UnitProperties(agility, attack, defense, speed);
        } else {
            return null;
        }
    }

    public UnitPropertyEditor(UnitProperties initial)
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        agilitySlider = new JSlider(0, 100, 0);
        attackSlider = new JSlider(0, 100, 0);
        defenseSlider = new JSlider(0, 100, 0);
        speedSlider = new JSlider(0, 100, 0);
        unitView = new UnitView();

        this.add(agilitySlider);
        this.add(attackSlider);
        this.add(defenseSlider);
        this.add(speedSlider);
        this.add(unitView);

        agilitySlider.addChangeListener(this);
        attackSlider.addChangeListener(this);
        defenseSlider.addChangeListener(this);
        speedSlider.addChangeListener(this);

        setUnitProperties(initial);
    }

}
