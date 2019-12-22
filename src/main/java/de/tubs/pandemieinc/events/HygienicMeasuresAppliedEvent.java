package de.tubs.pandemieinc.events;

/**
 * HygienicMeasuresAppliedEvent - A city event
 * This event indicates that the action "applyHygienicMeasures" was used on the
 * given city.
 */
public class HygienicMeasuresAppliedEvent extends SimpleEvent {

    public static final String eventName = "hygienicMeasuresApplied";
    public int round;

    public HygienicMeasuresAppliedEvent(int round) {
        super(eventName);
        this.round = round;
    }

    public HygienicMeasuresAppliedEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
