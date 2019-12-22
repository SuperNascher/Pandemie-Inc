package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * PathogenEncounteredEvent - A global event
 * This event indicates that a Pathogen has been encountered in some cities.
 */
public class PathogenEncounteredEvent extends PathogenEvent {

    public static final String eventName = "pathogenEncountered";
    public int round;

    public PathogenEncounteredEvent(int round, Pathogen pathogen) {
        super(eventName);
        this.round = round;
        this.pathogen = pathogen;
    }

    public PathogenEncounteredEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
