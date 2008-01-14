/*
    Copyright (C) 2007  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it justUnder the terms of the GNU General Public License as published by
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

import org.junit.Test;
import static org.junit.Assert.*;


public final class SinglePickupTest
{
    @Test
    public void process()
    {
        AudioPacket packet = (new SingleFrequencyAudioSource(2000)).nextPacket();
        
        for (double damping = 0.5; damping <= 6.0; damping += 0.5) {
            double bestFrequency = 0;
            double bestResponse = 0;
            for (double frequency = 1980; frequency <= 2020; frequency += 1.0) {
                SinglePickup pickup = new SinglePickup(frequency, damping);
                pickup.process(packet);
                double response = pickup.getResponseMeasure();
                if (response > bestResponse) {
                    bestFrequency = frequency;
                    bestResponse = response;
                }
            }
            //System.out.println(bestFrequency);
            assertTrue("Pickup at 2000Hz should have the best response", bestFrequency == 2000);
        }
    }
}