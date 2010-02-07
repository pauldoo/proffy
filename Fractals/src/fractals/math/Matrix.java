/*
    Copyright (C) 2009, 2010  Paul Richards.

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

package fractals.math;

import fractals.Utilities;
import java.util.Arrays;

/**
    Immutable matrix class (over reals).
*/
public final class Matrix implements Comparable<Matrix>
{
    private Matrix(double[][] values)
    {
        this.values = values;
        if (values.length <= 0 || values[0].length <= 0) {
            throw new IllegalArgumentException("Zero length array");
        }
        for (int i = 1; i < values.length; i++) {
            if (values[i].length != values[0].length) {
                throw new IllegalArgumentException("Array not square");
            }
        }
    }

    public static Matrix createIdentity(int size)
    {
        double[][] values = allocateArray(size, size);
        for (int i = 0; i < size; i++) {
            values[i][i] = 1.0;
        }
        return new Matrix(values);
    }

    /**
        Static constructor for matrices with 1 row and 3 columns.
    */
    public static Matrix create1x3(
            double a, double b, double c)
    {
        double[][] values = allocateArray(1, 3);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;
        return new Matrix(values);
    }

    /**
        Static constructor for matrices with 2 rows and 2 columns.
    */
    public static Matrix create2x2(
            double a, double b,
            double c, double d)
    {
        double[][] values = allocateArray(2, 2);
        values[0][0] = a;
        values[0][1] = b;
        values[1][0] = c;
        values[1][1] = d;
        return new Matrix(values);
    }

    /**
        Creates the 3x3 rotation matrix represented
        by the normalized quaternion.
    */
    public static Matrix create3x3(
            Quaternion q)
    {
        // http://en.wikipedia.org/w/index.php?title=Quaternions_and_spatial_rotation&oldid=337794355#From_a_quaternion_to_an_orthogonal_matrix
        return create3x3(
                q.a*q.a + q.b*q.b - q.c*q.c - q.d*q.d,
                2.0*q.b*q.c - 2.0*q.a*q.d,
                2.0*q.b*q.d + 2.0*q.a*q.c,

                2.0*q.b*q.c + 2.0*q.a*q.d,
                q.a*q.a - q.b*q.b + q.c*q.c - q.d*q.d,
                2.0*q.c*q.d - 2.0*q.a*q.b,

                2.0*q.b*q.d - 2.0*q.a*q.c,
                2.0*q.c*q.d + 2.0*q.a*q.b,
                q.a*q.a - q.b*q.b - q.c*q.c + q.d*q.d);
    }

    public static Matrix create4x4(
            Triplex translation,
            Quaternion rotation)
    {
        return Matrix.multiply(
                Matrix.extendWithIdentity(Matrix.create3x3(rotation), 4, 4),
                Matrix.create4x4(
                    1.0, 0.0, 0.0, translation.x,
                    0.0, 1.0, 0.0, translation.y,
                    0.0, 0.0, 1.0, translation.z,
                    0.0, 0.0, 0.0, 1.0));
    }

    public static Matrix create3x3(
            double a, double b, double c,
            double d, double e, double f,
            double g, double h, double i)
    {
        double[][] values = allocateArray(3, 3);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;

        values[1][0] = d;
        values[1][1] = e;
        values[1][2] = f;

        values[2][0] = g;
        values[2][1] = h;
        values[2][2] = i;
        return new Matrix(values);
    }

    public static Matrix create3x4(
            double a, double b, double c, double d,
            double e, double f, double g, double h,
            double i, double j, double k, double l)
    {
        double[][] values = allocateArray(3, 4);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;
        values[0][3] = d;
        values[1][0] = e;
        values[1][1] = f;
        values[1][2] = g;
        values[1][3] = h;
        values[2][0] = i;
        values[2][1] = j;
        values[2][2] = k;
        values[2][3] = l;
        return new Matrix(values);
    }

    public static Matrix create4x1(
            double a,
            double b,
            double c,
            double d)
    {
        double [][] values = allocateArray(4, 1);
        values[0][0] = a;
        values[1][0] = b;
        values[2][0] = c;
        values[3][0] = d;
        return new Matrix(values);
    }

    public static Matrix create4x4(
            double a, double b, double c, double d,
            double e, double f, double g, double h,
            double i, double j, double k, double l,
            double m, double n, double o, double p)
    {
        double[][] values = allocateArray(4, 4);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;
        values[0][3] = d;
        values[1][0] = e;
        values[1][1] = f;
        values[1][2] = g;
        values[1][3] = h;
        values[2][0] = i;
        values[2][1] = j;
        values[2][2] = k;
        values[2][3] = l;
        values[3][0] = m;
        values[3][1] = n;
        values[3][2] = o;
        values[3][3] = p;
        return new Matrix(values);
    }

    /**
        Static constructor for matrices of 3 rows and 5 columns.
    */
    public static Matrix create3x5(
            double a, double b, double c, double d, double e,
            double f, double g, double h, double i, double j,
            double k, double l, double m, double n, double o)
    {
        double[][] values = allocateArray(3, 5);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;
        values[0][3] = d;
        values[0][4] = e;
        values[1][0] = f;
        values[1][1] = g;
        values[1][2] = h;
        values[1][3] = i;
        values[1][4] = j;
        values[2][0] = k;
        values[2][1] = l;
        values[2][2] = m;
        values[2][3] = n;
        values[2][4] = o;
        return new Matrix(values);
    }

    /**
        Static constructor for matrices of 5 rows and 5 columns.
    */
    public static Matrix create5x5(
            double a, double b, double c, double d, double e,
            double f, double g, double h, double i, double j,
            double k, double l, double m, double n, double o,
            double p, double q, double r, double s, double t,
            double u, double v, double w, double x, double y)
    {
        double[][] values = allocateArray(5, 5);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;
        values[0][3] = d;
        values[0][4] = e;
        values[1][0] = f;
        values[1][1] = g;
        values[1][2] = h;
        values[1][3] = i;
        values[1][4] = j;
        values[2][0] = k;
        values[2][1] = l;
        values[2][2] = m;
        values[2][3] = n;
        values[2][4] = o;
        values[3][0] = p;
        values[3][1] = q;
        values[3][2] = r;
        values[3][3] = s;
        values[3][4] = t;
        values[4][0] = u;
        values[4][1] = v;
        values[4][2] = w;
        values[4][3] = x;
        values[4][4] = y;
        return new Matrix(values);
    }

    /**
        Static constructor for matrices of 5 rows and 3 columns.
    */
    public static Matrix create5x3(
            double a, double b, double c,
            double d, double e, double f,
            double g, double h, double i,
            double j, double k, double l,
            double m, double n, double o)
    {
        double[][] values = allocateArray(5, 3);
        values[0][0] = a;
        values[0][1] = b;
        values[0][2] = c;
        values[1][0] = d;
        values[1][1] = e;
        values[1][2] = f;
        values[2][0] = g;
        values[2][1] = h;
        values[2][2] = i;
        values[3][0] = j;
        values[3][1] = k;
        values[3][2] = l;
        values[4][0] = m;
        values[4][1] = n;
        values[4][2] = o;
        return new Matrix(values);
    }

    /**
        Returns a count of the number of matrix rows.
    */
    public int rows()
    {
        return values.length;
    }

    /**
        Returns a count of the number of matrix columns.
    */
    public int columns()
    {
        return values[0].length;
    }

    /**
        Returns the matrix value from the specified row and column of the matrix.
    */
    public double get(int row, int column)
    {
        return values[row][column];
    }

    public static Matrix add(final Matrix a, final Matrix b)
    {
        if (a.rows() != b.rows() || a.columns() != b.columns()) {
            throw new IllegalArgumentException("Matrices cannot be multiplied");
        }

        double[][] result = allocateArray(a.rows(), a.columns());
        for (int row = 0; row < a.rows(); row++) {
            for (int column = 0; column < a.columns(); column++) {
                result[row][column] = a.get(row, column) + b.get(row, column);
            }
        }
        return new Matrix(result);
    }

    public static Matrix multiply(final Matrix a, final Matrix b)
    {
        if (a.columns() != b.rows()) {
            throw new IllegalArgumentException("Matrices cannot be multiplied");
        }

        double[][] result = allocateArray(a.rows(), b.columns());
        for (int row = 0; row < a.rows(); row++) {
            for (int column = 0; column < b.columns(); column++) {
                double value = 0.0;
                for (int i = 0; i < a.columns(); i++) {
                    value += a.get(row, i) * b.get(i, column);
                }
                result[row][column] = value;
            }
        }
        return new Matrix(result);
    }

    /**
        Returns a^7 (or a*a*a*a*a*a*a).
    */
    public static Matrix power7(final Matrix a)
    {
        if (a.rows() != a.columns()) {
            throw new IllegalArgumentException("Matrix is not square");
        }

        Matrix a2 = multiply(a, a);
        Matrix a3 = multiply(a, a2);
        Matrix a4 = multiply(a2, a2);
        Matrix a7 = multiply(a3, a4);
        return a7;
    }

    /**
        Asserts that no values in the matrix are NaN, then returns the input matrix.
    */
    public static Matrix assertNotNaN(final Matrix a)
    {
        for (int row = 0; row < a.rows(); row++) {
            for (int column = 0; column < a.columns(); column++) {
                Utilities.assertNotNaN(a.get(row, column));
            }
        }
        return a;
    }

    public static Matrix extendWithIdentity(final Matrix a, int rows, int columns)
    {
        if (a.rows() == rows && a.columns() == columns) {
            return a;
        } else if (rows < a.rows() || columns < a.columns()) {
            throw new IllegalArgumentException("Cannot shrink matrix");
        } else {
            double[][] result = allocateArray(rows, columns);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    if (row < a.rows() && col < a.columns()) {
                        result[row][col] = a.get(row, col);
                    } else {
                        result[row][col] = (row == col) ? 1.0 : 0.0;
                    }
                }
            }
            return new Matrix(result);
        }
    }

    public static Matrix invert4x4(final Matrix a) {
        if (a.rows() != 4 || a.columns() != 4) {
            throw new IllegalArgumentException("Only 4x4 matrices supported.");
        }

        /*
            Adapted from:
            ftp://download.intel.com/design/PentiumIII/sml/24504301.pdf
        */

        // temp array for pairs
        double tmp[] = new double[12];
        // array of transpose source matrix
        double src[] = new double[16];
        // determinant
        double det;
        // destination
        double dst[] = new double[16];

        // transpose matrix
        for (int i = 0; i < 4; i++) {
            src[i] = a.get(i, 0);
            src[i + 4] = a.get(i, 1);
            src[i + 8] = a.get(i, 2);
            src[i + 12] = a.get(i, 3);
        }

        // calculare pairs for first 8 elements (cofactors)
        tmp[0] = src[10] * src[15]; 
        tmp[1] = src[11] * src[14];
        tmp[2] = src[9] * src[15];
        tmp[3] = src[11] * src[13];
        tmp[4] = src[9] * src[14];
        tmp[5] = src[10] * src[13];
        tmp[6] = src[8] * src[15];
        tmp[7] = src[11] * src[12];
        tmp[8] = src[8] * src[14];
        tmp[9] = src[10] * src[12];
        tmp[10] = src[8] * src[13];
        tmp[11] = src[9] * src[12];

        // calculate first 8 elements (cofactors)
        dst[0] = tmp[0]*src[5] + tmp[3]*src[6] + tmp[4]*src[7];
        dst[0] -= tmp[1]*src[5] + tmp[2]*src[6] + tmp[5]*src[7];
        dst[1] = tmp[1]*src[4] + tmp[6]*src[6] + tmp[9]*src[7];
        dst[1] -= tmp[0]*src[4] + tmp[7]*src[6] + tmp[8]*src[7];
        dst[2] = tmp[2]*src[4] + tmp[7]*src[5] + tmp[10]*src[7];
        dst[2] -= tmp[3]*src[4] + tmp[6]*src[5] + tmp[11]*src[7];
        dst[3] = tmp[5]*src[4] + tmp[8]*src[5] + tmp[11]*src[6];
        dst[3] -= tmp[4]*src[4] + tmp[9]*src[5] + tmp[10]*src[6];
        dst[4] = tmp[1]*src[1] + tmp[2]*src[2] + tmp[5]*src[3];
        dst[4] -= tmp[0]*src[1] + tmp[3]*src[2] + tmp[4]*src[3];
        dst[5] = tmp[0]*src[0] + tmp[7]*src[2] + tmp[8]*src[3];
        dst[5] -= tmp[1]*src[0] + tmp[6]*src[2] + tmp[9]*src[3];
        dst[6] = tmp[3]*src[0] + tmp[6]*src[1] + tmp[11]*src[3];
        dst[6] -= tmp[2]*src[0] + tmp[7]*src[1] + tmp[10]*src[3];
        dst[7] = tmp[4]*src[0] + tmp[9]*src[1] + tmp[10]*src[2];
        dst[7] -= tmp[5]*src[0] + tmp[8]*src[1] + tmp[11]*src[2];

        // calculate pairs for second 8 elements (cofactors)
        tmp[0] = src[2]*src[7];
        tmp[1] = src[3]*src[6];
        tmp[2] = src[1]*src[7];
        tmp[3] = src[3]*src[5];
        tmp[4] = src[1]*src[6];
        tmp[5] = src[2]*src[5];
        tmp[6] = src[0]*src[7];
        tmp[7] = src[3]*src[4]; 
        tmp[8] = src[0]*src[6];
        tmp[9] = src[2]*src[4];
        tmp[10] = src[0]*src[5];
        tmp[11] = src[1]*src[4];
        
        // calculate second 8 elements (cofactors)
        dst[8] = tmp[0]*src[13] + tmp[3]*src[14] + tmp[4]*src[15];
        dst[8] -= tmp[1]*src[13] + tmp[2]*src[14] + tmp[5]*src[15];
        dst[9] = tmp[1]*src[12] + tmp[6]*src[14] + tmp[9]*src[15]; 
        dst[9] -= tmp[0]*src[12] + tmp[7]*src[14] + tmp[8]*src[15]; 
        dst[10] = tmp[2]*src[12] + tmp[7]*src[13] + tmp[10]*src[15]; 
        dst[10]-= tmp[3]*src[12] + tmp[6]*src[13] + tmp[11]*src[15]; 
        dst[11] = tmp[5]*src[12] + tmp[8]*src[13] + tmp[11]*src[14]; 
        dst[11]-= tmp[4]*src[12] + tmp[9]*src[13] + tmp[10]*src[14];
        dst[12] = tmp[2]*src[10] + tmp[5]*src[11] + tmp[1]*src[9];
        dst[12]-= tmp[4]*src[11] + tmp[0]*src[9] + tmp[3]*src[10];
        dst[13] = tmp[8]*src[11] + tmp[0]*src[8] + tmp[7]*src[10]; 
        dst[13]-= tmp[6]*src[10] + tmp[9]*src[11] + tmp[1]*src[8];
        dst[14] = tmp[6]*src[9] + tmp[11]*src[11] + tmp[3]*src[8]; 
        dst[14]-= tmp[10]*src[11] + tmp[2]*src[8] + tmp[7]*src[9]; 
        dst[15] = tmp[10]*src[10] + tmp[4]*src[8] + tmp[9]*src[9];
        dst[15]-= tmp[8]*src[9] + tmp[11]*src[10] + tmp[5]*src[8];        
        
        // calculate determinant
        det=src[0]*dst[0]+src[1]*dst[1]+src[2]*dst[2]+src[3]*dst[3];
        
        // calculate matrix inverse
        return Matrix.create4x4(
                dst[0] / det, dst[1] / det, dst[2] / det, dst[3] / det,
                dst[4] / det, dst[5] / det, dst[6] / det, dst[7] / det,
                dst[8] / det, dst[9] / det, dst[10] / det, dst[11] / det,
                dst[12] / det, dst[13] / det, dst[14] / det, dst[15] / det);
    }

    /**
        Only operates on 4x1 column matrices/vectors.

        Assumes homogenous coordinate representation, and converts
        to standard Triplex coordinate.
    */
    public Triplex toTriplex()
    {
        if (rows() == 4 && columns() == 1) {
            return new Triplex(
                    get(0, 0) / get(3, 0),
                    get(1, 0) / get(3, 0),
                    get(2, 0) / get(3, 0));
        } else {
            throw new IllegalArgumentException("Only 4x1 column vectors supported");
        }
    }

    private static double[][] allocateArray(int rows, int columns)
    {
        double[][] result = new double[rows][];
        for (int i = 0; i < rows; i++) {
            result[i] = new double[columns];
        }
        return result;
    }

    @Override
    public int compareTo(Matrix m) {
        int result = 0;
        if (this != m) {
            result = this.rows() - m.rows();
            if (result == 0) {
                result = this.columns() - m.columns();

                for (int row = 0; (result == 0) && (row < rows()); row++) {
                    for (int column = 0; (result == 0) && (column < columns()); column++) {
                        result = Double.compare(this.get(row, column), m.get(row, column));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Matrix) && equals((Matrix)o);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int row = 0; row < rows(); row++) {
            result = result ^ (Arrays.hashCode(values[row]) * row);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        for (int row = 0; row < rows(); row++) {
            result.append("[");
            for (int column = 0; column < columns(); column++) {
                result.append(" ");
                result.append(get(row, column));
            }
            result.append(" ]\n");
        }
        return result.toString();
    }

    public boolean equals(Matrix m) {
        return this.compareTo(m) == 0;
    }

    private final double[][] values;
}
