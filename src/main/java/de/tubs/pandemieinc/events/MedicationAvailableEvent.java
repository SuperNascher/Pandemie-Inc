package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * MedicationAvailableEvent - An global event This event indicates that the medication for the given
 * pathogen is available to use for the cities.
 */
public class MedicationAvailableEvent extends PathogenEvent {

    public static String eventName = "medicationAvailable";

    public int sinceRound;

    public MedicationAvailableEvent(Pathogen pathogen, int sinceRound) {
        super(eventName);
        this.pathogen = pathogen;
        this.sinceRound = sinceRound;
    }

    public MedicationAvailableEvent() {
        super(eventName);
        this.pathogen = null;
        this.sinceRound = -1;
    }

    @Override
    public void setRound(int round) {
        this.sinceRound = round;
    }
}
