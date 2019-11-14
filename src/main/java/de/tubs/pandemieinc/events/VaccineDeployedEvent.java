package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.PathogenEvent;

public class VaccineDeployedEvent extends PathogenEvent  {

    public static String eventName = "vaccineDeployed";

    public int round;

    public VaccineDeployedEvent(Pathogen pathogen, int round) {
        super(eventName);
        this.pathogen = pathogen;
        this.round = round;
    }

    public VaccineDeployedEvent() {
        super(eventName);
        this.round = -1;
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}