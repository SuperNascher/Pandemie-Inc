package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.SimpleEvent;

public class HygienicMeasuresAppliedEvent extends SimpleEvent  {

    public static final String eventName = "hygienicMeasuresApplied";
    public int round;

    public HygienicMeasuresAppliedEvent(int round) {
        super(eventName);
        this.round = round;
    }

    public HygienicMeasuresAppliedEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
