package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class AntiVaccinationismEvent extends BaseEvent  {

    public static String eventName = "antiVaccinationism";

    public int sinceRound;

    public AntiVaccinationismEvent(int sinceRound) {
        super(eventName);
        this.sinceRound = sinceRound;
    }
}
