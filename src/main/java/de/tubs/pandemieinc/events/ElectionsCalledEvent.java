package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class ElectionsCalledEvent extends BaseEvent  {

    public static String eventName = "electionsCalled";

    public int round;

    public ElectionsCalledEvent(int round) {
        super(eventName);
        this.round = round;
    }
}
