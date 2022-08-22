import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SrvProcces {

    public void handle(Socket socket) {
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
        System.out.println("Введите сообщение: ");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        writer.write(msg);
        writer.write(System.lineSeparator());
        writer.flush();
    }
}
