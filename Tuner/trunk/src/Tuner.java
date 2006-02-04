/*

Tuner, a simple application to help you tune your musical instrument.
Copyright (C) 2003-2005 Paul Richards

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

public class Tuner extends JFrame implements ActionListener, Runnable {
    
    // Version number of application
    private static final String VERSION = "0.2";
    // Copyright message
    private static final String COPYRIGHT =
        "Tuner version " + VERSION + ", Copyright (C) 2003-2005 Paul Richards\n" +
        "Tuner comes with ABSOLUTELY NO WARRANTY; for details\n" +
        "see the file COPYRIGHT.TXT.  This is free software, and you are welcome\n" +
        "to redistribute it under certain conditions; see the file COPYRIGHT.TXT\n" +
        "for details.";
    // Window title
    private static final String TITLE = "Tuner v" + VERSION;
    // Credits message
    private static final String CREDITS = "Made by Paul Richards, pauldoo@users.sf.net";
    
    // Instrument names
    private final String[] instruments;
    
    // For each instrument, the list of note semitones and names
    private final int[][] semitones;
    private final String[][] names;
    
    // AudioInput object connected to microphone
    private AudioInput audioInput;
    
    // GUI components
    private JPanel chooserPanel;
    private JComboBox instrumentBox;
    private JComboBox noteBox;
    private TuningSpectrum spectrum;
    
    // Main
    public static void main(String[] args) throws Exception {
        JOptionPane.showMessageDialog( null, COPYRIGHT );

	InputStream in = new BufferedInputStream( new FileInputStream( "tuner.properties" ) );    
	Properties props = new Properties();
	props.load( in );
	
	int instrumentCount = Integer.parseInt( props.getProperty("InstrumentCount") );
	String[] instruments = new String[ instrumentCount ];
	int[][] semitones = new int[instrumentCount][];
	String[][] names = new String[instrumentCount][];
	
	for (int i = 0; i < instrumentCount; i++) {
	    String iPrefix = "Instrument_" + i;
	    String name = props.getProperty(iPrefix + ".Name");
	    instruments[i] = name;
	    int noteCount = Integer.parseInt(props.getProperty(iPrefix + ".NoteCount"));
	    semitones[i] = new int[noteCount];
	    names[i] = new String[noteCount];
	    for (int j = 0; j < noteCount; j++) {
		String nPrefix = iPrefix + ".Note_" + j;
		int semitone = Integer.parseInt(props.getProperty(nPrefix + ".Semitone" ));
		semitones[i][j] = semitone;
		String noteName = props.getProperty(nPrefix + ".Name" );
		names[i][j] = noteName;
	    }
	}
	
        Tuner t = new Tuner( TITLE, instruments, semitones, names );
        t.launch();
        
    }
    
    public Tuner( String title, String[] instruments, int[][] semitones, String[][] names ) {
        super( title );
        this.setDefaultCloseOperation( EXIT_ON_CLOSE );
        this.instruments = instruments;
        this.semitones = semitones;
        this.names = names;
        
	chooserPanel = new JPanel(new GridLayout( 1, 2 ));
	instrumentBox = new JComboBox( instruments );
	instrumentBox.addActionListener( this );
	noteBox = new JComboBox();
	noteBox.addActionListener( this );
	chooserPanel.add( instrumentBox );
	
        this.getContentPane().add( chooserPanel, BorderLayout.NORTH );
        
        JLabel credits = new JLabel( CREDITS );
        this.getContentPane().add( credits, BorderLayout.SOUTH );
    }
    
    
    public void launch() throws Exception {
	this.audioInput = AudioInput.createMicrophoneInput();

        updateInstrument();
        this.setVisible(true);
	
        new Thread(this, "Analyzer").start();
    }
    
    private int getNoteNumber() {
	return noteBox.getSelectedIndex();
    }
    
    private void updateNote() {
	int noteNumber = getNoteNumber();
	if (spectrum != null) {
	    this.getContentPane().remove( spectrum );
	}
	int[] iSemitones = semitones[getInstrumentNumber()];
	spectrum = new TuningSpectrum( audioInput, iSemitones[noteNumber] );
	this.getContentPane().add( spectrum, BorderLayout.CENTER );
        this.pack();
    }
    
    private int getInstrumentNumber() {
	return instrumentBox.getSelectedIndex();
    }
    
    private void updateInstrument() {
	int instrumentNumber = getInstrumentNumber();
	if (noteBox != null) {
	    chooserPanel.remove( noteBox );
	}
	String[] noteNames = names[instrumentNumber];
	noteBox = new JComboBox( noteNames );
	noteBox.addActionListener( this );
	chooserPanel.add( noteBox );
	updateNote();
    }
    
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == instrumentBox) {
	    updateInstrument();
	} else if (e.getSource() == noteBox) {
	    updateNote();
	}
	
    }
    
    public void run() {
        try {
            while (true) {
		spectrum.update();
	    }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

