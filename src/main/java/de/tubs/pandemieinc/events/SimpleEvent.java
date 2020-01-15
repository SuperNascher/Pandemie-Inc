package de.tubs.pandemieinc.events;

/** Base class for events with a round integer. */
public abstract class SimpleEvent extends BaseEvent {

    public SimpleEvent(String eventName) {
        super(eventName);
    }

    public abstract void setRound(int round);
}
