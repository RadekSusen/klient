package utb.fai;

public class App {

    public static void main(String[] args) {
        // Kontrola, zda byly předány správné argumenty (IP adresa a port)
        if (args.length < 2) {
            System.out.println("Usage: java App <IP Address> <Port>");
            return;
        }

        try {
            // Zpracování vstupních parametrů
            String ipAddress = args[0]; // IP adresa
            int port = Integer.parseInt(args[1]); // Číslo portu (musí být celé číslo)

            // Vytvoření instance TelnetClient s předanou IP adresou a portem
            TelnetClient telnetClient = new TelnetClient(ipAddress, port);

            // Spuštění telnet klienta
            telnetClient.run();

        } catch (NumberFormatException e) {
            // Ošetření chyby při neplatném číslu portu
            System.out.println("Invalid port number. Please provide a valid integer for the port.");
        } catch (Exception e) {
            // Ošetření obecné chyby
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
