package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

public abstract class PathogenEvent extends BaseEvent {

    public Pathogen pathogen;

    public PathogenEvent(String eventName) {
        super(eventName);
    }

    public abstract void setRound(int round);

    public void setPathogen(Pathogen pathogen) {
        this.pathogen = pathogen;
    }
}
