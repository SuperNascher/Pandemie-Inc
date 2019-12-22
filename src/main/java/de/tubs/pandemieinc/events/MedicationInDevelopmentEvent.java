package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * MedicationInDevelopmentEvent - An global event
 * This event indicates that the medication for the given pathogen
 * is indevelopment.
 */
public class MedicationInDevelopmentEvent extends InDevelopmentEvent {

    public static final String eventName = "medicationInDevelopment";

    public MedicationInDevelopmentEvent(int sinceRound, int untilRound, Pathogen pathogen) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.untilRound = untilRound;
        this.pathogen = pathogen;
    }

    public MedicationInDevelopmentEvent() {
        super(eventName);
    }
}
