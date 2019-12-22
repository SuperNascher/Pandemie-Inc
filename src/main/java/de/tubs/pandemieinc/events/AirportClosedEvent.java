package de.tubs.pandemieinc.events;

/**
 * AirportClosedEvent - City event
 *
 * This event indicates that the airport of the given
 * city is closed.
 */
public class AirportClosedEvent extends TimedEvent {

    public static final String eventName = "airportClosed";

    public AirportClosedEvent() {
        super(eventName);
    }

    public AirportClosedEvent(int sinceRound, int untilRound) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.untilRound = untilRound;
    }
}
