import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EchoServer {

    private final int port;
    SrvProcces srvProcces = new SrvProcces();
    private final int pools = 3;
    private final ExecutorService pool = Executors.newFixedThreadPool(pools);

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()){
                Socket clientSocket = server.accept();
                pool.submit(() ->  srvProcces.handle(clientSocket));
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят.%n", port);
            e.printStackTrace();
        }
    }
}
