package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.InDevelopmentEvent;

public class MedicationInDevelopmentEvent extends InDevelopmentEvent {
    
    public static String eventName = "medicationInDevelopment";
    public int sinceRound;
    
    public MedicationInDevelopmentEvent(int sinceRound, int untilRound, Pathogen pathogen) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.untilRound = untilRound;
        this.pathogen = pathogen;
    }
    
    public MedicationInDevelopmentEvent() {
        super(eventName);
        this.sinceRound = -1;
        this.untilRound = -1;
        this.pathogen = null;
    }
    
    @Override
    public void setRound(int round) {
        this.sinceRound = round;
    }
}
