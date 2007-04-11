import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;


final class Forward
{
    public static void main(String[] args) throws IOException
    {
        final File inputFile = new File(args[0]);
        final File normalOutputFile = new File(args[1]);
        final File reorderedOutputFile = new File(args[2]);

        BufferedImage image = ImageIO.read(inputFile);
        ImageIO.write(image, "bmp", normalOutputFile);
        image = Peano.transform(image, true);
        ImageIO.write(image, "bmp", reorderedOutputFile);

        final long inSize = inputFile.length();
        final long normalOutSize = normalOutputFile.length();
        final long reorderedOutSize = reorderedOutputFile.length();
    }
}

