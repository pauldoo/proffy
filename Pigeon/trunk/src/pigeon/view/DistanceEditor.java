/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.

        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/

package pigeon.view;

import java.awt.Component;
import java.util.Map;
import java.util.SortedMap;
import javax.swing.JOptionPane;
import pigeon.model.Distance;
import pigeon.model.Organization;
import pigeon.model.Member;
import pigeon.model.Racepoint;
import pigeon.model.ValidationException;

/**
 * Panel to let the user enter the distances (in the form of a table) for a single racepoint or member.
 */
final class DistanceEditor<Subject, Target> extends javax.swing.JPanel {

    private static final long serialVersionUID = 8017955650652536256L;

    private Map<Target, Distance> distances;
    private DistancesTableModel<Target> distancesTableModel;

    public static void editMemberDistances(Component parent, Member member, Organization club) throws UserCancelledException {
        Map<Racepoint, Distance> distances = club.getDistancesForMember(member);
        if (distances.isEmpty()) return;
        DistanceEditor<Member, Racepoint> panel = new DistanceEditor<Member, Racepoint>(member, "Racepoint", distances);
        while (true) {
            Object[] options = {"Ok", "Cancel"};
            int result = JOptionPane.showOptionDialog(parent, panel, "Distances", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == 0) {
                for (Map.Entry<Racepoint, Distance> entry: distances.entrySet()) {
                    club.setDistance(member, entry.getKey(), entry.getValue());
                }
                break;
            } else {
                result = JOptionPane.showConfirmDialog(parent, "Return to main menu and discard these changes?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    throw new UserCancelledException();
                }
            }
        }
    }

    public static void editRacepointDistances(Component parent, Racepoint racepoint, Organization club) throws UserCancelledException {
        Map<Member, Distance> distances = club.getDistancesForRacepoint(racepoint);
        if (distances.isEmpty()) return;
        DistanceEditor<Racepoint, Member> panel = new DistanceEditor<Racepoint, Member>(racepoint, "Member", distances);
        while (true) {
            Object[] options = {"Ok", "Cancel"};
            int result = JOptionPane.showOptionDialog(parent, panel, "Distances", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == 0) {
                for (Map.Entry<Member, Distance> entry: distances.entrySet()) {
                    club.setDistance(entry.getKey(), racepoint, entry.getValue());
                }
                break;
            } else {
                result = JOptionPane.showConfirmDialog(parent, "Return to main menu and discard these changes?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    throw new UserCancelledException();
                }
            }
        }
    }

    public DistanceEditor(Subject subject, String targetTitle, Map<Target, Distance> distances) {
        this.distances = distances;
        this.distancesTableModel = new DistancesTableModel<Target>(targetTitle, distances, true);
        initComponents();
        distancesPanel.setBorder(new javax.swing.border.TitledBorder("Distances For " + subject));
    }

    private void updateDistancesMap() {

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        distancesPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        distancesTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        distancesPanel.setLayout(new java.awt.BorderLayout());

        distancesPanel.setBorder(new javax.swing.border.TitledBorder("Distances For"));
        distancesTable.setModel(distancesTableModel);
        distancesTable.setRowSelectionAllowed(false);
        scrollPane.setViewportView(distancesTable);

        distancesPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        add(distancesPanel, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel distancesPanel;
    private javax.swing.JTable distancesTable;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

}
