package de.tubs.pandemieinc.events;

import de.tubs.pandemieinc.events.BaseEvent;

public class CampaignLaunchedEvent extends BaseEvent  {

    public static String eventName = "campaignLaunched";

    public int round;

    public CampaignLaunchedEvent(int round) {
        super(eventName);
        this.round = round;
    }
}
