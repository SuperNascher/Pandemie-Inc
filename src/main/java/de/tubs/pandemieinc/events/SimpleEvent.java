package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public abstract class SimpleEvent extends BaseEvent {

    public SimpleEvent(String eventName) {
        super(eventName);
    }

    public abstract void setRound(int round);
}
