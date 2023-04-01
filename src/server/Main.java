package server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Server server = new Server(8000);
            server.start();
            System.out.println("Press ENTER to stop the server.");
            scanner.nextLine();
            server.stop();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}
