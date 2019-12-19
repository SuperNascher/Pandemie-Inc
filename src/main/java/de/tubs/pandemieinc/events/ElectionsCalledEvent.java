package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.SimpleEvent;

public class ElectionsCalledEvent extends SimpleEvent  {

    public static final String eventName = "electionsCalled";
    public int round;

    public ElectionsCalledEvent(int round) {
        super(eventName);
        this.round = round;
    }

    public ElectionsCalledEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
