package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.PathogenEvent;

public class MedicationDeployedEvent extends PathogenEvent  {

    public static String eventName = "medicationDeployed";

    public int round;

    public MedicationDeployedEvent(Pathogen pathogen, int round) {
        super(eventName);
        this.pathogen = pathogen;
        this.round = round;
    }

    public MedicationDeployedEvent() {
        super(eventName);
        this.round = -1;
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
