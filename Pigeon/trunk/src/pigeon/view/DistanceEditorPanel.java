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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import pigeon.model.Distance;
import pigeon.model.Club;
import pigeon.model.Member;
import pigeon.model.Racepoint;

/**
 *
 * @author  pauldoo
 */
class DistanceEditorPanel<Subject, Target> extends javax.swing.JPanel {
    
    private static final long serialVersionUID = 42L;
    
    private SortedMap<Target, Distance> distances;
    private List<JTextComponent> mileTextAreas;
    private List<JTextComponent> yardTextAreas;
    
    public static void editMemberDistances(Component parent, Member member, Club club) {
        SortedMap<Racepoint, Distance> distances = club.getDistancesForMember(member);
        if (!distances.isEmpty()) {
            DistanceEditorPanel<Member, Racepoint> panel = new DistanceEditorPanel<Member, Racepoint>(member, distances);
            JOptionPane.showMessageDialog(parent, panel, "Distances", JOptionPane.PLAIN_MESSAGE);
            panel.updateDistancesMap();
            for (Map.Entry<Racepoint, Distance> entry: distances.entrySet()) {
                club.setDistance(member, entry.getKey(), entry.getValue());
            }
        }
    }

    public static void editRacepointDistances(Component parent, Racepoint racepoint, Club club) {
        SortedMap<Member, Distance> distances = club.getDistancesForRacepoint(racepoint);
        if (!distances.isEmpty()) {
            DistanceEditorPanel<Racepoint, Member> panel = new DistanceEditorPanel<Racepoint, Member>(racepoint, distances);
            JOptionPane.showMessageDialog(parent, panel, "Distances", JOptionPane.PLAIN_MESSAGE);
            panel.updateDistancesMap();
            for (Map.Entry<Member, Distance> entry: distances.entrySet()) {
                club.setDistance(entry.getKey(), racepoint, entry.getValue());
            }
        }
    }
    
    /** Creates new form DistanceEditorPanel */
    public DistanceEditorPanel(Subject subject, SortedMap<Target, Distance> distances) {
        this.distances = distances;
        initComponents();
        subjectLabel.setText(subjectLabel.getText() + " " + subject.toString());
        
        mileTextAreas = new ArrayList<JTextComponent>();
        yardTextAreas = new ArrayList<JTextComponent>();
        
        int rowCount = 2;
        for (Map.Entry<Target, Distance> entry: distances.entrySet()) {
            {
                GridBagConstraints gridBagConstraints;
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = rowCount;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                this.add( new JLabel(entry.getKey().toString()), gridBagConstraints );
            }
            {
                GridBagConstraints gridBagConstraints;
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = rowCount;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                JTextComponent miles = new JTextField(""+entry.getValue().getMiles(), 5);
                mileTextAreas.add( miles );
                this.add( miles, gridBagConstraints );
            }
            {
                GridBagConstraints gridBagConstraints;
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = rowCount;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                JTextComponent yards = new JTextField(""+entry.getValue().getYardsRemainder(), 5);
                yardTextAreas.add( yards );
                this.add( yards, gridBagConstraints );
            }
            rowCount++;
        }
    }
    
    private void updateDistancesMap() {
        int count = 0;
        for (Map.Entry<Target, Distance> entry: distances.entrySet()) {
            int miles = Integer.parseInt(mileTextAreas.get(count).getText());
            int yards = Integer.parseInt(yardTextAreas.get(count).getText());
            Distance distance = Distance.createFromImperial(miles, yards);
            entry.setValue( distance );
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        subjectLabel = new javax.swing.JLabel();
        milesLabel = new javax.swing.JLabel();
        yardsLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        subjectLabel.setText("Distances For");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(subjectLabel, gridBagConstraints);

        milesLabel.setText("Miles");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(milesLabel, gridBagConstraints);

        yardsLabel.setText("Yards");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(yardsLabel, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel milesLabel;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JLabel yardsLabel;
    // End of variables declaration//GEN-END:variables
    
}
