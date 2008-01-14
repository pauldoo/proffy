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

final class UpdateRunner implements Runnable
{
    final AudioSource audioSource;
    final SpectralView spectralView;
    
    public UpdateRunner(AudioSource audioSource, SpectralView spectralView)
    {
        this.audioSource = audioSource;
        this.spectralView = spectralView;
    }
    
    public void run()
    {
        Thread.currentThread().setName("UpdateRunner");
        try {
            while (!Thread.interrupted()) {
                AudioPacket packet = audioSource.nextPacket();
                spectralView.process(packet);
            }
        } finally {
            audioSource.close();
        }
    }
}
