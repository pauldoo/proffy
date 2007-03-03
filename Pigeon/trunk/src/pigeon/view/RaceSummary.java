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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import pigeon.model.Organization;
import pigeon.model.Constants;
import pigeon.model.Race;
import pigeon.model.Racepoint;
import pigeon.model.ValidationException;

/**
 * Edits basic info regarding a race like the racepoint, date, time etc.
 */
final class RaceSummary extends javax.swing.JPanel {

    private static final long serialVersionUID = 42L;

    private final Race race;

    public RaceSummary(Race race, Organization club, boolean editable) {
        this.race = race;
        initComponents();
        addComboOptions(club);

        if (race.getRacepoint() != null) {
            racepointCombo.setSelectedItem(race.getRacepoint());
        } else {
            racepointCombo.setSelectedIndex(0);
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(race.getLiberationDate());
        dayCombo.setSelectedIndex(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        monthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        yearCombo.setSelectedIndex(calendar.get(Calendar.YEAR) - Utilities.BASE_YEAR);
        hourCombo.setSelectedIndex(calendar.get(Calendar.HOUR_OF_DAY));
        minuteCombo.setSelectedIndex(calendar.get(Calendar.MINUTE));
        daysCoveredCombo.setSelectedIndex(race.getDaysCovered() - 1);
        windDirectionText.setText(race.getWindDirection());
        if (race.hasHoursOfDarkness()) {
            darknessBeginsHour.setSelectedIndex((int)((race.getDarknessBegins() / Constants.MILLISECONDS_PER_HOUR) - 12));
            darknessBeginsMinute.setSelectedIndex((int)((race.getDarknessBegins() / Constants.MILLISECONDS_PER_MINUTE) % 60));
            darknessEndsHour.setSelectedIndex((int)(race.getDarknessEnds() / Constants.MILLISECONDS_PER_HOUR));
            darknessEndsMinute.setSelectedIndex((int)((race.getDarknessEnds() / Constants.MILLISECONDS_PER_MINUTE) % 60));
        }

        updateHoursOfDarknessEnabledStatus();
    }

    private void addComboOptions(Organization club) {
        for (Racepoint r: Utilities.sortCollection(club.getRacepoints())) {
            racepointCombo.addItem( r );
        }

        for (int day = 1; day <= 31; day++) {
            String str = new Integer(day).toString();
            if (day < 10) {
                str = "0" + str;
            }
            dayCombo.addItem(str);
        }

        for (int month = 1; month <= 12; month++) {
            String str = new Integer(month).toString();
            if (month < 10) {
                str = "0" + str;
            }
            monthCombo.addItem(str);
        }

        for (int year = Utilities.BASE_YEAR; year <= Utilities.BASE_YEAR + 20; year++) {
            yearCombo.addItem(year);
        }

        for (int day = 1; day <= 3; day++) {
            daysCoveredCombo.addItem(day);
        }

        for (int hour = 0; hour <= 23; hour++) {
            String str = new Integer(hour).toString();
            if (hour < 10) {
                str = "0" + str;
            }
            hourCombo.addItem(str);
            if (hour >= 12) {
                darknessBeginsHour.addItem(str);
            } else {
                darknessEndsHour.addItem(str);
            }
        }

        for (int minute = 0; minute <= 59; minute++) {
            String str = new Integer(minute).toString();
            if (minute < 10) {
                str = "0" + str;
            }
            minuteCombo.addItem(str);
            darknessBeginsMinute.addItem(str);
            darknessEndsMinute.addItem(str);
        }
    }

    private boolean hoursOfDarknessEnabled()
    {
        int daysCovered = new Integer(daysCoveredCombo.getSelectedItem().toString());
        return daysCovered > 1;
    }

    private void updateHoursOfDarknessEnabledStatus()
    {
        boolean enable = hoursOfDarknessEnabled();
        darknessBeginsText.setEnabled(enable);
        darknessBeginsHour.setEnabled(enable);
        darknessBeginsSeperator.setEnabled(enable);
        darknessBeginsMinute.setEnabled(enable);
        darknessEndsText.setEnabled(enable);
        darknessEndsHour.setEnabled(enable);
        darknessEndsSeperator.setEnabled(enable);
        darknessEndsMinute.setEnabled(enable);
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

        racepointLabel = new javax.swing.JLabel();
        racepointCombo = new javax.swing.JComboBox();
        liberationDateLabel = new javax.swing.JLabel();
        dayCombo = new javax.swing.JComboBox();
        hourCombo = new javax.swing.JComboBox();
        minuteCombo = new javax.swing.JComboBox();
        yearCombo = new javax.swing.JComboBox();
        monthCombo = new javax.swing.JComboBox();
        monthYearSeperator = new javax.swing.JLabel();
        hourMinuteSeperator = new javax.swing.JLabel();
        dayMonthSeperator = new javax.swing.JLabel();
        liberationTimeLabel = new javax.swing.JLabel();
        windDirectionLabel = new javax.swing.JLabel();
        windDirectionText = new javax.swing.JTextField();
        daysCoveredLabel = new javax.swing.JLabel();
        daysCoveredCombo = new javax.swing.JComboBox();
        darknessBeginsText = new javax.swing.JLabel();
        darknessBeginsHour = new javax.swing.JComboBox();
        darknessEndsHour = new javax.swing.JComboBox();
        darknessEndsText = new javax.swing.JLabel();
        darknessEndsMinute = new javax.swing.JComboBox();
        darknessBeginsMinute = new javax.swing.JComboBox();
        darknessEndsSeperator = new javax.swing.JLabel();
        darknessBeginsSeperator = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        racepointLabel.setText("Racepoint");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(racepointLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(racepointCombo, gridBagConstraints);

        liberationDateLabel.setText("Liberation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(liberationDateLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(dayCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(hourCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(minuteCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(yearCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(monthCombo, gridBagConstraints);

        monthYearSeperator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monthYearSeperator.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        add(monthYearSeperator, gridBagConstraints);

        hourMinuteSeperator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hourMinuteSeperator.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        add(hourMinuteSeperator, gridBagConstraints);

        dayMonthSeperator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayMonthSeperator.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        add(dayMonthSeperator, gridBagConstraints);

        liberationTimeLabel.setText("Liberation Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(liberationTimeLabel, gridBagConstraints);

        windDirectionLabel.setText("Wind Direction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(windDirectionLabel, gridBagConstraints);

        windDirectionText.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(windDirectionText, gridBagConstraints);

        daysCoveredLabel.setText("No. of days covered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(daysCoveredLabel, gridBagConstraints);

        daysCoveredCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                daysCoveredComboActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(daysCoveredCombo, gridBagConstraints);

        darknessBeginsText.setText("Darkness Begins");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(darknessBeginsText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(darknessBeginsHour, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(darknessEndsHour, gridBagConstraints);

        darknessEndsText.setText("Darkness Ends");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(darknessEndsText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(darknessEndsMinute, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(darknessBeginsMinute, gridBagConstraints);

        darknessEndsSeperator.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        add(darknessEndsSeperator, gridBagConstraints);

        darknessBeginsSeperator.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        add(darknessBeginsSeperator, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void daysCoveredComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_daysCoveredComboActionPerformed
    {//GEN-HEADEREND:event_daysCoveredComboActionPerformed
        updateHoursOfDarknessEnabledStatus();
    }//GEN-LAST:event_daysCoveredComboActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox darknessBeginsHour;
    private javax.swing.JComboBox darknessBeginsMinute;
    private javax.swing.JLabel darknessBeginsSeperator;
    private javax.swing.JLabel darknessBeginsText;
    private javax.swing.JComboBox darknessEndsHour;
    private javax.swing.JComboBox darknessEndsMinute;
    private javax.swing.JLabel darknessEndsSeperator;
    private javax.swing.JLabel darknessEndsText;
    private javax.swing.JComboBox dayCombo;
    private javax.swing.JLabel dayMonthSeperator;
    private javax.swing.JComboBox daysCoveredCombo;
    private javax.swing.JLabel daysCoveredLabel;
    private javax.swing.JComboBox hourCombo;
    private javax.swing.JLabel hourMinuteSeperator;
    private javax.swing.JLabel liberationDateLabel;
    private javax.swing.JLabel liberationTimeLabel;
    private javax.swing.JComboBox minuteCombo;
    private javax.swing.JComboBox monthCombo;
    private javax.swing.JLabel monthYearSeperator;
    private javax.swing.JComboBox racepointCombo;
    private javax.swing.JLabel racepointLabel;
    private javax.swing.JLabel windDirectionLabel;
    private javax.swing.JTextField windDirectionText;
    private javax.swing.JComboBox yearCombo;
    // End of variables declaration//GEN-END:variables

    private void updateRaceObject() throws ValidationException {
        race.setRacepoint((Racepoint)racepointCombo.getSelectedItem());
        Date liberationDate = new GregorianCalendar(
                new Integer(yearCombo.getSelectedItem().toString()),
                new Integer(monthCombo.getSelectedItem().toString()) - 1,
                new Integer(dayCombo.getSelectedItem().toString()),
                new Integer(hourCombo.getSelectedItem().toString()),
                new Integer(minuteCombo.getSelectedItem().toString())).getTime();
        race.setLiberationDate(liberationDate);
        race.setDaysCovered(new Integer(daysCoveredCombo.getSelectedItem().toString()));
        race.setWindDirection(windDirectionText.getText());

        if (hoursOfDarknessEnabled()) {
            long darknessBegins =
                    (new Integer(darknessBeginsHour.getSelectedItem().toString())) * Constants.MILLISECONDS_PER_HOUR +
                    (new Integer(darknessBeginsMinute.getSelectedItem().toString())) * Constants.MILLISECONDS_PER_MINUTE;
            long darknessEnds =
                    (new Integer(darknessEndsHour.getSelectedItem().toString())) * Constants.MILLISECONDS_PER_HOUR +
                    (new Integer(darknessEndsMinute.getSelectedItem().toString())) * Constants.MILLISECONDS_PER_MINUTE;
            race.setHoursOfDarkness((int)darknessBegins, (int)darknessEnds);
        }
    }

    public static void editRace(Component parent, Race race, Organization club, boolean newRace) throws UserCancelledException {
        RaceSummary panel = new RaceSummary(race, club, true);
        while (true) {
            Object[] options = { (newRace ? "Add" : "Ok"), "Cancel" };
            int result = JOptionPane.showOptionDialog(parent, panel, "Race Information", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == 0) {
                try {
                    panel.updateRaceObject();
                    break;
                } catch (ValidationException e) {
                    e.displayErrorDialog(parent);
                }
            } else {
                result = JOptionPane.showConfirmDialog(parent, "Return to Races window and discard these changes?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    throw new UserCancelledException();
                }
            }
        }
    }

    public static Race createRace(Component parent, Organization club) throws UserCancelledException {
        Race race = new Race();
        editRace(parent, race, club, true);
        return race;
    }

}
