import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public final class Player
{
    final java.util.List<Image> images = new ArrayList<Image>();

    private Player(InputStream in) throws Exception
    {
        ZipInputStream zis = new ZipInputStream(in);
        ZipEntry entry;
        while( (entry = zis.getNextEntry()) != null ) {
            System.out.println(entry);
            BufferedImage image = ImageIO.read(zis);
            images.add(image);
        }
    }

    private void run(final GraphicsDevice device) throws Exception
    {
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        Frame frame = new Frame(gc);
        try {
            frame.setUndecorated(true);
            frame.setIgnoreRepaint(true);
            device.setFullScreenWindow(frame);
            Rectangle bounds = frame.getBounds();
            frame.createBufferStrategy(2);
            BufferStrategy bufferStrategy = frame.getBufferStrategy();

            Image lastImage = null;
            while (true) {
                final Point mouse = frame.getMousePosition(true);
                if (mouse != null) {
                    final Image image = images.get(images.size() * (mouse.x - bounds.x) / bounds.width);
                    if (image != lastImage) {
                        Graphics g = bufferStrategy.getDrawGraphics();
                        if (!bufferStrategy.contentsLost()) {
                            g.drawImage(
                                image,
                                (bounds.width - image.getWidth(null)) / 2,
                                (bounds.height - image.getHeight(null)) / 2,
                                null);

                            bufferStrategy.show();
                            g.dispose();
                        }
                    }
                    lastImage = image;
                }
                Thread.yield();
            }
        } finally {
            device.setFullScreenWindow(null);
        }
    }

    public static final void main(String[] args) throws Exception
    {
        InputStream in = new BufferedInputStream(new FileInputStream(args[0]));
        Player player = new Player(in);
        in.close();

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        player.run(device);
    }

}

