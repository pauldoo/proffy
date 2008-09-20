import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.*;
import java.io.*;

final class Circle
{
    public final double x;
    public final double y;
    public final double d;

    public Circle(double x, double y, double d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    public String toString()
    {
        return "[" + x + ", " + y + ", " + d + "]";
    }
}

final class Constants
{
    public static final double GOLDEN_RATIO = (1.0 + Math.sqrt(5.0)) / 2.0;
    public static final double GOLDEN_ANGLE = 2 * Math.PI / (GOLDEN_RATIO * GOLDEN_RATIO);
}

public final class SpottySpiral
{

    public static void main(String[] args) throws IOException
    {
        final Collection<Circle> spots = new ArrayList<Circle>();
        final BufferedImage image = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        for (int i = 0; true; i++) {
            final double angle = Constants.GOLDEN_ANGLE * i;
            //final double angle = (Constants.GOLDEN_ANGLE - 0.001) * i;
            //final double angle = Math.E * i;

            double radius = 0.0;
            for (Circle c: spots) {
                final double dx = Math.cos(angle);
                final double dy = Math.sin(angle);
                final double nx = dy;
                final double ny = -dx;
                final double distanceToSpotCenter = nx * c.x + ny * c.y;

                final double correction = Math.sqrt(c.d * c.d - distanceToSpotCenter * distanceToSpotCenter);
                if (correction > 0.0) {
                    final double radiusToClosestPointToSpot = dx * c.x + dy * c.y;
                    final double newRadius = radiusToClosestPointToSpot + correction;

                    if (newRadius > radius) {
                        radius = newRadius;
                    }
                }
            }

            Circle c = new Circle(Math.cos(angle) * radius, Math.sin(angle) * radius, 10.0);
            spots.add(c);
            Shape circle = new Ellipse2D.Double(c.x - c.d/2.0 + 1000.0, c.y - c.d/2.0 + 1000.0, c.d, c.d);
            graphics.fill(circle);
            //System.out.println(c);
            if (radius > (Math.sqrt(2) * 1000)) {
                break;
            }
        }
        graphics.dispose();
        ImageIO.write(image, "png", new File("image.png"));
    }
}
