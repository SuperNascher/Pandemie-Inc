package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * VaccineInDevelopmentEvent - A global event
 * This event indicates that the vaccine for the given
 * Pathogen is in development.
 */
public class VaccineInDevelopmentEvent extends InDevelopmentEvent {

    public static final String eventName = "vaccineInDevelopment";

    public VaccineInDevelopmentEvent(int sinceRound, int untilRound, Pathogen pathogen) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.untilRound = untilRound;
        this.pathogen = pathogen;
    }

    public VaccineInDevelopmentEvent() {
        super(eventName);
    }
}
