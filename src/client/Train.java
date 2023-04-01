package client;

import java.util.Date;

public class Train {
    private final String id;
    private final String origin;
    private final String destination;
    private final Date departureTime;

    public Train(utils.parser.Train train, String destination) {
        this.id = train.getId();
        this.origin = train.getInitialStation();
        this.destination = destination;
        this.departureTime = new Date(new Date().getTime() + 30 * 1000);
    }

    public Train(String id, String origin, String destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = new Date(new Date().getTime() + 30 * 1000);
    }

    public String getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }
}
