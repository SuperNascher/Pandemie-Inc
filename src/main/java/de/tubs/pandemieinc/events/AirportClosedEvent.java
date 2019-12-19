package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.TimedEvent;

public class AirportClosedEvent extends TimedEvent  {

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
