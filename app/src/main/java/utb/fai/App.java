package utb.fai;

public class App {

    public static void main(String[] args) {
        // TODO: Implement input parameter processing
        String smtpServer = args[0];
        int smtpPort = Integer.parseInt(args[1]);

        TelnetClient telnetClient = new TelnetClient(smtpServer, smtpPort);
        telnetClient.run(); // run telnet client
    }
}
