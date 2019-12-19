package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.BaseEvent;

public class OutbreakEvent extends BaseEvent  {

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
