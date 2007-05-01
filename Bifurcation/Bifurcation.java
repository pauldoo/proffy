import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.zip.*;

public final class Bifurcation
{
    final int width;
    final int height;
    final int supersample;
    final int iterations;
    final double exposure;
    final double minR;
    final double maxR;
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
        final double exposure = Double.parseDouble(args[argumentIndex++]);
        final double minR = Double.parseDouble(args[argumentIndex++]);
        final double maxR = Double.parseDouble(args[argumentIndex++]);

        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(System.out));
        zos.putNextEntry(new ZipEntry("bifurcation.bmp"));
        new Bifurcation(width, height, supersample, iterations, exposure, minR, maxR).go(zos);
        zos.closeEntry();
        zos.close();
    }

    private Bifurcation(final int width, final int height, final int supersample, final int iterations, final double exposure, final double minR, final double maxR)
    {
        this.width = width;
        this.height = height;
        this.supersample = supersample;
        this.iterations = iterations;
        this.exposure = exposure;
        this.minR = minR;
        this.maxR = maxR;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    }

    private void go(OutputStream out) throws IOException
    {
        System.err.println("Width: " + width);
        System.err.println("Height: " + height);
        System.err.println("Supersample: " + supersample);
        System.err.println("Iterations: " + iterations);
        System.err.println("Exposure: " + exposure);
        System.err.println("Min R: " + minR);
        System.err.println("Max R: " + maxR);

        for (int x = 0; x < width; ++x) {
            final double[] averaging_buckets = new double[height];
            for (int xs = 0; xs < supersample; ++xs) {
                final int[] buckets = new int[height * supersample];
                {
                    final double r = minR + (x * supersample + xs) * (maxR - minR) / (width * supersample);
                    double y = 0.5;
                    for (int i = -1000; i < iterations; ++i) {
                        y = r * y * (1.0 - y);
                        if (i >= 0) {
                            buckets[(int)(y * buckets.length)]++;
                        }
                    }
                }
                for (int y = 0; y < buckets.length; ++y) {
                    final double v = 1.0 - Math.exp(-buckets[y] * exposure);
                    averaging_buckets[y / supersample] += v;
                }
            }

            for (int y = 0; y < averaging_buckets.length; ++y) {
                final double v = averaging_buckets[y] / (supersample * supersample);
                final int r = (int)(brown[0] + (green[0] - brown[0]) * v);
                final int g = (int)(brown[1] + (green[1] - brown[1]) * v);
                final int b = (int)(brown[2] + (green[2] - brown[2]) * v);
                image.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }

        ImageIO.write(image, "bmp", out);
    }
}
