package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.SimpleEvent;

public class InfluenceExertedEvent extends SimpleEvent  {

    public static final String eventName = "influenceExerted";
    public int round;

    public InfluenceExertedEvent(int round) {
        super(eventName);
        this.round = round;
    }

    public InfluenceExertedEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
