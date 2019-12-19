package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.events.BaseEvent;

public class ConnectionClosedEvent extends BaseEvent  {

    public static final String eventName = "connectionClosed";

    public City city;
    public int sinceRound;
    public int untilRound;

    public ConnectionClosedEvent() {
        super(eventName);
    }
}
