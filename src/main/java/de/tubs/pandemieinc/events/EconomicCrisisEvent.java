package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class EconomicCrisisEvent extends BaseEvent  {

    public static String eventName = "economicCrisis";

    public int sinceRound;

    public EconomicCrisisEvent(int sinceRound) {
        super(eventName);
        this.sinceRound = sinceRound;
    }
}
