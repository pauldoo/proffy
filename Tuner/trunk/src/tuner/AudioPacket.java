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

/**
    A short snippet of mono audio.
*/
public final class AudioPacket
{
    private final double[] samples;
    private final double sampleFrequency;

    public AudioPacket(double[] samples, double sampleFrequency)
    {
        this.samples = samples;
        this.sampleFrequency = sampleFrequency;
    }
    
    public double[] getSamples()
    {
        return samples;
    }

    /**
        The frequency at which the audio samples were captured.
    */
    public double getSampleFrequencyInHz()
    {
        return sampleFrequency;
    }
}
