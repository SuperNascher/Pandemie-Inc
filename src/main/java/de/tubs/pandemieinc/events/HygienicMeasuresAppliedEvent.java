package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class HygienicMeasuresAppliedEvent extends BaseEvent  {

    public static String eventName = "hygienicMeasuresApplied";

    public int round;

    public HygienicMeasuresAppliedEvent(int round) {
        super(eventName);
        this.round = round;
    }
}
