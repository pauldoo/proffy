package threadedgameoflife;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public final class Main
{
    private final class CellRunner implements Runnable
    {
        public void run() {
            try {
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    private static Image createRunningBoard(int width, int height)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        return image;
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
    }
}
