package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class LargeScalePanicEvent extends BaseEvent  {

    public static String eventName = "largeScalePanic";

    public int sinceRound;

    public LargeScalePanicEvent(int sinceRound) {
        super(eventName);
        this.sinceRound = sinceRound;
    }
}
