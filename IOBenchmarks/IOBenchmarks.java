import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;

final class Settings {
    public static final int PORT = 6520;
    public static final int BLOCK_SIZE = 1024 * 1024;
}



public final class IOBenchmarks {

    public static void main(String[] args) throws Exception {
        switch (args.length) {
            case 0:
                runServer();
                break;
            case 2:
                final int blocks = Integer.parseInt(args[0]);
                final InetAddress clientAddress = InetAddress.getByName(args[1]);
                runClient(blocks, clientAddress);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static String calculatePerformance(final int blocks, final long startTime, final long endTime) {
        final double megabytes = (blocks * Settings.BLOCK_SIZE) / (1.0 * 1024 * 1024);
        final double seconds = (endTime - startTime) / 1000.0;
        return megabytes + "MiB in " + seconds + "s, " + (megabytes / seconds) + "MiB/s";
    }

    private static void writeBlocksFromMemory(final WritableByteChannel output, final int count) throws IOException
    {
        if (output == null) {
            throw new IllegalArgumentException("Channel is null");
        }
        final ByteBuffer buffer = ByteBuffer.allocateDirect(Settings.BLOCK_SIZE);
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; ++i) {
            System.err.print(">");
            final long bytesTransferred = output.write(buffer);
            if (bytesTransferred != Settings.BLOCK_SIZE) {
                throw new IOException("Failed to write entire block (" + bytesTransferred + ")");
            }
        }
        final long endTime = System.currentTimeMillis();
        System.out.println(calculatePerformance(count, startTime, endTime));
    }

    private static void readBlocksToMemory(final ReadableByteChannel input, final int count) throws IOException
    {
        if (input == null) {
            throw new IllegalArgumentException("Channel is null");
        }
        final ByteBuffer buffer = ByteBuffer.allocateDirect(Settings.BLOCK_SIZE);
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; ++i) {
            System.err.print("<");
            final long bytesTransferred = input.read(buffer);
            if (bytesTransferred != Settings.BLOCK_SIZE) {
                throw new IOException("Failed to read entire block (" + bytesTransferred + ")");
            }
        }
        final long endTime = System.currentTimeMillis();
        System.out.println(calculatePerformance(count, startTime, endTime));
    }

    private static void runClient(final int blocks, final InetAddress clientAddress) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(true);
        Socket socket = channel.socket();
        socket.connect(new InetSocketAddress(clientAddress, Settings.PORT));

        configureSocket(socket);

        System.out.print("Writing over TCP from Memory: ");
        writeBlocksFromMemory(socket.getChannel(), blocks);
        System.out.print("Reading over TCP to Memory: ");
        readBlocksToMemory(socket.getChannel(), blocks);
    }

    private static void runServer() throws Exception {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(true);
        ServerSocket server = channel.socket();
        server.bind(new InetSocketAddress(Settings.PORT));
        server.setPerformancePreferences(0, 0, 1);
        while (true) {
            Socket socket = server.accept();

            configureSocket(socket);

            new Thread(new ReadRunner(socket.getChannel())).start();
            new Thread(new WriteRunner(socket.getChannel())).start();
            break;
        }
    }

    private static void configureSocket(Socket socket) throws IOException {
        socket.getChannel().configureBlocking(true);
        socket.setPerformancePreferences(0, 0, 1);
        if (socket.getChannel().isBlocking() == false) {
            throw new IOException("Failed to enable blocking mode.");
        }
    }

    private static final class WriteRunner implements Runnable {
        final WritableByteChannel output;

        public WriteRunner(WritableByteChannel output) {
            this.output = output;
        }

        public void run() {
            try {
                while (true) {
                    writeBlocksFromMemory(output, 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    final static class ReadRunner implements Runnable {
        final ReadableByteChannel input;

        public ReadRunner(ReadableByteChannel input) {
            this.input = input;
        }

        public void run() {
            try {
                while (true) {
                    readBlocksToMemory(input, 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
