package threadedgameoflife;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public final class Main
{
    private static final class CellRunner implements Runnable
    {

        public CellRunner(boolean state, int x, int y, BufferedImage image) {
            this.state = state;
            this.x = x;
            this.y = y;
            this.image = image;
        }

        public void run() {
            try {
                if (state == true) {
                   image.setRGB(x, y, 0xffffff);
                } else {
                    image.setRGB(x, y, 0x000000);
                }

                if (inputs.size() != 8 || outputs.size() != 8) {
                    throw new IllegalArgumentException();
                }

                while (true) {
                    int aliveCount = 0;
                    for (DataInput in: inputs) {
                        if (in.readBoolean() == true) {
                            aliveCount++;
                        }
                    }

                    if (state == true) {
                        switch (aliveCount) {
                            case 0:
                            case 1:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                state = false;
                                break;

                            case 2:
                            case 3:
                                state = true;
                                break;

                            default:
                                throw new IllegalStateException();
                        }
                    } else {
                        switch (aliveCount) {
                            case 3:
                                state = true;
                                break;

                            case 0:
                            case 1:
                            case 2:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                state = false;
                                break;

                            default:
                                throw new IllegalStateException();
                        }
                    }

                    for (DataOutput out: outputs) {
                        out.writeBoolean(state);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        public void setNeighbours(CellRunner[] neighbours) throws IOException {
            if (neighbours.length != 8) {
                throw new IllegalArgumentException();
            }

            for (CellRunner other: neighbours) {
                PipedInputStream in = new PipedInputStream();
                PipedOutputStream out = new PipedOutputStream(in);
                this.inputs.add(new DataInputStream(in));
                other.outputs.add(new DataOutputStream(out));
            }
        }

        private boolean state;
        final private int x;
        final private int y;
        final private BufferedImage image;
        final private ArrayList<DataInput> inputs = new ArrayList<DataInput>();
        final private ArrayList<DataOutput> outputs = new ArrayList<DataOutput>();
    }
    
    private static Image createRunningBoard(
            int width,
            int height) throws IOException
    {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        CellRunner[][] cells = new CellRunner[height][];
        
        for (int y = 0; y < height; y++) {
            cells[y] = new CellRunner[width];
        }

        final Random rng = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new CellRunner(rng.nextBoolean(), x, y, image);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final CellRunner[] neighbours = new CellRunner[8];
                neighbours[0] = cells[(y - 1 + height) % height][(x - 1 + width) % width];
                neighbours[1] = cells[(y - 1 + height) % height][x];
                neighbours[2] = cells[(y - 1 + height) % height][(x + 1) % width];
                neighbours[3] = cells[y][(x - 1 + width) % width];
                neighbours[4] = cells[y][(x + 1) % width];
                neighbours[5] = cells[(y + 1) % height][(x - 1 + width) % width];
                neighbours[6] = cells[(y + 1) % height][x];
                neighbours[7] = cells[(y + 1) % height][(x + 1) % width];
                cells[y][x].setNeighbours(neighbours);
            }
        }

        ThreadGroup group = new ThreadGroup("Cells");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Thread t = new Thread(group, cells[y][x], x + "," + y, 10 * 1024);
                t.setPriority(Thread.MIN_PRIORITY);
                t.start();
                System.out.print(".");
            }
            System.out.println();
        }

        return image;
    }
    
    public static void main(String[] args) throws Exception
    {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image image = createRunningBoard(40, 30);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000 / 50);
                        frame.getContentPane().repaint();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }).start();
    }
}
