import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public final class Attractors
{
    private static final int STEPS = 250;
    private static final double DISTANCE = 15;
    private static final double XSCALE = 1;
    private static final double YSCALE = 1;
    private static final double ZSCALE = 1;

    private final int FRAME;
    private final int WIDTH;
    private final int HEIGHT;
    private final int LAYERS;
    private final int ITERATIONS;
    private final double EXPOSURE;

    private final int image[];

    private Attractors(
        final int frame,
        final int width,
        final int height,
        final int layers,
        final int iterations,
        final double exposure
    )
    {
        this.FRAME = frame;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.LAYERS = layers;
        this.ITERATIONS = iterations;
        this.EXPOSURE = exposure;
        this.image = new int[WIDTH * HEIGHT];
    }

    private final void plot(final int x, final int y)
    {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            image[y * WIDTH + x]++;
        }
    }

    private final void plot(double x, double y, double z)
    {
        x *= XSCALE;
        y *= YSCALE;
        z *= ZSCALE;
        z += DISTANCE;
        if (z > 0) {
            final int xi = (int)((x / z) * HEIGHT + (WIDTH / 2));
            final int yi = (int)((y / z) * HEIGHT + (HEIGHT / 2));
            plot(xi, yi);
        }
    }

    public static void main(String[] args) throws IOException
    {
        final int frame = Integer.parseInt(args[0]);
        final int width = Integer.parseInt(args[1]);
        final int height = Integer.parseInt(args[2]);
        final int layers = Integer.parseInt(args[3]);
        final int iterations = Integer.parseInt(args[4]);
        final double exposure = Double.parseDouble(args[5]);

        new Attractors(frame, width, height, layers, iterations, exposure).go(System.out);
    }


    private void go(OutputStream out) throws IOException
    {
        System.err.println("Frame: " + FRAME);
        System.err.println("Width: " + WIDTH);
        System.err.println("Height: " + HEIGHT);
        System.err.println("Layers: " + LAYERS);
        System.err.println("Iterations: " + ITERATIONS);
        System.err.println("Exposure: " + EXPOSURE);

        final double angle = (Math.PI * 2 * FRAME) / STEPS;
        final double ca = Math.cos(angle);
        final double sa = Math.sin(angle);

        final double B = 1;
        final double C = 2;

        for (int layer = 0; layer < LAYERS; ++layer) {
            final double A = (layer * 5.0) / LAYERS;

            double x = 2;
            double y = 3;
            final double z = (A / 5.0) * 20 - 10;

            for (int i = 0; i < ITERATIONS; ++i) {
                {
                    final double t = x;
                    x = y - Math.signum(x) * Math.sqrt(Math.abs(x * B + C));
                    y = A - t;
                }
                plot(
                    ca * z + sa * x,
                    y,
                    -sa * z + ca * x);
            }
        }

        BufferedImage output = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                final double v = 1.0 - Math.exp(-image[y * WIDTH + x] * EXPOSURE);
                final int b = (int)(v * 255);
                output.setRGB(x, y, (b << 16) | (b << 8) | b);
            }
        }
        ImageIO.write(output, "png", System.out);
    }
}

