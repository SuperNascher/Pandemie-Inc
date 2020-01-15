package de.tubs.pandemieinc;

import de.tubs.pandemieinc.events.MedicationAvailableEvent;
import de.tubs.pandemieinc.events.VaccineAvailableEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Possible Actions mapped in an enum. Contains the base and round cost and the JSON interpretation
 * string.
 */
public enum Action {
    ENDROUND("endRound", 0, 0),
    PUTUNDERQUARANTINE("putUnderQuarantine", 20, 10),
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

    /**
     * Get all actions, that does not have a dependency to an event.
     *
     * @return Action array with all possible actions, that does not have any required event.
     */
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

    /**
     * Get all actions, that have a event dependency.
     *
     * @return A Map, where the key is the required event name (as JSON String) and the value is the
     *     Action enum.
     */
    public static Map<String, Action> dependentActions() {
        Map<String, Action> actions = new HashMap<String, Action>();
        actions.put(VaccineAvailableEvent.eventName, DEPLOYVACCINE);
        actions.put(MedicationAvailableEvent.eventName, DEPLOYMEDICATION);
        return actions;
    }
}
