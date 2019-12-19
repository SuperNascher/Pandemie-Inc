package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.SimpleEvent;

public class AntiVaccinationismEvent extends SimpleEvent  {

    public static final String eventName = "antiVaccinationism";
    public int sinceRound;

    public AntiVaccinationismEvent(int sinceRound) {
        super(eventName);
        this.sinceRound = sinceRound;
    }

    public AntiVaccinationismEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.sinceRound = round;
    }
}
