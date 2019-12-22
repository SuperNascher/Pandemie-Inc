package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.City;

/**
 * ConnectionClosedEvent - A city event
 * This event indicates that the connection from the given city to the
 * destination city can not be used as long this event exists.
 */
public class ConnectionClosedEvent extends BaseEvent {

    public static final String eventName = "connectionClosed";

    public City city;
    public int sinceRound;
    public int untilRound;

    public ConnectionClosedEvent() {
        super(eventName);
    }
}
