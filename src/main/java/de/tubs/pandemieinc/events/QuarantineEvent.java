package de.tubs.pandemieinc.events;

/**
 * QuarantineEvent - A city event
 * This event indicates that the Pathogens in this city
 * can not infect other cities as long this event exists.
 */
public class QuarantineEvent extends TimedEvent {

    public static final String eventName = "quarantine";

    public QuarantineEvent() {
        super(eventName);
    }

    public QuarantineEvent(int sinceRound, int untilRound) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.untilRound = untilRound;
    }
}
