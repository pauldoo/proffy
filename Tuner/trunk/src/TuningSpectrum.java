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
    // Time delta between samples
    private static final double TIME_STEP = 1.0 / AudioInput.SAMPLE_FREQ;
    // Samples to take in each chunk
    private static final int CHUNK_SIZE = 32768;
    
    
    private final AudioInput audioInput;
    private final int semitone;
    
    private double max_value;
    private double[] spectrum;
    
    public TuningSpectrum(AudioInput audioInput, int semitone) {
	// Set input stream for samples
	this.audioInput = audioInput;
        this.semitone = semitone;
    }
    
    // Run for CHUNK_SIZE audio samples and update display
    public void update() {
        try {
            
            double[] sample_real = new double[CHUNK_SIZE];
            double[] sample_imag = new double[CHUNK_SIZE];
	    for (int i = 0; i < CHUNK_SIZE; i++) {
		sample_real[i] = audioInput.readSample();
	    }
            
            FFT.FFT(sample_real, sample_imag);
            double max = 0;
            int max_index = 0;
            for (int i = 0; i < CHUNK_SIZE; i++) {
                sample_real[i] = Math.sqrt(sample_real[i] * sample_real[i] + sample_imag[i] * sample_imag[i]);
                if (sample_real[i] > max) {
                    max = sample_real[i];
                    max_index = i;
                }
            }
            if (max_index > CHUNK_SIZE / 2) max_index = CHUNK_SIZE - max_index;

            synchronized (this) {
                max_value = max;
                spectrum = sample_real;
            }

            repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        final int width = getWidth();
        final int height = getHeight();
        g.fillRect(0, 0, width, height);
        
        if (spectrum != null) {
            final double lower_index = Math.pow( 2.0, (semitone - 2.5) / 12 ) * MIDDLE_C * CHUNK_SIZE / AudioInput.SAMPLE_FREQ;
            final double upper_index = Math.pow( 2.0, (semitone + 2.5) / 12 ) * MIDDLE_C * CHUNK_SIZE / AudioInput.SAMPLE_FREQ;
            
            g.setColor(Color.RED);
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
            
            g.setColor(Color.WHITE);
            for (int dev = -2; dev <= 2; dev++) {
                final double index = Math.pow( 2.0, (semitone + dev) / 12.0 ) * MIDDLE_C * CHUNK_SIZE / AudioInput.SAMPLE_FREQ;
                final int pos = (int)((index - lower_index) * width / (upper_index - lower_index));
                
                g.fillRect(pos, 0, 1, height);
                
            }
        }
        
    }
    
}

