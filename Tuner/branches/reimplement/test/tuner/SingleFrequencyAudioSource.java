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

final class SingleFrequencyAudioSource implements AudioSource
{
    final double frequencyInHz;
    long samplesTaken = 0;
    
    public SingleFrequencyAudioSource(double frequencyInHz)
    {
        this.frequencyInHz = frequencyInHz;
    }
        
    public double getFrequencyInHz()
    {
        return frequencyInHz;
    }
    
    public double getSampleFrequencyInHz()
    {
        return 200000;
    }

    public void close()
    {
    }
    
    public AudioPacket nextPacket()
    {
        int numberOfSamples = (int)(getSampleFrequencyInHz() * 0.2); // 1/5th of a second worth
        double[] samples = new double[numberOfSamples];
        for (int i = 0; i < samples.length; i++) {
            double w = (samplesTaken / getSampleFrequencyInHz()) * (2 * Math.PI * getFrequencyInHz());
            samples[i] = Math.cos(w);
            samplesTaken++;
        }
        return new AudioPacket(samples, getSampleFrequencyInHz());
    }
}
