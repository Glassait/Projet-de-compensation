package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Map<InetAddress, Station> stations;
    private final ServerSocket serverSocket;
    private boolean running;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.stations = new HashMap<>();
        this.running = false;
    }

    public void start() {
        System.out.println("Server started.");
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                StationHandler stationHandler = new StationHandler(socket, this);

                Thread t = new Thread(stationHandler);
                t.start();
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    public void stop() {
        try {
            running = false;
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    public synchronized void registerStation(Station station) {
        this.stations.put(station.getAddress(), station);
        System.out.println("Registered station: " + station.getName());
    }

    public synchronized void unregisterStation(Station station) {
        this.stations.remove(station.getAddress());
        System.out.println("Unregistered station: " + station.getName());
    }

    public Map<InetAddress, Station> getStations() {
        return stations;
    }
}
