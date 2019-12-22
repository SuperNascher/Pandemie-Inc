package de.tubs.pandemieinc.events;

public class LargeScalePanicEvent extends SimpleEvent {

    public static final String eventName = "largeScalePanic";
    public int sinceRound;

    public LargeScalePanicEvent(int sinceRound) {
        super(eventName);
        this.sinceRound = sinceRound;
    }

    public LargeScalePanicEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.sinceRound = round;
    }
}
