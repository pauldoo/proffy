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
import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import pigeon.model.Clock;
import pigeon.model.Organization;
import pigeon.model.Member;
import pigeon.model.Race;
import pigeon.model.ValidationException;

/**
 * Lists the clocks for a given race.
 *
 * Lets the user add more clocks or go on to edit the times
 * associated with one of the clocks (using ClockEditor).
 */
public final class RaceEditor extends javax.swing.JPanel {

    private static final long serialVersionUID = 42L;

    private final Race race;
    Collection<Member> members;

    public RaceEditor(Race race, Collection<Member> members) {
        this.race = race;
        this.members = members;
        initComponents();
        reloadClocksTable();
        ((TitledBorder)jPanel1.getBorder()).setTitle("Clocks for " + race.toString());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        clocksTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        addClockButton = new javax.swing.JButton();
        editClockButton = new javax.swing.JButton();
        deleteClockButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Clocks"));
        clocksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(clocksTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        addClockButton.setText("Add Clock");
        addClockButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addClockButtonActionPerformed(evt);
            }
        });

        jPanel2.add(addClockButton);

        editClockButton.setText("Edit Clock");
        editClockButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editClockButtonActionPerformed(evt);
            }
        });

        jPanel2.add(editClockButton);

        deleteClockButton.setText("Delete Clock");
        deleteClockButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteClockButtonActionPerformed(evt);
            }
        });

        jPanel2.add(deleteClockButton);

        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jLabel1.setText("Add member clocks to the race below.");
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void deleteClockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClockButtonActionPerformed
        int index = clocksTable.getSelectedRow();
        Clock clock = Utilities.sortCollection(race.getClocks()).get(index);
        try {
            race.removeClock(clock);
        } catch (ValidationException e) {
            e.displayErrorDialog(this);
        }
        reloadClocksTable();
    }//GEN-LAST:event_deleteClockButtonActionPerformed

    private void editClockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClockButtonActionPerformed
        int index = clocksTable.getSelectedRow();
        Clock clock = Utilities.sortCollection(race.getClocks()).get(index);
        try {
            ClockSummary.editClock(this, clock, members, false);
            editResultsForClock( clock );
        } catch (UserCancelledException e) {
        }
        reloadClocksTable();
    }//GEN-LAST:event_editClockButtonActionPerformed

    private void editResultsForClock(Clock clock) throws UserCancelledException
    {
        ClockEditor.editClockResults(this, clock, race.getDaysCovered());
    }

    private void addClockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClockButtonActionPerformed
        try {
            Clock clock = ClockSummary.createClock(this, members);
            race.addClock(clock);
            try {
                editResultsForClock( clock );
            } catch (UserCancelledException e) {
                race.removeClock( clock );
                throw e;
            }
        } catch (UserCancelledException ex) {
        } catch (ValidationException e) {
            e.displayErrorDialog(this);
        }
        reloadClocksTable();
    }//GEN-LAST:event_addClockButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addClockButton;
    private javax.swing.JTable clocksTable;
    private javax.swing.JButton deleteClockButton;
    private javax.swing.JButton editClockButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    static public void editRaceResults(Component parent, Race race, Organization club) {
        RaceEditor panel = new RaceEditor(race, club.getMembers());
        Object[] options = {"Ok"};
        int result = JOptionPane.showOptionDialog(parent, panel, "Clocks", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    private void reloadClocksTable() {
        clocksTable.setModel(new ClocksTableModel(Utilities.sortCollection(race.getClocks())));
    }

}
