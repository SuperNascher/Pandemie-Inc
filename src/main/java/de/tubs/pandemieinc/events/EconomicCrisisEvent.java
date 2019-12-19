package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.SimpleEvent;

public class EconomicCrisisEvent extends SimpleEvent  {

    public static final String eventName = "economicCrisis";
    public int sinceRound;

    public EconomicCrisisEvent(int sinceRound) {
        super(eventName);
        this.sinceRound = sinceRound;
    }

    public EconomicCrisisEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.sinceRound = round;
    }
}
