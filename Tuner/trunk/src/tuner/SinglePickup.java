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

public final class SinglePickup
{
    private final double tuneFrequencyInHz;
    private final double damping;
    
    private double position = 0.0;
    private double velocity = 0.0;
    private double currentAmplitude = 0.0;
    private double currentOffset = 0.0;
    
    public SinglePickup(double tuneFrequencyInHz, double damping)
    {
        this.tuneFrequencyInHz = tuneFrequencyInHz;
        this.damping = damping;
    }
    
    private final double springConstant()
    {
        return
            Math.pow(tuneFrequencyInHz * 2.0 * Math.PI, 2.0) +
            Math.pow(damping, 2.0) / 4.0;
    }
    
    public synchronized void process(AudioPacket packet)
    {
        final double timeStep = 1.0 / packet.getSampleFrequencyInHz();
        
        currentAmplitude = 0.0;
        for (double sample: packet.getSamples()) {
            final double offsettedSample = sample - currentOffset;
            currentOffset += Math.signum(offsettedSample) * 0.1;
            final double force = offsettedSample + (-damping * velocity) + (-springConstant() * position);
            
            if (Double.isNaN(force) || Double.isInfinite(force)) {
                throw new RuntimeException("Force Hit: "+ force);
            }

            velocity += 1.0 * force * timeStep;
            position += velocity * timeStep;
            
            currentAmplitude = Math.max(currentAmplitude, Math.abs(position));
            if (Double.isNaN(currentAmplitude) || Double.isInfinite(currentAmplitude)) {
                throw new RuntimeException("Amplitude Hit: "+ force);
            }
        }
    }
    
    public synchronized double getResponseMeasure()
    {
        return 0.5 * springConstant() * Math.pow(currentAmplitude, 2.0);
    }
}
