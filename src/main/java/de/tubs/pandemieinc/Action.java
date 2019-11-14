package de.tubs.pandemieinc;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import de.tubs.pandemieinc.events.VaccineAvailableEvent;
import de.tubs.pandemieinc.events.MedicationAvailableEvent;

public enum Action {

    ENDROUND("endRound", 0, 0),
    PUTUNDERQUARANTINE("putUnderQuarantine", 20,10),
    CLOSEAIRPORT("closeAirport", 15, 5),
    CLOSECONNECTION("closeConnection", 3, 3),
    DEVELOPVACCINE("developVaccine", 40, 0),
    DEPLOYVACCINE("deployVaccine", 5, 0),
    DEVELOPMEDICATION("developMedication", 20, 0),
    DEPLOYMEDICATION("deployMedication", 10, 0),
    EXERTINFLUENCE("exertInfluence", 3, 0),
    CALLELECTIONS("callElections", 3, 0),
    APPLYHYGIENICMEASURES("applyHygienicMeasures", 3, 0),
    LAUNCHCAMPAIGN("launchCampaign", 3, 0);

    public final String type;
    public final int baseCost;
    public final int roundCost;

    Action(String type, int baseCost, int roundCost) {
        this.type = type;
        this.baseCost = baseCost;
        this.roundCost = roundCost;
    }

    public static Action[] nonDependentActions() {
        Action[] actions = {
            Action.APPLYHYGIENICMEASURES,
            Action.CALLELECTIONS,
            Action.CLOSEAIRPORT,
            Action.CLOSECONNECTION,
            Action.DEVELOPMEDICATION,
            Action.DEVELOPVACCINE,
            Action.ENDROUND,
            Action.EXERTINFLUENCE,
            Action.LAUNCHCAMPAIGN,
            Action.PUTUNDERQUARANTINE,
        };
        return actions;
    }

    public static Map<String, Action> dependentActions() {
        Map<String, Action> actions = new HashMap<String, Action>();
        actions.put(VaccineAvailableEvent.eventName, DEPLOYVACCINE);
        actions.put(MedicationAvailableEvent.eventName, DEPLOYMEDICATION);
        return actions;
    }
}
