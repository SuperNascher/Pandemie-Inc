package de.tubs.pandemieinc.events;

/**
 * ElectionsCalledEvent - City event
 *
 * <p>This event indicates that the action "callElections" was executed on the given city.
 */
public class ElectionsCalledEvent extends SimpleEvent {

    public static final String eventName = "electionsCalled";
    public int round;

    public ElectionsCalledEvent(int round) {
        super(eventName);
        this.round = round;
    }

    public ElectionsCalledEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
