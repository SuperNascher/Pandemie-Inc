package de.tubs.pandemieinc;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.tubs.pandemieinc.Action;
import de.tubs.pandemieinc.Round;
import de.tubs.pandemieinc.events.BaseEvent;
import de.tubs.pandemieinc.events.PathogenEncounteredEvent;

public class ActionHelper {

    public static List<Action> getPossibleActions(Round round) {
        List<Action> possibleActions = new ArrayList<Action>();

        // Filter through non dependent actions
        for (Action action : Action.nonDependentActions()) {
            if ((action.baseCost + action.roundCost) <= round.points) {
                possibleActions.add(action);
            }
        }

        Map<String, Action> dependentActions = Action.dependentActions();
        for(BaseEvent event : round.events) {
            if (event.eventName == PathogenEncounteredEvent.eventName) {
                continue;
            }

            Action action = dependentActions.get(event.eventName);
            if (action != null) {
                possibleActions.add(action);
            }
        }
        return possibleActions;
    }
}
