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

import java.awt.Component;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import javax.swing.JOptionPane;
import pigeon.model.Clock;
import pigeon.model.Member;
import pigeon.model.ValidationException;

/**
 * Creates / edits the basic info about a clock, ie master/member open/close times.
 *
 * The ClockEditor final class edits the list of times.
 */
final class ClockSummary extends javax.swing.JPanel {

    private static final long serialVersionUID = 2610008291157171060L;

    private Clock clock;
    private Collection<Member> members;

    public ClockSummary(Clock clock, Collection<Member> members, boolean editable) {
        this.clock = clock;
        this.members = members;
        initComponents();
        setDate.setMode(DateTimeDisplayMode.DATE);
        masterSet.setMode(DateTimeDisplayMode.HOURS_MINUTES_SECONDS);
        memberSet.setMode(DateTimeDisplayMode.HOURS_MINUTES_SECONDS);
        openDate.setMode(DateTimeDisplayMode.DATE);
        masterOpen.setMode(DateTimeDisplayMode.HOURS_MINUTES_SECONDS);
        memberOpen.setMode(DateTimeDisplayMode.HOURS_MINUTES_SECONDS);
        
        addComboOptions();

        if (clock.getMember() != null) {
            memberCombo.setSelectedItem(clock.getMember());
        } else {
            memberCombo.setSelectedIndex(0);
        }

        setDate.setDate(pigeon.model.Utilities.beginningOfCalendarDay(clock.getTimeOnMasterWhenSet()));
        masterSet.setDate(pigeon.model.Utilities.fractionOfCalendarDay(clock.getTimeOnMasterWhenSet()));
        memberSet.setDate(pigeon.model.Utilities.fractionOfCalendarDay(clock.getTimeOnMemberWhenSet()));

        openDate.setDate(pigeon.model.Utilities.beginningOfCalendarDay(clock.getTimeOnMasterWhenOpened()));
        masterOpen.setDate(pigeon.model.Utilities.fractionOfCalendarDay(clock.getTimeOnMasterWhenOpened()));
        memberOpen.setDate(pigeon.model.Utilities.fractionOfCalendarDay(clock.getTimeOnMemberWhenOpened()));
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

        jLabel1 = new javax.swing.JLabel();
        memberCombo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        setDate = new pigeon.view.DateTimeComponent();
        masterSet = new pigeon.view.DateTimeComponent();
        memberSet = new pigeon.view.DateTimeComponent();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        openDate = new pigeon.view.DateTimeComponent();
        masterOpen = new pigeon.view.DateTimeComponent();
        memberOpen = new pigeon.view.DateTimeComponent();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(memberCombo, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Times"));
        jLabel2.setText("Clock Set Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Member Clock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Master Clock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(setDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(masterSet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(memberSet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Open Times"));
        jLabel5.setText("Clock Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Master Clock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Member Clock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(openDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(masterOpen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(memberOpen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jPanel2, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private pigeon.view.DateTimeComponent masterOpen;
    private pigeon.view.DateTimeComponent masterSet;
    private javax.swing.JComboBox memberCombo;
    private pigeon.view.DateTimeComponent memberOpen;
    private pigeon.view.DateTimeComponent memberSet;
    private pigeon.view.DateTimeComponent openDate;
    private pigeon.view.DateTimeComponent setDate;
    // End of variables declaration//GEN-END:variables

    private void addComboOptions()
    {
        for (Member m: members) {
            memberCombo.addItem( m );
        }
    }

    private void updateClockObject() throws ValidationException
    {
        clock.setMember((Member)memberCombo.getSelectedItem());
        
        Date setDate;
        try {
            setDate = this.setDate.getDate();
        } catch (ParseException e) {
            throw new ValidationException("Clock set date is invalid, " + this.setDate.getFormatPattern(), e);
        }
        
        try {
            Date masterSetDate = new Date(setDate.getTime() + masterSet.getDate().getTime());
            clock.setTimeOnMasterWhenSet(masterSetDate);
        } catch (ParseException e) {
            throw new ValidationException("Master set time is invalid, " + masterSet.getFormatPattern(), e);
        }

        try {
            Date memberSetDate = new Date(setDate.getTime() + memberSet.getDate().getTime());
            clock.setTimeOnMemberWhenSet(memberSetDate);
        } catch (ParseException e) {
            throw new ValidationException("Member set time is invalid, " + memberSet.getFormatPattern(), e);
        }

        Date openDate;
        try {
            openDate = this.openDate.getDate();
        } catch (ParseException e) {
            throw new ValidationException("Clock open date is invalid, " + this.openDate.getFormatPattern(), e);
        }

        try {
            Date masterOpenDate = new Date(openDate.getTime() + masterOpen.getDate().getTime());
            clock.setTimeOnMasterWhenOpened(masterOpenDate);
        } catch (ParseException e) {
            throw new ValidationException("Master open time is invalid, " + masterOpen.getFormatPattern(), e);
        }

        try {
            Date memberOpenDate = new Date(openDate.getTime() + memberOpen.getDate().getTime());
            clock.setTimeOnMemberWhenOpened(memberOpenDate);
        } catch (ParseException e) {
            throw new ValidationException("Member open time is invalid, " + memberOpen.getFormatPattern(), e);
        }
    }

    public static void editClock(Component parent, Clock clock, Collection<Member> members, boolean newClock) throws UserCancelledException {
        ClockSummary panel = new ClockSummary(clock, members, true);
        while (true) {
            Object[] options = { (newClock ? "Add" : "Ok"), "Cancel" };
            int result = JOptionPane.showOptionDialog(parent, panel, "Clock Information", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == 0) {
                try {
                    panel.updateClockObject();
                    break;
                } catch (ValidationException e) {
                    e.displayErrorDialog(panel);
                }
            } else {
                result = JOptionPane.showConfirmDialog(parent, "Return to Clocks window and discard these changes?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    throw new UserCancelledException();
                }
            }
        }
    }

    public static Clock createClock(Component parent, Collection<Member> members) throws UserCancelledException
    {
        Clock clock = new Clock();
        editClock(parent, clock, members, true);
        return clock;
    }
}
