package client;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private final String name;
    private final String address;
    private final List<Train> trains;

    public Station(utils.parser.Station station) {
        this.name = station.getName();
        this.address = station.getAddress();
        this.trains = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void addTrain(Train train) {
        trains.add(train);
    }

    public synchronized List<Train> getTrains() {
        return trains;
    }
}
