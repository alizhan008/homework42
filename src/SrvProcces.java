import java.io.*;
import java.net.Socket;
import java.util.*;

public class SrvProcces {
    public void handle(Socket socket) {

        System.out.printf("Подключен клиент: %s%n", EchoServer.stringSocketMap.get(socket));

        try (Scanner reader = getReader(socket)) {
            String message = reader.nextLine().strip();
            System.out.printf("%s отправил: %s%n", EchoServer.stringSocketMap.get(socket), message);
            if (isEmptyMsg(message) || isQuitMsg(message)) {
                System.out.println("Пустая строка!");
            }
            for (Map.Entry<Socket, String> sockets : EchoServer.stringSocketMap.entrySet()) {
                PrintWriter writer = getWriter(sockets.getKey());
                System.out.printf("Отправить сообщение: %s: %n",EchoServer.stringSocketMap.get(socket));
                sendResponse(writer);

            }
        } catch (NoSuchElementException e) {
            System.out.printf("Клиент %s закрыл соединение!%n", EchoServer.stringSocketMap.get(socket));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.printf("Клиент отключен: %s%n", EchoServer.stringSocketMap.get(socket));
        }
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }

    private Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream, "UTF-8");
        return new Scanner(input);
    }

    private boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }

    public void sendResponse(Writer writer) throws IOException {
        try {
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            writer.write(msg);
            writer.write(System.lineSeparator());
            writer.flush();
        } catch (NoSuchElementException e) {
            System.out.println("Вы ничего не ввели!");
        }
    }
}
