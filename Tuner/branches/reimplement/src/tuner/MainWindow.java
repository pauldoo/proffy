/*
    Copyright (C) 2007  Paul Richards.

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

package tuner;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class MainWindow extends JFrame
{
    private static final long serialVersionUID = 1062351301126993589L;

    private final Configuration configuration;
    private final SpectralView spectralView;
    private Thread updateThread;
    private JComboBox instrumentPicker;
    private JComboBox stringPicker;
    
    private final class LocalItemListener implements ItemListener
    {
        public void itemStateChanged(ItemEvent e)
        {
            if (e.getSource() == instrumentPicker) {
                updateSelectedInstrument();
            } else if (e.getSource() == stringPicker) {
                int index = stringPicker.getSelectedIndex();
                if (index != -1) {
                    updateSelectedString();
                }
            } else {
                throw new IllegalArgumentException("Unexpected event source");
            }
        }
    }
    
    private MainWindow(Configuration configuration) throws LineUnavailableException
    {
        this.configuration = configuration;

        BorderLayout layout = new BorderLayout();
        this.getContentPane().setLayout(layout);

        FrequencyCurve frequencyCurve = new FrequencyCurve();
        this.spectralView = frequencyCurve;

        this.getContentPane().add(frequencyCurve, BorderLayout.CENTER);
        this.getContentPane().add(createButtonPanel(), BorderLayout.NORTH);

        pack();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        updateThread.interrupt();
        updateThread = null;
    }
    
    public void startUpdateThread() throws LineUnavailableException
    {
        AudioSource audioSource = new MicrophoneAudioSource();
        updateThread = new Thread(new UpdateRunner(audioSource, spectralView));
        updateThread.setDaemon(true);
        updateThread.start();
    }
    
    private JPanel createButtonPanel()
    {
        LayoutManager layout = new GridLayout(1, 2);
        JPanel buttonPanel = new JPanel(layout);
        
        instrumentPicker = new JComboBox(configuration.instrumentNames().toArray());
        instrumentPicker.addItemListener(new LocalItemListener());
        buttonPanel.add(instrumentPicker);

        stringPicker = new JComboBox();
        stringPicker.addItemListener(new LocalItemListener());
        buttonPanel.add(stringPicker);
        
        instrumentPicker.setSelectedIndex(0);
        updateSelectedInstrument();
        stringPicker.setSelectedIndex(0);
        updateSelectedString();
        return buttonPanel;
    }
    
    private String getSelectedInstrument()
    {
        return (String)instrumentPicker.getSelectedItem();
    }
    
    private String getSelectedString()
    {
        return (String)stringPicker.getSelectedItem();
    }

    private void updateSelectedInstrument()
    {
        stringPicker.removeAllItems();
        for (String stringName: configuration.stringNames(getSelectedInstrument())) {
            stringPicker.addItem(stringName);
        }
    }
    
    private void updateSelectedString()
    {
        double semitone = configuration.stringSemitone(getSelectedInstrument(), getSelectedString());
        double targetFrequency = configuration.frequencyInHzOfMiddleC() * Math.pow(2.0, semitone / 12);
        spectralView.setTargetFrequency(targetFrequency);
    }
    
    public static void main(String[] args) throws LineUnavailableException, IOException
    {
        InputStream stream = new BufferedInputStream(new FileInputStream("instruments.xml"));
        Configuration configuration;
        try {
            configuration = Configuration.loadFromStream(stream);
        } finally {
            stream.close();
            stream = null;
        }
        
        MainWindow window = new MainWindow(configuration);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.startUpdateThread();
    }
}
