import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final int port;
    SrvProcces srvProcces = new SrvProcces();
    private final ExecutorService pool = Executors.newCachedThreadPool();
    public static Map<Socket,String> stringSocketMap = new HashMap<>();

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
                stringSocketMap.put(clientSocket,name());
                pool.submit(() ->  srvProcces.handle(clientSocket));
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят.%n", port);
            e.printStackTrace();
        }
    }

    private String name() {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("John");
        strings.add("Mike");
        strings.add("Alex");
        String names = null;
        int rnd = new Random().nextInt(0, 3);
        switch (rnd) {
            case 0:
                names = strings.get(0);
                break;
            case 1:
                names = strings.get(1);
                break;
            case 2:
                names = strings.get(2);
                break;
        }
        return names;
    }

}
