package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.parser.Config;
import utils.parser.Station;
import utils.parser.Train;

import java.io.IOException;
import java.io.InputStream;

public class MainC {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Config.class.getResourceAsStream("../config.json");
        Config config = mapper.readValue(is, Config.class);

        // Adresse IP et port du serveur
        String serverAddress = config.serverAddress;
        int serverPort = config.serverPort;
        // Configuration de la gare C
        Station station = config.stations.get(2);
        Station nextStation = config.stations.get(0);
        // Config train
        Train train = config.trains.get(2);

        Client client = new Client(serverAddress, serverPort, station, nextStation, train);
        client.start();
    }
}
