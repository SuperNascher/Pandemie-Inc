package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.BaseEvent;

public class PathogenEncounteredEvent extends BaseEvent  {
	public static String eventName = "pathogenEncountered";

    public Pathogen pathogen;
    public int round;

    public PathogenEncounteredEvent(int round, Pathogen pathogen) {
    	super(eventName);
    	this.round = round;
    	this.pathogen = pathogen;
    }
}
