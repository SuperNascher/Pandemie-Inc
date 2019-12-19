package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.SimpleEvent;

public class CampaignLaunchedEvent extends SimpleEvent  {

    public static final String eventName = "campaignLaunched";
    public int round;

    public CampaignLaunchedEvent(int round) {
        super(eventName);
        this.round = round;
    }

    public CampaignLaunchedEvent() {
        super(eventName);
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }
}
