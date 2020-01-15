package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * VaccineAvailableEvent - A global event This event indicates that the vaccine for the given
 * Pathogen is available for all cities.
 */
public class VaccineAvailableEvent extends PathogenEvent {

    public static String eventName = "vaccineAvailable";

    public int sinceRound;

    public VaccineAvailableEvent(Pathogen pathogen, int sinceRound) {
        super(eventName);
        this.pathogen = pathogen;
        this.sinceRound = sinceRound;
    }

    public VaccineAvailableEvent() {
        super(eventName);
        this.pathogen = null;
        this.sinceRound = -1;
    }

    @Override
    public void setRound(int round) {
        this.sinceRound = round;
    }
}
