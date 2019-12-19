package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.TimedEvent;

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
