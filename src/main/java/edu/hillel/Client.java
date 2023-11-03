package edu.hillel;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private final static int PORT = 1111;

    public static void main(String[] args) throws IOException {
        new Client().startClient();
    }

    public void startClient() throws IOException {
        try (Socket socketClient = new Socket("localhost", PORT)) {
            try (PrintWriter printWriter = new PrintWriter(socketClient.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()))) {
                Thread inputThread = new Thread(() -> {
                    try {
                        while (true) {
                            String message = reader.readLine();
                            if (message != null) {
                                System.out.println("message is " + message);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Client disconnected");
                    }
                });
                inputThread.start();

                Scanner scanner = new Scanner(System.in);
                handleClientMessages(printWriter, socketClient.getOutputStream(),scanner);

            }
        }
    }

    public void handleClientMessages(PrintWriter printWriter, OutputStream outputStream, Scanner scanner) throws IOException {
        while (scanner.hasNextLine()) {
            String inputMessage = scanner.nextLine();
            printWriter.println(inputMessage);
            if ("exit".equals(inputMessage)) {
                break;
            } else if (inputMessage.startsWith("file ")) {
                String sendFileError = sendFile(inputMessage, outputStream);
                if (sendFileError != null) {
                    System.out.println("Sending file error: " + sendFileError);
                }
            }
        }
    }

    String sendFile(String message, OutputStream outputStream) throws IOException {
        String fileName = (message.substring(message.lastIndexOf(' ') + 1)).trim();
        if (fileName.isEmpty()) {
            return "File name is empty!";
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return "File not exists!";
        }

        int countBytes;
        byte[] bufferBytes = new byte[8192];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            while ((countBytes = bufferedInputStream.read(bufferBytes)) >= 0) {
                outputStream.write(bufferBytes, 0, countBytes);
                outputStream.flush();
            }
        }
        return null;
    }
}
