package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

/** This is the abstract/base class for "indevelopment" events. */
public abstract class InDevelopmentEvent extends BaseEvent {

    public int sinceRound;
    public int untilRound;
    public Pathogen pathogen;

    public InDevelopmentEvent(String eventName) {
        super(eventName);
    }

    public void setSinceRound(int round) {
        this.sinceRound = round;
    }

    public void setUntilRound(int round) {
        this.untilRound = round;
    }

    public void setPathogen(Pathogen pathogen) {
        this.pathogen = pathogen;
    }
}
