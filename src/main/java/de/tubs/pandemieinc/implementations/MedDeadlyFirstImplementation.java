package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.Action;
import de.tubs.pandemieinc.ActionHelper;
import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.Round;
import de.tubs.pandemieinc.events.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MedDeadlyFirstImplementation implements PandemieImpl {

    final int quarantineRoundlength = 2; // ändern wenn fastestFirst

    public MedDeadlyFirstImplementation() {}

    public String selectAction(Round round) {
        if (!round.outcome.equals("pending") || round.points < 40) {
            return ActionPrinter.endRound();
        }

        Map<City, Pathogen> singleCityPathogens = ActionHelper.getCitiesWithOnePathogen(round);
        for (Iterator<City> it = singleCityPathogens.keySet().iterator(); it.hasNext(); ) {
            City city = it.next();
            for (BaseEvent event : city.events) {
                if (event.eventName == QuarantineEvent.eventName) {
                    it.remove();
                }
            }
        }

        if (!singleCityPathogens.isEmpty()) {
            Pathogen deadlyPath = ActionHelper.findDeadlyPathogen(singleCityPathogens.values());
            City pathCity = null;
            for (Map.Entry<City, Pathogen> entry : singleCityPathogens.entrySet()) {
                if (entry.getValue() == deadlyPath) {
                    pathCity = entry.getKey();
                    break;
                }
            }

            round.points -=
                    (Action.PUTUNDERQUARANTINE.baseCost
                            + quarantineRoundlength * Action.PUTUNDERQUARANTINE.roundCost);
            if (pathCity != null) {
                return ActionPrinter.putUnderQuarantine(pathCity, quarantineRoundlength);
            }
        }

        List<Pathogen> acutePathogens = ActionHelper.pathogensNotInQuarantine(round);
        if (acutePathogens.size() > 0) {
            Pathogen medPath = ActionHelper.findSlowlyPathogen(acutePathogens);
            if (!ActionHelper.isMedicationInDevelopment(round, medPath)
                    && !ActionHelper.isMedicationAvailable(round, medPath)
                    && round.points - Action.DEVELOPMEDICATION.baseCost >= 20) {
                round.points -= Action.DEVELOPMEDICATION.baseCost;
                return ActionPrinter.developMedication(medPath);
            } else if (ActionHelper.isMedicationAvailable(round, medPath)
                    && round.points - Action.DEPLOYMEDICATION.baseCost >= 20) {
                List<City> cities = round.cities.values().stream().collect(Collectors.toList());
                City city = null;
                double infectedPopTemp = 0;
                double infectedPop = 0;
                double prevalence = 0;

                for (City tempC : cities) {
                    if (!ActionHelper.isCityInfectedWithPathogen(tempC, medPath)) {
                        continue;
                    }
                    for (BaseEvent tempE : tempC.events) {
                        if (tempE.eventName == OutbreakEvent.eventName) {
                            OutbreakEvent event = (OutbreakEvent) tempE;
                            if (event.pathogen == medPath) {
                                prevalence = event.prevalence;
                            }
                        }
                    }

                    infectedPopTemp = tempC.population * prevalence;
                    if (infectedPop < infectedPopTemp) {
                        city = tempC;
                        infectedPop = infectedPopTemp;
                    }
                }

                if (city != null) {
                    return ActionPrinter.deployMedication(medPath, city);
                }
            }
        }
        return ActionPrinter.endRound();
    }
}
