package server;

import utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class StationHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private Station station;
    private ObjectInputStream inFromStation;
    private ObjectOutputStream outToStation;

    public StationHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            outToStation = new ObjectOutputStream(socket.getOutputStream());
            inFromStation = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Register station
            Message message = (Message) inFromStation.readObject();
            String[] stationConfig = message.getMessageBody().split("\\|");
            station = new Station(stationConfig[0], InetAddress.getByName(stationConfig[1]), this);
            server.registerStation(station);

            while (true) {
                message = (Message) inFromStation.readObject();
                if (!message.getAddress().equals("localhost")) {
                    Station station = server.getStations().get(InetAddress.getByName(message.getAddress()));
                    station.getHandler().sendMessage(message.getMessageBody(), "");
                }

                if (message.getMessageBody().equalsIgnoreCase("quit")) {
                    socket.close();
                    System.out.println("Station disconnected");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client input: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
                server.unregisterStation(station);
            } catch (IOException e) {
                System.err.println("Error closing client connection: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String message, String address) {
        try {
            outToStation.writeObject(new Message(message, address));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
