/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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

package pigeon.view;

import java.awt.Component;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import pigeon.competitions.Competition;
import pigeon.model.Constants;
import pigeon.model.Season;
import pigeon.model.Sex;
import pigeon.model.Time;
import pigeon.model.ValidationException;

/**
 * Form to let the user enter a single ring number and clocking time.
 */
final class RingTimeEditor extends javax.swing.JPanel
{
    private static final long serialVersionUID = 1436323402423205110L;

    private Time time;
    private final int numberOfDaysCovered;
    private final Season season;
    private final Configuration configuration;
    private final Map<String, JCheckBox> openCompetitionCheckboxes = new TreeMap<String, JCheckBox>();
    private final Map<String, JCheckBox> sectionCompetitionCheckboxes = new TreeMap<String, JCheckBox>();

    public RingTimeEditor(Time time, int numberOfDaysCovered, Season season, Configuration configuration)
    {
        this.time = time;
        this.numberOfDaysCovered = numberOfDaysCovered;
        this.season = season;
        this.configuration = configuration;
        initComponents();
        addComboOptions();
        addCompetitions();

        updateGui();
    }

    private void updateGui()
    {
        ringNumberText.setText(time.getRingNumber());
        dayCombo.setSelectedIndex((int)(time.getMemberTime() / Constants.MILLISECONDS_PER_DAY));
        clockTime.setDate(new Date(time.getMemberTime() % Constants.MILLISECONDS_PER_DAY));
        birdColorCombo.setSelectedItem(time.getColor());
        birdSexCombo.setSelectedItem(time.getSex());

        switch (configuration.getMode()) {
            case FEDERATION:
                for (String name: time.getOpenCompetitionsEntered()) {
                    openCompetitionCheckboxes.get(name).setSelected(true);
                }
                for (String name: time.getSectionCompetitionsEntered()) {
                    sectionCompetitionCheckboxes.get(name).setSelected(true);
                }
                break;

            case CLUB:
                openPoolsLabel.setText("Pools");
                sectionPoolsLabel.setEnabled(false);
                sectionPoolsPanel.setEnabled(false);
                for (JCheckBox checkBox: sectionCompetitionCheckboxes.values()) {
                    checkBox.setEnabled(false);
                }
                this.remove(sectionPoolsLabel);
                this.remove(sectionPoolsPanel);

                for (String name: time.getOpenCompetitionsEntered()) {
                    openCompetitionCheckboxes.get(name).setSelected(true);
                }
                break;

            default:
                throw new IllegalArgumentException("Unexpected application mode: " + configuration.getMode());
        }
    }

    /**
        Finds the names of all the checkboxes that have been ticked.
    */
    private Set<String> findSelectedBoxes(Map<String, JCheckBox> checkboxes)
    {
        Set<String> result = new TreeSet<String>();
        for (Map.Entry<String, JCheckBox> entry: checkboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private void updateTimeObject() throws ValidationException
    {
        time = time.repSetRingNumber(ringNumberText.getText());
        try {
            long memberTime =
                    ((new Integer(dayCombo.getSelectedItem().toString()) - 1) * Constants.MILLISECONDS_PER_DAY) +
                    clockTime.getDate().getTime();
            time = time.repSetMemberTime(memberTime, numberOfDaysCovered);
        } catch (java.text.ParseException e) {
            throw new ValidationException("Clock in time is invalid, expecting " + clockTime.getFormatPattern(), e);
        }
        time = time.repSetColor(((String)birdColorCombo.getSelectedItem()).trim());
        time = time.repSetSex((Sex)birdSexCombo.getSelectedItem());
        

        switch (configuration.getMode()) {
            case FEDERATION:
                time = time.repSetOpenCompetitionsEntered(findSelectedBoxes(openCompetitionCheckboxes));
                time = time.repSetSectionCompetitionsEntered(findSelectedBoxes(sectionCompetitionCheckboxes));
                break;
            case CLUB:
                time = time.repSetOpenCompetitionsEntered(findSelectedBoxes(openCompetitionCheckboxes));
                break;

            default:
                throw new IllegalArgumentException("Unexpected application mode: " + configuration.getMode());
        }
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
        ringNumberText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        clockTime = new pigeon.view.DateTimeComponent();
        jLabel5 = new javax.swing.JLabel();
        dayCombo = new javax.swing.JComboBox();
        openPoolsLabel = new javax.swing.JLabel();
        openPoolsPanel = new javax.swing.JPanel();
        sectionPoolsLabel = new javax.swing.JLabel();
        sectionPoolsPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        birdColorCombo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        birdSexCombo = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Ring Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jLabel1, gridBagConstraints);

        ringNumberText.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ringNumberTextActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(ringNumberText, gridBagConstraints);

        jLabel4.setText("Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(clockTime, gridBagConstraints);

        jLabel5.setText("Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(dayCombo, gridBagConstraints);

        openPoolsLabel.setText("Open Pools");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(openPoolsLabel, gridBagConstraints);

        openPoolsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(openPoolsPanel, gridBagConstraints);

        sectionPoolsLabel.setText("Section Pools");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(sectionPoolsLabel, gridBagConstraints);

        sectionPoolsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(sectionPoolsPanel, gridBagConstraints);

        jLabel6.setText("Bird Colour");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jLabel6, gridBagConstraints);

        birdColorCombo.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(birdColorCombo, gridBagConstraints);

        jLabel7.setText("Bird Sex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(birdSexCombo, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void ringNumberTextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ringNumberTextActionPerformed
    {//GEN-HEADEREND:event_ringNumberTextActionPerformed
        String ringNumber = ringNumberText.getText().trim();
        Time bird = Utilities.findBirdEntry(season, ringNumber);
        if (bird != null) {
            if (((String)birdColorCombo.getSelectedItem()).trim().length() == 0) {
                birdColorCombo.setSelectedItem(bird.getColor());
            }
            birdSexCombo.setSelectedItem(bird.getSex());
        }
    }//GEN-LAST:event_ringNumberTextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox birdColorCombo;
    private javax.swing.JComboBox birdSexCombo;
    private pigeon.view.DateTimeComponent clockTime;
    private javax.swing.JComboBox dayCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel openPoolsLabel;
    private javax.swing.JPanel openPoolsPanel;
    private javax.swing.JTextField ringNumberText;
    private javax.swing.JLabel sectionPoolsLabel;
    private javax.swing.JPanel sectionPoolsPanel;
    // End of variables declaration//GEN-END:variables

    private static Time editEntry(Component parent, Time time, int numberOfDaysCovered, Season season, Configuration configuration, boolean newTime) throws UserCancelledException
    {
        RingTimeEditor panel = new RingTimeEditor(time, numberOfDaysCovered, season, configuration);
        while (true)
        {
            Object[] options = { (newTime ? "Add" : "Ok"), "Cancel" };
            int result = JOptionPane.showOptionDialog(parent, panel, "Ring Information", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == 0)
            {
                try
                {
                    panel.updateTimeObject();
                    break;
                }
                catch (ValidationException e)
                {
                    e.displayErrorDialog(panel);
                }
            }
            else
            {
                result = JOptionPane.showConfirmDialog(parent, "Return to Clock window and discard these changes?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION)
                {
                    throw new UserCancelledException();
                }
            }
        }
        return panel.time;
    }

    public static Time editEntry(Component parent, Time time, int numberOfDaysCovered, Season season, Configuration configuration) throws UserCancelledException
    {
        return editEntry(parent, time, numberOfDaysCovered, season, configuration, false);
    }

    public static Time createEntry(Component parent, int numberOfDaysCovered, Season season, Configuration configuration) throws UserCancelledException
    {
        return editEntry(parent, Time.createEmpty(), numberOfDaysCovered, season, configuration, true);
    }

    private void addCompetitions()
    {
        for (Competition comp: configuration.getCompetitions()) {
            if (comp.isAvailableInOpen()) {
                JCheckBox checkBox = new JCheckBox(comp.getName());
                openCompetitionCheckboxes.put(comp.getName(), checkBox);
                openPoolsPanel.add(checkBox);
            }
        }
        for (Competition comp: configuration.getCompetitions()) {
            JCheckBox checkBox = new JCheckBox(comp.getName());
            sectionCompetitionCheckboxes.put(comp.getName(), checkBox);
            sectionPoolsPanel.add(checkBox);
        }
    }

    private void addComboOptions()
    {
        for (int day = 1; day <= numberOfDaysCovered; day++)
        {
            String str = new Integer(day).toString();
            dayCombo.addItem(str);
        }

        clockTime.setMode(DateTimeDisplayMode.HOURS_MINUTES_SECONDS);

        for (String color: Utilities.getBirdColors(season)) {
            birdColorCombo.addItem(color);
        }
        
        for (Sex sex: Sex.values()) {
            birdSexCombo.addItem(sex);
        }
    }
}
