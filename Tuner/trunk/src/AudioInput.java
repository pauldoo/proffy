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


import java.io.*;
import javax.sound.sampled.*;

public class AudioInput {

    // Sound recording sample frequency
    public static final float SAMPLE_FREQ = 44100.0F;

    private DataInputStream dis;
    
    
    public AudioInput(DataInputStream dis) {
	this.dis = dis;
    }
    
    public int readSample() throws IOException {
        return dis.readShort();
    }

    // creates an AudioInput object connected to the microphone
    public static AudioInput createMicrophoneInput() throws Exception {
        AudioFormat audioFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, SAMPLE_FREQ, 16, 1, 2, SAMPLE_FREQ, true );
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(audioFormat);
        
        AudioInputStream ais = new AudioInputStream( targetDataLine );
        targetDataLine.start();
        return new AudioInput( new DataInputStream( new BufferedInputStream( ais ) ) );
    }
    
    // creates an AudioInput object which is simulated
    public static AudioInput createSimulatedInput(double frequency) throws Exception {
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

