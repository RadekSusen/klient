package utb.fai;

import java.io.*;
import java.net.*;

public class TelnetClient {

    private String serverIp;
    private int port;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket(serverIp, port);

            Thread inputThread = new Thread(() -> {
                try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String serverMessage;
                    while ((serverMessage = socketReader.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            });

            Thread outputThread = new Thread(() -> {
                try (BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
                        BufferedWriter socketWriter = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()))) {
                    String userInput;
                    while ((userInput = userInputReader.readLine()) != null) {
                        if ("/QUIT".equalsIgnoreCase(userInput.trim())) {
                            System.out.println("Closing connection...");
                            socket.close();
                            break;
                        }
                        socketWriter.write(userInput + "\r\n");
                        socketWriter.flush();
                    }
                } catch (IOException e) {
                    System.err.println("Error writing to server: " + e.getMessage());
                }
            });

            inputThread.start();
            outputThread.start();

            inputThread.join();
            outputThread.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error during communication: " + e.getMessage());
        }
    }
}
