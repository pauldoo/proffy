import java.awt.image.*;
import java.util.*;

public final class Peano
{
    private int x = 0;
    private int y = 0;

    private final List<Coordinate> points = new ArrayList<Coordinate>();

    private static final class Coordinate {
        public final int x;
        public final int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void nextPoint(int dx, int dy)
    {
        x += dx;
        y += dy;
        points.add(new Coordinate(x, y));
    }

    private void HilbertA (int level) {
        if (level > 0) {
            HilbertB(level-1);
            nextPoint(0, 1);
            HilbertA(level-1);
            nextPoint(1, 0);
            HilbertA(level-1);
            nextPoint(0, -1);
            HilbertC(level-1);
        }
    }

    private void HilbertB (int level) {
        if (level > 0) {
            HilbertA(level-1);
            nextPoint(1, 0);
            HilbertB(level-1);
            nextPoint(0, 1);
            HilbertB(level-1);
            nextPoint(-1, 0);
            HilbertD(level-1);
        }
    }

    private void HilbertC (int level) {
        if (level > 0) {
            HilbertD(level-1);
            nextPoint(-1, 0);
            HilbertC(level-1);
            nextPoint(0, -1);
            HilbertC(level-1);
            nextPoint(1, 0);
            HilbertA(level-1);
        }
    }

    private void HilbertD (int level) {
        if (level > 0) {
            HilbertC(level-1);
            nextPoint(0, -1);
            HilbertD(level-1);
            nextPoint(-1, 0);
            HilbertD(level-1);
            nextPoint(0, 1);
            HilbertB(level-1);
        }
    }

    private void generate(int width) {
        int levels = 0;
        while ((width /= 2) > 0) {
            levels++;
        }
        nextPoint(0, 0);
        HilbertA(levels);
    }

    public Peano(int width) {
        generate(width);
    }

    public static BufferedImage transform(BufferedImage image, boolean forwards)
    {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Peano p = new Peano(image.getWidth());
        int index = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Coordinate coord = p.points.get(index);
                index++;
                if (forwards) {
                    int color = image.getRGB(coord.x, coord.y);
                    result.setRGB(x, image.getHeight() - y - 1, color);
                } else {
                    int color = image.getRGB(x, image.getHeight() - y - 1);
                    result.setRGB(coord.x, coord.y, color);
                }
            }
        }
        return result;
    }
}
