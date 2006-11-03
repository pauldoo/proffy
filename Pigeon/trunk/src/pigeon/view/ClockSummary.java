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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import pigeon.model.Clock;
import pigeon.model.Member;
import pigeon.model.ValidationException;

/**
 * Creates / edits the basic info about a clock, ie master/member open/close times.
 * The ClockEditor class edits the list of times.
 * @author  pauldoo
 */
public class ClockSummary extends javax.swing.JPanel {
    
    private static final long serialVersionUID = 42L;

    private Clock clock;
    private Collection<Member> members;
    
    /** Creates new form ClockSummary */
    public ClockSummary(Clock clock, Collection<Member> members, boolean editable) {
        this.clock = clock;
        this.members = members;
        initComponents();
        addComboOptions();

        if (clock.getMember() != null) {
            memberCombo.setSelectedItem(clock.getMember());
        } else {
            memberCombo.setSelectedIndex(0);
        }
        
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(clock.getTimeOnMasterWhenSet());
        setDayCombo.setSelectedIndex(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        setMonthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        setYearCombo.setSelectedIndex(calendar.get(Calendar.YEAR) - Utilities.BASE_YEAR);
        masterSetHourCombo.setSelectedIndex(calendar.get(Calendar.HOUR_OF_DAY));
        masterSetMinuteCombo.setSelectedIndex(calendar.get(Calendar.MINUTE));
        masterSetSecondCombo.setSelectedIndex(calendar.get(Calendar.SECOND));
        calendar.setTime(clock.getTimeOnMemberWhenSet());
        memberSetHourCombo.setSelectedIndex(calendar.get(Calendar.HOUR_OF_DAY));
        memberSetMinuteCombo.setSelectedIndex(calendar.get(Calendar.MINUTE));
        memberSetSecondCombo.setSelectedIndex(calendar.get(Calendar.SECOND));

        calendar.setTime(clock.getTimeOnMasterWhenOpened());
        openDayCombo.setSelectedIndex(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        openMonthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        openYearCombo.setSelectedIndex(calendar.get(Calendar.YEAR) - Utilities.BASE_YEAR);
        masterOpenHourCombo.setSelectedIndex(calendar.get(Calendar.HOUR_OF_DAY));
        masterOpenMinuteCombo.setSelectedIndex(calendar.get(Calendar.MINUTE));
        masterOpenSecondCombo.setSelectedIndex(calendar.get(Calendar.SECOND));
        calendar.setTime(clock.getTimeOnMemberWhenOpened());
        memberOpenHourCombo.setSelectedIndex(calendar.get(Calendar.HOUR_OF_DAY));
        memberOpenMinuteCombo.setSelectedIndex(calendar.get(Calendar.MINUTE));
        memberOpenSecondCombo.setSelectedIndex(calendar.get(Calendar.SECOND));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        memberCombo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        setDayCombo = new javax.swing.JComboBox();
        masterSetHourCombo = new javax.swing.JComboBox();
        memberSetHourCombo = new javax.swing.JComboBox();
        masterSetMinuteCombo = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        memberSetMinuteCombo = new javax.swing.JComboBox();
        setMonthCombo = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        setYearCombo = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        masterSetSecondCombo = new javax.swing.JComboBox();
        memberSetSecondCombo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        openDayCombo = new javax.swing.JComboBox();
        openYearCombo = new javax.swing.JComboBox();
        openMonthCombo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        masterOpenHourCombo = new javax.swing.JComboBox();
        masterOpenMinuteCombo = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        memberOpenHourCombo = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        memberOpenMinuteCombo = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        memberOpenSecondCombo = new javax.swing.JComboBox();
        masterOpenSecondCombo = new javax.swing.JComboBox();

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(setDayCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(masterSetHourCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(memberSetHourCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(masterSetMinuteCombo, gridBagConstraints);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("/");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel1.add(jLabel12, gridBagConstraints);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("/");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel1.add(jLabel13, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(memberSetMinuteCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(setMonthCombo, gridBagConstraints);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText(":");
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel1.add(jLabel14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(setYearCombo, gridBagConstraints);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText(":");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel1.add(jLabel15, gridBagConstraints);

        jLabel16.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel1.add(jLabel16, gridBagConstraints);

        jLabel17.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel1.add(jLabel17, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(masterSetSecondCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(memberSetSecondCombo, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(openDayCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(openYearCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(openMonthCombo, gridBagConstraints);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("/");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel2.add(jLabel8, gridBagConstraints);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("/");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel2.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(masterOpenHourCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(masterOpenMinuteCombo, gridBagConstraints);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(":");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel2.add(jLabel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(memberOpenHourCombo, gridBagConstraints);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText(":");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel2.add(jLabel11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(memberOpenMinuteCombo, gridBagConstraints);

        jLabel18.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel2.add(jLabel18, gridBagConstraints);

        jLabel19.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 10);
        jPanel2.add(jLabel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(memberOpenSecondCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(masterOpenSecondCombo, gridBagConstraints);

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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox masterOpenHourCombo;
    private javax.swing.JComboBox masterOpenMinuteCombo;
    private javax.swing.JComboBox masterOpenSecondCombo;
    private javax.swing.JComboBox masterSetHourCombo;
    private javax.swing.JComboBox masterSetMinuteCombo;
    private javax.swing.JComboBox masterSetSecondCombo;
    private javax.swing.JComboBox memberCombo;
    private javax.swing.JComboBox memberOpenHourCombo;
    private javax.swing.JComboBox memberOpenMinuteCombo;
    private javax.swing.JComboBox memberOpenSecondCombo;
    private javax.swing.JComboBox memberSetHourCombo;
    private javax.swing.JComboBox memberSetMinuteCombo;
    private javax.swing.JComboBox memberSetSecondCombo;
    private javax.swing.JComboBox openDayCombo;
    private javax.swing.JComboBox openMonthCombo;
    private javax.swing.JComboBox openYearCombo;
    private javax.swing.JComboBox setDayCombo;
    private javax.swing.JComboBox setMonthCombo;
    private javax.swing.JComboBox setYearCombo;
    // End of variables declaration//GEN-END:variables

    private void addComboOptions()
    {
        for (Member m: Utilities.sortCollection(members)) {
            memberCombo.addItem( m );
        }
        
        for (int day = 1; day <= 31; day++) {
            String str = new Integer(day).toString();
            if (day < 10) {
                str = "0" + str;
            }
            setDayCombo.addItem(str);
            openDayCombo.addItem(str);
        }
        
        for (int month = 1; month <= 12; month++) {
            String str = new Integer(month).toString();
            if (month < 10) {
                str = "0" + str;
            }
            setMonthCombo.addItem(str);
            openMonthCombo.addItem(str);
        }
        
        for (int year = Utilities.BASE_YEAR; year <= Utilities.BASE_YEAR + 20; year++) {
            setYearCombo.addItem(year);
            openYearCombo.addItem(year);
        }
        
        for (int hour = 0; hour <= 23; hour++) {
            String str = new Integer(hour).toString();
            if (hour < 10) {
                str = "0" + str;
            }
            masterSetHourCombo.addItem(str);
            masterOpenHourCombo.addItem(str);
            memberSetHourCombo.addItem(str);
            memberOpenHourCombo.addItem(str);
        }
        
        for (int minute = 0; minute <= 59; minute++) {
            String str = new Integer(minute).toString();
            if (minute < 10) {
                str = "0" + str;
            }
            masterSetMinuteCombo.addItem(str);
            masterOpenMinuteCombo.addItem(str);
            memberSetMinuteCombo.addItem(str);
            memberOpenMinuteCombo.addItem(str);

            masterSetSecondCombo.addItem(str);
            masterOpenSecondCombo.addItem(str);
            memberSetSecondCombo.addItem(str);
            memberOpenSecondCombo.addItem(str);
        }
    }
    
    private void updateClockObject() throws ValidationException
    {
        clock.setMember((Member)memberCombo.getSelectedItem());
        
        Date masterSetDate = new GregorianCalendar(
                new Integer(setYearCombo.getSelectedItem().toString()),
                new Integer(setMonthCombo.getSelectedItem().toString()) - 1,
                new Integer(setDayCombo.getSelectedItem().toString()),
                new Integer(masterSetHourCombo.getSelectedItem().toString()),
                new Integer(masterSetMinuteCombo.getSelectedItem().toString()),
                new Integer(masterSetSecondCombo.getSelectedItem().toString())).getTime();
        clock.setTimeOnMasterWhenSet(masterSetDate);

        Date memberSetDate = new GregorianCalendar(
                new Integer(setYearCombo.getSelectedItem().toString()),
                new Integer(setMonthCombo.getSelectedItem().toString()) - 1,
                new Integer(setDayCombo.getSelectedItem().toString()),
                new Integer(memberSetHourCombo.getSelectedItem().toString()),
                new Integer(memberSetMinuteCombo.getSelectedItem().toString()),
                new Integer(memberSetSecondCombo.getSelectedItem().toString())).getTime();
        clock.setTimeOnMemberWhenSet(memberSetDate);

        Date masterOpenDate = new GregorianCalendar(
                new Integer(openYearCombo.getSelectedItem().toString()),
                new Integer(openMonthCombo.getSelectedItem().toString()) - 1,
                new Integer(openDayCombo.getSelectedItem().toString()),
                new Integer(masterOpenHourCombo.getSelectedItem().toString()),
                new Integer(masterOpenMinuteCombo.getSelectedItem().toString()),
                new Integer(masterOpenSecondCombo.getSelectedItem().toString())).getTime();
        clock.setTimeOnMasterWhenOpened(masterOpenDate);

        Date memberOpenDate = new GregorianCalendar(
                new Integer(openYearCombo.getSelectedItem().toString()),
                new Integer(openMonthCombo.getSelectedItem().toString()) - 1,
                new Integer(openDayCombo.getSelectedItem().toString()),
                new Integer(memberOpenHourCombo.getSelectedItem().toString()),
                new Integer(memberOpenMinuteCombo.getSelectedItem().toString()),
                new Integer(memberOpenSecondCombo.getSelectedItem().toString())).getTime();
        clock.setTimeOnMemberWhenOpened(memberOpenDate);
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
                    e.displayErrorDialog(parent);
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
