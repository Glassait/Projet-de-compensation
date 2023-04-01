package server;

import java.net.InetAddress;

public class Station {
    private final String name;
    private final InetAddress address;
    private final StationHandler handler;
//    private final List<Train> trains;

    public Station(String name, InetAddress address, StationHandler handler) {
        this.name = name;
        this.address = address;
        this.handler = handler;
//        trains = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public StationHandler getHandler() {
        return handler;
    }

 /*   public void addTrain(Train train) {
        trains.add(train);
        handler.sendMessage("Le train " + train.getId() + " est arrivé à la gare " + train.getOrigin(), address.toString());
    }

    public void removeTrain(Train train) {
        trains.remove(train);
        handler.sendMessage("Le train " + train.getId() + " part en direction de " + train.getDestination(), address.toString());
    }

    public List<Train> getTrains() {
        return trains;
    }*/
}
