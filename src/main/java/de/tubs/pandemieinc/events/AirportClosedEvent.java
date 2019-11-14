package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.events.BaseEvent;

public class AirportClosedEvent extends BaseEvent  {

    public static String eventName = "airportClosed";

    public int sinceRound;
    public int untilRound;

    public AirportClosedEvent() {
        super(eventName);
    }
}
