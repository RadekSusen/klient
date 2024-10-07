package utb.fai;


public class App {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java App <host> <port>");
            return;
        }

        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
            return;
        }

        TelnetClient telnetClient = new TelnetClient(host, port);
        telnetClient.run(); // run telnet client
    }
}