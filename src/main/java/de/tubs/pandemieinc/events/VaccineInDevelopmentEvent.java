package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.InDevelopmentEvent;

public class VaccineInDevelopmentEvent extends InDevelopmentEvent {
    
    public static final String eventName = "vaccineInDevelopment";
    public int sinceRound;
    
    public VaccineInDevelopmentEvent(int sinceRound, int untilRound, Pathogen pathogen) {
        super(eventName);
        this.sinceRound = sinceRound;
        this.untilRound = untilRound;
        this.pathogen = pathogen;
    }
    
    public VaccineInDevelopmentEvent() {
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
