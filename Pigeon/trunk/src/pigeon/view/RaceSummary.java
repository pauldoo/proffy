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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SortingFocusTraversalPolicy;
import pigeon.competitions.Competition;
import pigeon.model.Organization;
import pigeon.model.Constants;
import pigeon.model.Race;
import pigeon.model.Racepoint;
import pigeon.model.ValidationException;

/**
    FocusTraversalPolicy based on LayoutFocusTraversalPolicy, but runs top down.
*/
final class TopDownFocusTraversalPolicy extends SortingFocusTraversalPolicy
{
    private static final class LocalComparator implements Comparator<Component>
    {
        public int compare(Component lhs, Component rhs)
        {
            Rectangle lhsRect = lhs.getBounds();
            Rectangle rhsRect = rhs.getBounds();
            int result = new Integer(lhsRect.x).compareTo(rhsRect.x);
            if (result == 0) {
                result = new Integer(lhsRect.y).compareTo(rhsRect.y);
            }
            return result;
        }

        public boolean equals(Object obj)
        {
            return (obj instanceof TopDownFocusTraversalPolicy);
        }
    }

    public TopDownFocusTraversalPolicy()
    {
        super(new LocalComparator());
    }

    protected boolean accept(Component aComponent)
    {
        return !(aComponent instanceof JLabel);
    }
}

/**
    Edits basic info regarding a race like the racepoint, date, time etc.
*/
final class RaceSummary extends javax.swing.JPanel {

    private static final long serialVersionUID = 5181019751737997744L;

    private final Race race;
    private final Map<String, JTextField[]> raceEntrantsCountFields = new TreeMap<String, JTextField[]>();
    private final Map<String, Map<String, JTextField>> poolEntrantsCountFields = new TreeMap<String, Map<String, JTextField>>();
    private final Map<String, List<JTextField>> prizeFields = new TreeMap<String, List<JTextField>>();

    public RaceSummary(Race race, Organization club, Configuration configuration, boolean editable) {
        this.race = race;
        initComponents();
        addComboOptions(club, configuration);

        if (race.getRacepoint() != null) {
            racepointCombo.setSelectedItem(race.getRacepoint());
        } else {
            racepointCombo.setSelectedIndex(0);
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(race.getLiberationDate());
        dayCombo.setSelectedIndex(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        monthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        yearCombo.setSelectedIndex(calendar.get(Calendar.YEAR) - Utilities.YEAR_DISPLAY_START);
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
        {
            Map<String, Integer> membersEntered = race.getMembersEntered();
            Map<String, Integer> birdsEntered = race.getBirdsEntered();
            for (Map.Entry<String, JTextField[]> entry: raceEntrantsCountFields.entrySet()) {
                Integer members = membersEntered.get(entry.getKey());
                Integer birds = birdsEntered.get(entry.getKey());
                if (members == null) {
                    members = new Integer(0);
                }
                if (birds == null) {
                    birds = new Integer(0);
                }
                entry.getValue()[0].setText(members.toString());
                entry.getValue()[1].setText(birds.toString());
            }
        }
        {
            Map<String, Map<String, Integer>> entrantsCount = race.getBirdsEnteredInPools();
            for (Map.Entry<String, Map<String, JTextField>> i: poolEntrantsCountFields.entrySet()) {
                Map<String, Integer> map = entrantsCount.get(i.getKey());
                for (Map.Entry<String, JTextField> j: i.getValue().entrySet()) {
                    Integer count = null;
                    if (map != null) {
                        count = map.get(j.getKey());
                    }
                    if (count == null) {
                        count = new Integer(0);
                    }
                    j.getValue().setText(count.toString());
                }
            }
        }
        {
            Map<String, List<Double>> prizes = race.getPrizes();
            for (Map.Entry<String, List<JTextField>> i: prizeFields.entrySet()) {
                List<Double> list = prizes.get(i.getKey());
                for (JTextField field: i.getValue()) {
                    int index = i.getValue().indexOf(field);
                    if (list != null && index < list.size()) {
                        field.setText(Utilities.currencyFormat().format(list.get(index)));
                    } else {
                        field.setText(Utilities.currencyFormat().format(0));
                    }
                }
            }
        }

        updateHoursOfDarknessEnabledStatus();
    }

    private void addComboOptions(Organization club, Configuration configuration) {
        for (Racepoint r: club.getRacepoints()) {
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

        for (int year = Utilities.YEAR_DISPLAY_START; year <= Utilities.YEAR_DISPLAY_END; year++) {
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
        
        populateRaceEntrantsCountPanel(raceEntrantsCountPanel, raceEntrantsCountFields, club);
        populatePoolEntrantsCountPanel(poolEntrantsCountPanel, poolEntrantsCountFields, club, configuration);
        populatePrizesPanel(prizesPanel, prizeFields, club);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        summaryPanel = new javax.swing.JPanel();
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
        raceEntrantsCountPanel = new javax.swing.JPanel();
        poolEntrantsCountPanel = new javax.swing.JPanel();
        prizesPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        summaryPanel.setLayout(new java.awt.GridBagLayout());

        racepointLabel.setText("Racepoint");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(racepointLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(racepointCombo, gridBagConstraints);

        liberationDateLabel.setText("Liberation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(liberationDateLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(dayCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(hourCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(minuteCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(yearCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(monthCombo, gridBagConstraints);

        monthYearSeperator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monthYearSeperator.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        summaryPanel.add(monthYearSeperator, gridBagConstraints);

        hourMinuteSeperator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hourMinuteSeperator.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        summaryPanel.add(hourMinuteSeperator, gridBagConstraints);

        dayMonthSeperator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayMonthSeperator.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        summaryPanel.add(dayMonthSeperator, gridBagConstraints);

        liberationTimeLabel.setText("Liberation Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(liberationTimeLabel, gridBagConstraints);

        windDirectionLabel.setText("Wind Direction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(windDirectionLabel, gridBagConstraints);

        windDirectionText.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(windDirectionText, gridBagConstraints);

        daysCoveredLabel.setText("No. of days covered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(daysCoveredLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(daysCoveredCombo, gridBagConstraints);

        darknessBeginsText.setText("Darkness Begins");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(darknessBeginsText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(darknessBeginsHour, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(darknessEndsHour, gridBagConstraints);

        darknessEndsText.setText("Darkness Ends");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(darknessEndsText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(darknessEndsMinute, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        summaryPanel.add(darknessBeginsMinute, gridBagConstraints);

        darknessEndsSeperator.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        summaryPanel.add(darknessEndsSeperator, gridBagConstraints);

        darknessBeginsSeperator.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 10, 2);
        summaryPanel.add(darknessBeginsSeperator, gridBagConstraints);

        jTabbedPane1.addTab("Summary", summaryPanel);

        jTabbedPane1.addTab("No. Race Entrants", raceEntrantsCountPanel);

        jTabbedPane1.addTab("No. Pool Entrants", poolEntrantsCountPanel);

        jTabbedPane1.addTab("Prizes", prizesPanel);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents


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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel liberationDateLabel;
    private javax.swing.JLabel liberationTimeLabel;
    private javax.swing.JComboBox minuteCombo;
    private javax.swing.JComboBox monthCombo;
    private javax.swing.JLabel monthYearSeperator;
    private javax.swing.JPanel poolEntrantsCountPanel;
    private javax.swing.JPanel prizesPanel;
    private javax.swing.JPanel raceEntrantsCountPanel;
    private javax.swing.JComboBox racepointCombo;
    private javax.swing.JLabel racepointLabel;
    private javax.swing.JPanel summaryPanel;
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
        
        {
            Map<String, Integer> membersEntered = new TreeMap<String, Integer>();
            Map<String, Integer> birdsEntered = new TreeMap<String, Integer>();
            for (Map.Entry<String, JTextField[]> entry: raceEntrantsCountFields.entrySet()) {
                membersEntered.put(entry.getKey(), Integer.parseInt(entry.getValue()[0].getText()));
                birdsEntered.put(entry.getKey(), Integer.parseInt(entry.getValue()[1].getText()));
            }
            race.setMembersEntered(membersEntered);
            race.setBirdsEntered(birdsEntered);
        }
        
        {
            Map<String, Map<String, Integer>> entrantsCount = new TreeMap<String, Map<String, Integer>>();
            for (Map.Entry<String, Map<String, JTextField>> i: poolEntrantsCountFields.entrySet()) {
                entrantsCount.put(i.getKey(), new TreeMap<String, Integer>());
                for (Map.Entry<String, JTextField> j: i.getValue().entrySet()) {
                    entrantsCount.get(i.getKey()).put(j.getKey(), Integer.parseInt(j.getValue().getText()));
                }
            }
            race.setBirdsEnteredInPools(entrantsCount);
        }
        
        {
            Map<String, List<Double>> prizes = new TreeMap<String, List<Double>>();
            for (Map.Entry<String, List<JTextField>> i: prizeFields.entrySet()) {
                List<Double> list = new ArrayList<Double>();
                for (JTextField field: i.getValue()) {
                    list.add(Double.parseDouble(field.getText()));
                }
                prizes.put(i.getKey(), list);
            }
            race.setPrizes(prizes);
        }
    }

    public static void editRace(Component parent, Race race, Organization club, Configuration configuration, boolean newRace) throws UserCancelledException {
        RaceSummary panel = new RaceSummary(race, club, configuration, true);
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

    public static Race createRace(Component parent, Organization club, Configuration configuration) throws UserCancelledException {
        Race race = new Race();
        editRace(parent, race, club, configuration, true);
        return race;
    }
    
    /**
        Populates the JPanel which contains the input dialogs for inputting the number of
        members and birds entered from each section.
    */
    private static void populateRaceEntrantsCountPanel(JPanel panel, Map<String, JTextField[]> textFieldMap, Organization club)
    {
        GridBagLayout gridbag = new GridBagLayout();
        panel.setLayout(gridbag);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        
        {
            constraints.gridx = 1;
            JLabel memberCountLabel = new JLabel("Members");
            gridbag.setConstraints(memberCountLabel, constraints);
            panel.add(memberCountLabel);
        }
        {
            constraints.gridx = 2;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            JLabel birdCountLabel = new JLabel("Birds");
            gridbag.setConstraints(birdCountLabel, constraints);
            panel.add(birdCountLabel);
        }
        
        List<String> sections = pigeon.report.Utilities.participatingSections(club);
        for (String section: sections) {
            {
                constraints.anchor = GridBagConstraints.EAST;
                constraints.fill = GridBagConstraints.NONE;
                constraints.gridx = 0;
                constraints.gridy = sections.indexOf(section) + 1;
                constraints.weightx = 0.0;
                constraints.gridwidth = 1;
                JLabel label = new JLabel(section);
                gridbag.setConstraints(label, constraints);
                panel.add(label);
            }
            textFieldMap.put(section, new JTextField[2]);
            {
                constraints.anchor = GridBagConstraints.CENTER;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.gridx = 1;
                constraints.weightx = 1.0;
                constraints.gridwidth = 1;
                JTextField memberCountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
                memberCountField.setColumns(4);
                gridbag.setConstraints(memberCountField, constraints);
                panel.add(memberCountField);
                textFieldMap.get(section)[0] = memberCountField;
            }
            {
                constraints.anchor = GridBagConstraints.CENTER;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.gridx = 2;
                constraints.weightx = 1.0;
                constraints.gridwidth = GridBagConstraints.REMAINDER;
                JTextField birdCountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
                birdCountField.setColumns(4);
                gridbag.setConstraints(birdCountField, constraints);
                panel.add(birdCountField);
                textFieldMap.get(section)[1] = birdCountField;
            }
        }
    }
    
    /**
        Populates the JPanel which contains the input dialogs for inputting the number of
        birds entered in the pools.
    */
    private static void populatePoolEntrantsCountPanel(JPanel panel, Map<String, Map<String, JTextField>> textFieldMap, Organization club, Configuration configuration) throws IllegalArgumentException
    {
        GridBagLayout gridbag = new GridBagLayout();
        panel.setLayout(gridbag);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        List<String> sections = pigeon.report.Utilities.participatingSections(club);
        if (sections.contains("Open")) {
            throw new IllegalArgumentException("Arg!  A section called 'Open' has been used, that's too confusing!");
        }
        sections.add(0, "Open");

        for (String section: sections) {
            textFieldMap.put(section, new TreeMap<String, JTextField>());

            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.NONE;
            constraints.weightx = 1.0;
            constraints.gridwidth = 2;
            if (section == sections.get(sections.size() - 1)) {
                constraints.gridwidth = GridBagConstraints.REMAINDER;
            }
            JLabel label = new JLabel(section);
            gridbag.setConstraints(label, constraints);
            panel.add(label);
        }

        for (Competition c: configuration.getCompetitions()) {
            for (String section: sections) {
                if (c.isAvailableInOpen() || !section.equals("Open")) {
                    final int index = sections.indexOf(section);
                    
                    constraints.anchor = GridBagConstraints.EAST;
                    constraints.fill = GridBagConstraints.NONE;
                    constraints.gridx = index * 2;
                    constraints.weightx = 0.0;
                    constraints.gridwidth = 1;
                    JLabel label = new JLabel(c.getName());
                    gridbag.setConstraints(label, constraints);
                    panel.add(label);

                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridx = index * 2 + 1;
                    constraints.weightx = 1.0;
                    constraints.gridwidth = 1;
                    JTextField field = new JFormattedTextField(NumberFormat.getIntegerInstance());
                    field.setColumns(4);
                    gridbag.setConstraints(field, constraints);
                    panel.add(field);

                    textFieldMap.get(section).put(c.getName(), field);
                }
            }
        }

        panel.setFocusTraversalPolicyProvider(true);
        panel.setFocusTraversalPolicy(new TopDownFocusTraversalPolicy());
    }
    
    private static void populatePrizesPanel(JPanel panel, Map<String, List<JTextField>> fields, Organization club)
    {
        GridBagLayout gridbag = new GridBagLayout();
        panel.setLayout(gridbag);
        
        List<String> sections = pigeon.report.Utilities.participatingSections(club);
        for (String section: sections) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 10, 10, 10);
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            JLabel label = new JLabel(section);
            gridbag.setConstraints(label, constraints);
            panel.add(label);
            
            List<JTextField> list = new ArrayList<JTextField>();
            
            for (int i = 0; i < 20; ++i) {
                constraints.fill = GridBagConstraints.NONE;
                constraints.anchor = GridBagConstraints.EAST;
                constraints.gridwidth = 1;
                JLabel posLabel = new JLabel(Integer.toString(i+1) + ".");
                gridbag.setConstraints(posLabel, constraints);
                panel.add(posLabel);


                constraints.fill = GridBagConstraints.BOTH;
                constraints.anchor = GridBagConstraints.CENTER;
                if ((i % 4) == 3 || i == 19) {
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                }
                JTextField field = new JFormattedTextField(Utilities.currencyFormat());
                field.setColumns(6);
                gridbag.setConstraints(field, constraints);
                panel.add(field);
                list.add(field);
            }
            fields.put(section, list);
        }
    }
}
