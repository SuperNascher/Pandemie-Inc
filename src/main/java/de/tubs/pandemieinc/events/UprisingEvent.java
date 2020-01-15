package de.tubs.pandemieinc.events;

/**
 * UprisingEvent - City event
 *
 * <p>We believe that this event has an impact on the government attribute of the given city.
 */
public class UprisingEvent extends BaseEvent {

    public static String eventName = "uprising";

    public int sinceRound;
    public int participants;

    public UprisingEvent(int sinceRound, int participants) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.participants = participants;
    }
}
