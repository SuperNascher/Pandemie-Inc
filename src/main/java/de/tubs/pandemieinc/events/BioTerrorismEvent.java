package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * BioTerrorismEvent - Global event
 *
 * This event indicates that a new Pathogen is introduced
 * as biological weapon.
 */
public class BioTerrorismEvent extends PathogenEvent {

    public static final String eventName = "bioTerrorism";
    public int round;

    public BioTerrorismEvent(Pathogen pathogen, int round) {
        super(eventName);
        this.pathogen = pathogen;
        this.round = round;
    }

    public BioTerrorismEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
