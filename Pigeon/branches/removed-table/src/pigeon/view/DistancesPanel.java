/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.SortedMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pigeon.model.Distance;

/**
 *
 * @author pauldoo
 */
public class DistancesPanel<Target> extends JPanel {
    
    private static final long serialVersionUID = 42L;
    
    private SortedMap<Target, Distance> distances;
    
    /** Creates a new instance of DistancesPanel */
    public DistancesPanel(SortedMap<Target, Distance> distances) {
        this.distances = distances;
        initialise();
    }
    
    private void initialise() {
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 2, 2, 2);
        
        for (Map.Entry<Target, Distance> e: distances.entrySet()) {
            constraints.gridwidth = 1;
            constraints.fill = GridBagConstraints.NONE;
            JLabel targetLabel = new JLabel(e.getKey().toString());
            gridbag.setConstraints(targetLabel, constraints);
            this.add(targetLabel);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            JTextField miles = new JTextField("" + e.getValue().getMiles(), 5);
            gridbag.setConstraints(miles, constraints);
            this.add(miles);

            constraints.fill = GridBagConstraints.NONE;
            JLabel milesLabel = new JLabel("miles");
            gridbag.setConstraints(milesLabel, constraints);
            this.add(milesLabel);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            JTextField yards = new JTextField("" + e.getValue().getYardsRemainder(), 5);
            gridbag.setConstraints(yards, constraints);
            this.add(yards);

            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.fill = GridBagConstraints.NONE;
            JLabel yardsLabel = new JLabel("yards");
            gridbag.setConstraints(yardsLabel, constraints);
            this.add(yardsLabel);
        }
    }
    
    public void updateDistancesMap() {
    
    }
}
