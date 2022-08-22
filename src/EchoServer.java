import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final int port;

    private final ExecutorService pool = Executors.newCachedThreadPool();

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
                pool.submit(() ->  handle(clientSocket));
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят.%n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) {
        System.out.printf("Подключен клиент: %s%n",socket);


        try (Scanner reader = getReader(socket);
            PrintWriter writer = getWriter(socket)) {
            while (true) {
                String message = reader.nextLine().strip();
                System.out.printf("Клиент: %s отправил смс: %s%n",socket, message);

                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }
                sendResponse(writer);
            }
        } catch (NoSuchElementException e) {
            System.out.printf("Клиент %s закрыл соединение!",socket);
        }catch (IOException ex){
            ex.printStackTrace();
            System.out.printf("Клиент отключен: %s%n", socket);
        }
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }

    private Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream,"UTF-8");
        return new Scanner(input);
    }

    private boolean isQuitMsg(String message){
        return "bye".equalsIgnoreCase(message);
    }

    private boolean isEmptyMsg(String message){
        return message == null || message.isBlank();
    }

    private void sendResponse(Writer writer) throws IOException {
        System.out.print("Введите сообщение: ");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        writer.write(msg);
        writer.write(System.lineSeparator());
        writer.flush();
    }

}
