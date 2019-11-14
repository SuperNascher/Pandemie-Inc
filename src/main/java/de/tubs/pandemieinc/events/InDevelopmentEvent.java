package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;

public abstract class InDevelopmentEvent extends BaseEvent {
    public int untilRound;
    public Pathogen pathogen;
    public abstract void setRound(int round);

    public InDevelopmentEvent(String eventName) {
    	super(eventName);
    }
}
