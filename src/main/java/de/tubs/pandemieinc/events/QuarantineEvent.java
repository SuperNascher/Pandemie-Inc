package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.events.BaseEvent;

public class QuarantineEvent extends BaseEvent  {

    public static String eventName = "quarantine";

    public int sinceRound;
    public int untilRound;

    public QuarantineEvent() {
        super(eventName);
    }
}
