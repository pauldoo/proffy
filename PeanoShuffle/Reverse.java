import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;


final class Reverse
{
    public static void main(String[] args) throws IOException
    {
        final File inputFile = new File(args[0]);
        final File outputFile = new File(args[1]);

        BufferedImage image = ImageIO.read(inputFile);
        image = Peano.transform(image, false);
        ImageIO.write(image, "bmp", outputFile);
    }
}

