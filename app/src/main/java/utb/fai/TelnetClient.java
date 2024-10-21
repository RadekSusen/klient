package utb.fai;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TelnetClient {

    private String serverIp;
    private int port;
    private volatile boolean running = true; // Sdílený stav pro ukončení vláken

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try (Socket socket = new Socket(serverIp, port)) {
            System.out.println("Connected to " + serverIp + " on port " + port);

            // Vlákno pro příjem zpráv
            Thread receiveThread = new Thread(() -> {
                try {
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    while (running && !socket.isClosed()) {
                        if (input.available() > 0) {
                            String response = reader.readLine();
                            if (response != null) {
                                System.out.println("Server: " + response);
                            }
                        }
                        Thread.sleep(20); // Snížení zátěže CPU
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println("Connection closed.");
                }
            });

            // Vlákno pro odesílání zpráv
            Thread sendThread = new Thread(() -> {
                try {
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    Scanner scanner = new Scanner(System.in);

                    while (running && !socket.isClosed()) {
                        if (scanner.hasNextLine()) {
                            String message = scanner.nextLine();

                            // Pokud uživatel zadá "/QUIT", ukončíme spojení
                            if (message.equalsIgnoreCase("/QUIT")) {
                                writer.println(message);
                                running = false;  // Nastavení příznaku pro ukončení
                                socket.close();    // Zavření soketu
                                System.out.println("Closing connection...");
                                break;
                            }

                            // Odeslání zprávy na server
                            writer.println(message);
                        }
                        Thread.sleep(20); // Snížení zátěže CPU
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println("Connection closed.");
                }
            });

            // Spuštění obou vláken
            receiveThread.start();
            sendThread.start();

            // Čekání na dokončení vláken
            receiveThread.join();
            sendThread.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}