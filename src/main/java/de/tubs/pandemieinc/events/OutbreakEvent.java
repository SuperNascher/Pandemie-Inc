package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/**
 * OutbreakEvent - A city event This event indicates that a Pathogen has been "found" in the given
 * city.
 */
public class OutbreakEvent extends BaseEvent {

    public static String eventName = "outbreak";

    public Pathogen pathogen;
    public double prevalence;
    public int sinceRound;

    public OutbreakEvent(Pathogen pathogen, double prevalence, int sinceRound) {
        super(eventName);
        this.pathogen = pathogen;
        this.prevalence = prevalence;
        this.sinceRound = sinceRound;
    }
}
