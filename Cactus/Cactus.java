import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.zip.*;

public final class Cactus
{
    final int width;
    final int height;
    final int supersample;
    final int iterations;
    final BufferedImage image;

    final int[] brown = new int[]{0x1f, 0x14, 0x01};
    final int[] green = new int[]{0x7c, 0xaf, 0x2d};

    public static final void main(String[] args) throws Exception
    {
        int argumentIndex = 0;
        final int width = Integer.parseInt(args[argumentIndex++]);
        final int height = Integer.parseInt(args[argumentIndex++]);
        final int supersample = Integer.parseInt(args[argumentIndex++]);
        final int iterations = Integer.parseInt(args[argumentIndex++]);

        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(System.out));
        zos.putNextEntry(new ZipEntry("cactus.bmp"));
        new Cactus(width, height, supersample, iterations).go(zos);
        zos.closeEntry();
        zos.close();
    }

    private Cactus(final int width, final int height, final int supersample, final int iterations)
    {
        this.width = width;
        this.height = height;
        this.supersample = supersample;
        this.iterations = iterations;

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    }

    private static final class Imag
    {
        public final double real;
        public final double imag;

        public static final Imag ONE = new Imag(1.0);

        public Imag(final double r)
        {
            this(r, 0);
        }

        public Imag(final double r, final double i)
        {
            this.real = r;
            this.imag = i;
        }

        public Imag power(final int p)
        {
            Imag result = new Imag(1.0);
            for (int i = 1; i <= p; ++i) {
                result = multiply(result, this);
            }
            return result;
        }

        public Imag negate()
        {
            return new Imag(-real, -imag);
        }

        public Imag reciprocal()
        {
            final double mag2 = magnitude2();
            return new Imag(real / mag2, -imag / mag2);
        }

        public double magnitude2()
        {
            return real * real + imag * imag;
        }

        public double magnitude()
        {
            return Math.sqrt(magnitude2());
        }

        public static Imag multiply(final Imag lhs, final Imag rhs)
        {
            return new Imag(
                lhs.real * rhs.real - lhs.imag * rhs.imag,
                lhs.real * rhs.imag + lhs.imag * rhs.real);
        }

        public static Imag divide(final Imag lhs, final Imag rhs)
        {
            return multiply(lhs, rhs.reciprocal());
        }

        public static Imag add(final Imag lhs, final Imag rhs)
        {
            return new Imag(
                lhs.real + rhs.real,
                lhs.imag + rhs.imag);
        }

        public static Imag subtract(final Imag lhs, final Imag rhs)
        {
            return add(lhs, rhs.negate());
        }
    }

    private static final double f(final double r)
    {
        return (r * (1 + 2*r + r*r) * (r*r - 1)) / ((1 + r*r*r) * (1 + r*r*r));
    }

    private static final double g(final double r)
    {
        return (r * (1 - 2*r + r*r) * (r*r - 1)) / ((1 + r*r*r) * (1 + r*r*r));
    }

    private void go(OutputStream out) throws IOException
    {
        System.err.println("Width: " + width);
        System.err.println("Height: " + height);
        System.err.println("Supersample: " + supersample);
        System.err.println("Iterations: " + iterations);

        final double xmin = -2;
        final double xmax = 2;
        final double ymin = -2;
        final double ymax = 2;
        final int m = 4;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int memberCount = 0;
                for (int xs = 0; xs < supersample; ++xs) {
                    for (int ys = 0; ys < supersample; ++ys) {
                        final Imag z0 = new Imag(
                            (x * supersample + xs) * (xmax - xmin) / (width * supersample) + xmin,
                            (y * supersample + ys) * (ymax - ymin) / (height * supersample) + ymin).reciprocal();
                        int count = 0;
                        Imag z = z0;
                        while (count < iterations && z.magnitude2() < 100) {
                            //z = Imag.subtract(Imag.add(z.power(3), Imag.multiply(Imag.subtract(z0, Imag.ONE), z)), z0);
                            //z = Imag.divide(Imag.add(z.power(2), z), Imag.add(Imag.multiply(new Imag(2), z.power(m)), z0));
                            //z = Imag.add(z.power(2), z0);
                            /*
                            z = Imag.divide(
                                Imag.add(Imag.add(z.power(m), z.power(2)), Imag.ONE),
                                Imag.add(Imag.add(Imag.multiply(new Imag(2), z.power(m-1)), z0.negate()), Imag.ONE)
                            );
                            */
                            z = Imag.add(
                                z.power(2),
                                new Imag(
                                    f(z.magnitude()),
                                    g(z.magnitude())
                                )
                            );
                            count++;
                        }
                        if (count == iterations || (count % 2) == 0) {
                            memberCount++;
                        }
                    }
                }
                final double v = 1.0 - (memberCount * 1.0 / (supersample * supersample));
                final int r = (int)(brown[0] + (green[0] - brown[0]) * v);
                final int g = (int)(brown[1] + (green[1] - brown[1]) * v);
                final int b = (int)(brown[2] + (green[2] - brown[2]) * v);
                image.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
            System.err.print("\r" + x);
        }
        System.err.println();

        ImageIO.write(image, "bmp", out);
    }
}
