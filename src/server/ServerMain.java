package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ServerMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8081);
             Socket clientSocket = serverSocket.accept();
             InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter writer = new PrintWriter(outputStream)) {
            writer.println("Hello! How are you?");
            writer.flush();
            boolean suspect = false;
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(message);
                if (!suspect && (message.contains("ъ")
                        || message.contains("э")
                        || message.contains("ё")
                        || message.contains("ы"))) {
                    writer.println("що таке паляниця?");
                    writer.flush();
                    suspect = true;
                    continue;
                }
                if (message.contains("хліб")) {
                    writer.println(LocalDateTime.now()
                            + System.lineSeparator()
                            + "Goodbye!");
                    writer.flush();
                    suspect = false;
                }
                if (suspect) {
                    clientSocket.shutdownInput();
                }
            }
            System.out.println("Client disconnected, server will stop now.");
        } catch (IOException e) {
            throw new RuntimeException("No connection", e);
        }
    }
}
