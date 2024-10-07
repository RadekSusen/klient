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
        Thread receiveThread = new Thread(() -> {
        });
        Thread sendThread = new Thread(() -> {
        });

        try (Socket socket = new Socket(serverIp, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    String message = stdIn.readLine();
                    if ("/QUIT".equalsIgnoreCase(message)) {
                        break;
                    }
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            receiveThread.start();
            sendThread.start();
            try {
                sendThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}