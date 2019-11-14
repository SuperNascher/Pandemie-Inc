package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class InfluenceExertedEvent extends BaseEvent  {

    public static String eventName = "influenceExerted";

    public int round;

    public InfluenceExertedEvent(int round) {
        super(eventName);
        this.round = round;
    }
}
