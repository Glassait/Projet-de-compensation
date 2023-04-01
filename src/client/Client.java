package client;

import utils.Message;
import utils.parser.Station;
import utils.parser.Train;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private final client.Station station;
    private final Station nextStation;
    private Socket socket;
    private ObjectInputStream inFromServer;
    private ObjectOutputStream outToServer;

    public Client(String serverAddress, int serverPort, Station station, Station nextStation, Train train) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.station = new client.Station(station);
        this.station.addTrain(new client.Train(train, nextStation.getName()));
        this.nextStation = nextStation;
    }

    public void start() {
        try {
            socket = new Socket(serverAddress, serverPort, InetAddress.getByName(station.getAddress()), 0);
            System.out.println(station.getName() + " est connecter au serveur " + serverAddress + ":" + serverPort);
            inFromServer = new ObjectInputStream(socket.getInputStream());
            outToServer = new ObjectOutputStream(socket.getOutputStream());

            Message registerStation = new Message(station.getName() + "|" + station.getAddress(), this.serverAddress);
            outToServer.writeObject(registerStation);

            Thread t = new Thread(this::listenToMessage);
            t.setDaemon(true);
            t.start();

            Thread tr = new Thread(this::checkTrainDepartureTime);
            tr.setDaemon(true);
            tr.start();

            while (true) {}
        } catch (UnknownHostException e) {
            System.err.println("Invalid server address: " + serverAddress);
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        } finally {
            close();
        }
    }

    public void listenToMessage() {
        while (true) {
            try {
                Message message = (Message) inFromServer.readObject();
                synchronized (station.getTrains()) {
                    station.getTrains().add(new client.Train(message.getMessageBody(), station.getName(), nextStation.getName()));
                }
                System.out.println("Le train n°" + message.getMessageBody() + " est arrivé à la gare " + station.getName());
                System.out.println("Nombre de train en gare : " + station.getTrains().size() + "\n");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(e.hashCode());
                break;
            }
        }
    }

    public void checkTrainDepartureTime() {
        while (true) {
            Date now = new Date();
            List<client.Train> trainsToRemove = new ArrayList<>();

            for (client.Train train : station.getTrains()) {
                if (train.getDepartureTime().equals(now)) {
                    System.out.println("Le train n°" + train.getId() + " part en direction de " + train.getDestination() + "\n");
                    trainsToRemove.add(train);
                    try {
                        outToServer.writeObject(new Message(train.getId(), nextStation.getAddress()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            station.getTrains().removeIf(trainsToRemove::contains);
        }
    }

    private void close() {
        System.out.println("CLOSING STATION !");
        try {
            if (socket != null) {
                socket.close();
            }
            if (inFromServer != null) {
                inFromServer.close();
            }
            if (outToServer != null) {
                outToServer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}
