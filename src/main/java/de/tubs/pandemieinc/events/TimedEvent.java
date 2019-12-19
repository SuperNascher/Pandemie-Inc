package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public abstract class TimedEvent extends BaseEvent {

    public int sinceRound;
    public int untilRound;

    public TimedEvent(String eventName) {
        super(eventName);
    }

    public void setSinceRound(int round) {
        this.sinceRound = round;
    }

    public void setUntilRound(int round) {
        this.untilRound = round;
    }
}
