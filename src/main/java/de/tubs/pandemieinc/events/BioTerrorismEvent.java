package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.PathogenEvent;

public class BioTerrorismEvent extends PathogenEvent  {

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
