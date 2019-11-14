package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class UprisingEvent extends BaseEvent  {

    public static String eventName = "uprising";

    public int sinceRound;
    public int participants;

    public UprisingEvent(int sinceRound, int participants) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.participants = participants;
    }
}
