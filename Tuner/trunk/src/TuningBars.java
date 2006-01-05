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

public class TuningBars extends JPanel {
    
    // Frequency of middle C in Hz
    private static final double MIDDLE_C = 261.6256;
    // Number of bars to display
    private static final int SAMPLE_COUNT = 7;
    // Sound recording sample frequency
    private static final float SAMPLE_FREQ = 44100.0F;
    // Time delta between samples
    private static final double TIME_STEP = 1.0 / SAMPLE_FREQ;
    
    
    // Damping factor for bars
    private static final double damping = 6.0;
    
    private double[] springConst = new double[ SAMPLE_COUNT ];
    private AudioInput audioInput;
    
    private JProgressBar[] bars = new JProgressBar[ SAMPLE_COUNT ];
    
    double pos[] = new double[SAMPLE_COUNT];
    double vel[] = new double[SAMPLE_COUNT];
    
    
    public TuningBars(AudioInput audioInput, int semitone) {
	super( new BorderLayout() );

        JPanel labelPanel = new JPanel(new GridLayout( SAMPLE_COUNT, 1 ));
        JPanel barPanel = new JPanel(new GridLayout( SAMPLE_COUNT, 1 ));
	
	// Set input stream for samples
	this.audioInput = audioInput;

	// Configure the bars to match desired semitone
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            JLabel label = new JLabel();
            bars[i] = new JProgressBar(0, 1000);


            double deviance = (double)((i - (SAMPLE_COUNT / 2))) / (SAMPLE_COUNT / 2);
            deviance *= Math.abs(deviance);
            double frequency = Math.pow( 2.0, (semitone + deviance) / 12 ) * MIDDLE_C;
            
            double dev = (int)(deviance * 100) / 100.0;
            double freq = (int)(frequency * 100) / 100.0;
            if ( i == (SAMPLE_COUNT/2) ) {
                label.setText( "Target note (" + freq + "Hz)" );
            } else {
                label.setText( dev + "s" );
            }
            
            double w0 = frequency * 2.0 * Math.PI;
            springConst[i] = w0 * w0;


            labelPanel.add( label );
            barPanel.add( bars[i] );
        }


        this.add( labelPanel, BorderLayout.WEST );
        this.add( barPanel, BorderLayout.CENTER );
    }
    
    // Run for 10000 audio samples and update bar displays
    public void update() {
        try {
            
            double maxPos[] = new double[SAMPLE_COUNT];
	    
	    for (int i = 0; i < 10000; i++) {
		int samp = audioInput.readSample();
		
		for (int j = 0; j < SAMPLE_COUNT; j++) {
		    double force =
		    samp + // applied force
		    (-vel[j] * damping) + // damping force
		    (-pos[j] * springConst[j]); // spring constant
		    
		    vel[j] += force * TIME_STEP;
		    
		    pos[j] += vel[j] * TIME_STEP;
		    if ( pos[j] > maxPos[j] )
			maxPos[j] = pos[j];
		}
	    }
	    
	    double max = 0.0;
	    for (int i = 0; i < SAMPLE_COUNT; i++) {
		if ( maxPos[i] > max )
		    max = maxPos[i];
	    }
	    //System.out.println( max );
	    for (int i = 0; i < SAMPLE_COUNT; i++) {
		bars[i].setValue( (int)( 1000.0 * maxPos[i] / max ) );
	    }
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // creates an AudioInput object connected to the microphone
    public static AudioInput createAudioInput() throws Exception {
        AudioFormat audioFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, SAMPLE_FREQ, 16, 1, 2, SAMPLE_FREQ, true );
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(audioFormat);
        
        AudioInputStream ais = new AudioInputStream( targetDataLine );
        targetDataLine.start();
        return new AudioInput( new DataInputStream( new BufferedInputStream( ais ) ) );
    }
    
    // creates an AudioInput object which is simulated
    public static AudioInput createSimulatedAudioInput(double frequency) throws Exception {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream( pos );
        final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(pos));
        DataInputStream dis = new DataInputStream(new BufferedInputStream(pis));
        final double freq = frequency;
        
        new Thread(new Runnable(){
            public void run() {
                try {
                    long fn = 0;
                    long startTime = System.currentTimeMillis();
                    while(true) {
                        for (int i = 0; i < 5000; i++) {
                            short val = (short)(Math.sin( (fn * freq * 2.0 * Math.PI) / SAMPLE_FREQ ) * 32000);
                            dos.writeShort( val );
                            fn++;
                        }
                        long sleepTime = (long)((fn * 1000) / SAMPLE_FREQ) - (System.currentTimeMillis() - startTime); 
                        if ( sleepTime > 0 )
                            Thread.sleep( sleepTime );
                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
        return new AudioInput( dis );
    }
    
    
}

