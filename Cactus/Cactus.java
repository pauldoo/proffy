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
        zos.putNextEntry(new ZipEntry("ship.bmp"));
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

    private void go(OutputStream out) throws IOException
    {
        System.err.println("Width: " + width);
        System.err.println("Height: " + height);
        System.err.println("Supersample: " + supersample);
        System.err.println("Iterations: " + iterations);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int memberCount = 0;
                for (int xs = 0; xs < supersample; ++xs) {
                    for (int ys = 0; ys < supersample; ++ys) {
                        final double cx = (x * supersample + xs) * 3.0 / (width * supersample) - 1.0;
                        final double cy = (y * supersample + ys) * 3.0 / (height * supersample) - 1.0;
                        double xn = 0;
                        double yn = 0;
                        int count = 0;
                        while (count < iterations && (xn*xn + yn*yn) < 100) {
                            final double xt = xn*xn - yn*yn - cx;
                            final double yt = 2 * Math.abs(xn * yn) - cy;
                            xn = xt;
                            yn = yt;
                            count++;
                        }
                        if (count >= iterations) {
                            memberCount++;
                        }
                    }
                }
                final double v = memberCount * 1.0 / (supersample * supersample);
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
