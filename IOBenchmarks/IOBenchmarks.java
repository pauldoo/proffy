import java.net.*;

final class Settings {
    public static final int PORT = 6520;
}

final class ReadRunner implements Runnable {
}

final class WriteRunner implements Runnable {
}

public final class IOBenchmarks {
    public static void main(String[] args) {
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

    private static runServer() {
        ServerSocket server = new ServerSocket(Settings.PORT);
        Socket socket = server.accept();
        server.close();
        server = null;
        configureSocket(socket);
        new Thread(new ReadRunner(socket.getInputStream())).start();
        new Thread(new WriteRunner(socket.getInputStream())).start();
    }

    private static configureSocket(Socket socket) {
        socket.setPerformancePreferences(0, 0, 1);
    }
}
