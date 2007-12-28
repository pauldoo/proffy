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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
    An implementation of AudioSource that uses the computer's microphone.
*/
final class MicrophoneAudioSource implements AudioSource, Closeable
{
    static final float SAMPLE_FREQUENCY = 44100.0f;

    private DataInputStream stream;
    
    public MicrophoneAudioSource() throws LineUnavailableException
    {
        AudioFormat audioFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, SAMPLE_FREQUENCY, 16, 1, 2, SAMPLE_FREQUENCY, true );
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(audioFormat);
        
        AudioInputStream ais = new AudioInputStream( targetDataLine );
        targetDataLine.start();
        
        this.stream = new DataInputStream(new BufferedInputStream( ais ));
    }

    public synchronized void close()
    {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                stream = null;
            }
        }
    }
    
    public synchronized AudioPacket nextPacket()
    {
        try {
            final double[] samples = new double[(int)(SAMPLE_FREQUENCY * 0.03)]; // Take 30ms of audio at a time
            final byte[] dataPacket = new byte[samples.length * 2];
            stream.readFully(dataPacket);
            final DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(dataPacket));
            for (int i = 0; i < samples.length; i++) {
                samples[i] = dataStream.readShort();
            }
            return new AudioPacket(samples, SAMPLE_FREQUENCY);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
