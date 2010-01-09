/*
    Copyright (C) 2009  Paul Richards.

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

package fractals;

import java.util.Arrays;

/**
    Immutable matrix class (over reals).
*/
final class Matrix implements Comparable<Matrix>
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

    static Matrix createIdentity(int size)
    {
        double[][] values = allocateArray(size, size);
        for (int i = 0; i < size; i++) {
            values[i][i] = 1.0;
        }
        return new Matrix(values);
    }

    /**
        Static constructor for matrices of 3 rows and 5 columns.
    */
    static Matrix create3x5(
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
    static Matrix create5x5(
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
    static Matrix create5x3(
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
    int rows()
    {
        return values.length;
    }

    /**
        Returns a count of the number of matrix columns.
    */
    int columns()
    {
        return values[0].length;
    }

    /**
        Returns the matrix value from the specified row and column of the matrix.
    */
    double get(int row, int column)
    {
        return values[row][column];
    }

    static Matrix multiply(final Matrix a, final Matrix b)
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

    public boolean equals(Matrix m) {
        return this.compareTo(m) == 0;
    }

    private final double[][] values;
}
