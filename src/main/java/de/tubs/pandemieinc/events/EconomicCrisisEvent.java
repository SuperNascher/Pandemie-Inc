package de.tubs.pandemieinc.events;

/**
 * EconomicCrisisEvent - Global event
 *
 * <p>This event indicates that ecenomy on random cities will decrease on each round.
 */
public class EconomicCrisisEvent extends SimpleEvent {

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
