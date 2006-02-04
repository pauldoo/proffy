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
import java.io.*;
import javax.swing.*;


public class TuningSpectrum extends JComponent {
    
    // Frequency of middle C in Hz
    private static final double MIDDLE_C = 261.6256;
    // Samples to take in each chunk
    private static final int CHUNK_SIZE = 1 << 15;
    
    
    private final AudioInput audioInput;
    private final int semitone;
    
    private double max_value;
    private double[] spectrum;
    
    public TuningSpectrum(AudioInput audioInput, int semitone) {
	// Set input stream for samples
	this.audioInput = audioInput;
        this.semitone = semitone;
        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(200, 200));
    }
    
    // Run for CHUNK_SIZE audio samples and update display
    public void update() {
        try {
            
            final double[] samples = new double[CHUNK_SIZE];
	    for (int i = 0; i < CHUNK_SIZE; i++) {
		samples[i] = audioInput.readSample();
	    }
            final double[] magnitudes = FFT.RealFFT(samples);
            
            int max_index = 0;
            for (int i = 0; i < magnitudes.length; i++) {
                if (magnitudes[i] > magnitudes[max_index]) {
                    max_index = i;
                }
            }

            synchronized (this) {
                max_value = magnitudes[max_index];
                spectrum = magnitudes;
            }

            repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static double frequencyToIndex(double freq) {
        return freq * CHUNK_SIZE / AudioInput.SAMPLE_FREQ;
    }
    
    private static double semitoneToFrequency(double semitone) {
        return Math.pow( 2.0, semitone / 12.0 ) * MIDDLE_C;
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        final int width = getWidth();
        final int height = getHeight();
        g.fillRect(0, 0, width, height);
        
        synchronized (this) {
            if (spectrum != null) {
                final double lower_index = frequencyToIndex(semitoneToFrequency(semitone - 2.5));
                final double upper_index = frequencyToIndex(semitoneToFrequency(semitone + 2.5));
                
                g.setColor(Color.RED);
                for (int index = (int)lower_index; index <= (int)upper_index; index++) {
                    final int pos = (int)((index - lower_index) * width / (upper_index - lower_index));
                    g.fillRect(pos, 0, 1, (int)(spectrum[index] * height / max_value));
                }
                
                /*
                for (int i = 0; i < width; i++) {
                    int a = (int)(lower_index + i * (upper_index - lower_index) / width);
                    int b = (int)(lower_index + (i+1) * (upper_index - lower_index) / width);
                    double v = 0;
                    for (int j = a; j < b; j++) {
                        if (spectrum[j] > v) {
                            v = spectrum[j];
                        }
                    }
                    g.fillRect(i, 0, 1, (int)(v * height / max_value));
                }
                */
                
                g.setColor(Color.WHITE);
                System.out.println( semitoneToFrequency(semitone) );
                for (int dev = -2; dev <= 2; dev++) {
                    final double index = frequencyToIndex(semitoneToFrequency(semitone + dev));
                    System.out.print( index + "\t" );
                    final int pos = (int)((index - lower_index) * width / (upper_index - lower_index));
                    
                    g.fillRect(pos, 0, 1, height);
                }
                System.out.println();
            }
        }
        
    }
    
}

