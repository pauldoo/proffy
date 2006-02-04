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

public class FFT {

    // Performans a DFT on a real signal and returns the magnitude of the left
    // half.  The real values are stomped on in the process.
    public static double[] RealFFT(double[] real) {
        final int length = real.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Array length not divisible by two");
        }

        final double[] imag = new double[length];
        FFT.FFT(real, imag);
        final double[] result = new double[length / 2];
        for (int i = 0; i < length / 2; i++) {
            result[i] = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
        }
        return result;
    }
        
    
    // Performs an in place DFT on real and imag
    private static void FFT(double[] real, double[] imag) {
        if (real.length != imag.length) {
            throw new IllegalArgumentException("Array lengths differ");
        }

        final int length = real.length;
        
        if (length == 1) {
            return;
        }
        
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Array length not divisible by two");
        }
        
        final double[] even_real = new double[length / 2];
        final double[] even_imag = new double[length / 2];
        final double[] odd_real = new double[length / 2];
        final double[] odd_imag = new double[length / 2];
        
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                even_real[i / 2] = real[i];
                even_imag[i / 2] = imag[i];
            } else {
                odd_real[i / 2] = real[i];
                odd_imag[i / 2] = imag[i];
            }
        }
        
        FFT(even_real, even_imag);
        FFT(odd_real, odd_imag);
        
        for (int i = 0; i < length / 2; i++) {
            final double w = Math.PI * 2 * i / length;
            final double w_real = Math.cos(w);
            final double w_imag = Math.sin(w);
            real[i] = even_real[i] + w_real * odd_real[i] - w_imag * odd_imag[i];
            imag[i] = even_imag[i] + w_real * odd_imag[i] + w_imag * odd_real[i];
        
            real[i + length / 2] = even_real[i] - w_real * odd_real[i] + w_imag * odd_imag[i];
            imag[i + length / 2] = even_imag[i] - w_real * odd_imag[i] - w_imag * odd_real[i];
        }
    }
}

