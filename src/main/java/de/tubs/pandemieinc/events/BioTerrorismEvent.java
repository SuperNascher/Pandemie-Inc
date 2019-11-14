package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.BaseEvent;

public class BioTerrorismEvent extends BaseEvent  {

    public static String eventName = "bioTerrorism";

    public Pathogen pathogen;
    public int round;

    public BioTerrorismEvent(Pathogen pathogen, int round) {
        super(eventName);
        this.pathogen = pathogen;
        this.round = round;
    }
}
