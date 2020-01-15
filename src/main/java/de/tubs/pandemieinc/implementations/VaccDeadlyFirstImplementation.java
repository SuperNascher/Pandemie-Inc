package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.Action;
import de.tubs.pandemieinc.ActionHelper;
import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.Round;
import de.tubs.pandemieinc.events.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VaccDeadlyFirstImplementation implements PandemieImpl {

    final int quarantineRoundlength = 2; // ändern wenn fastestFirst

    public VaccDeadlyFirstImplementation() {}

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
            Pathogen fastPathogen = ActionHelper.findDeadlyPathogen(singleCityPathogens.values());
            City pathCity = null;
            for (Map.Entry<City, Pathogen> entry : singleCityPathogens.entrySet()) {
                if (entry.getValue() == fastPathogen) {
                    pathCity = entry.getKey();
                    break;
                }
            }
            if (pathCity != null) {
                if (round.round == 1) {
                    return ActionPrinter.putUnderQuarantine(pathCity, quarantineRoundlength);
                }

                for (BaseEvent tempE : pathCity.events) {
                    if (tempE.eventName == OutbreakEvent.eventName) {
                        OutbreakEvent outEvent = (OutbreakEvent) tempE;
                        if (outEvent.sinceRound != 1) {
                            return ActionPrinter.putUnderQuarantine(
                                    pathCity, quarantineRoundlength);
                        }
                    }
                }
            }
        }

        if (round.points < Action.DEVELOPVACCINE.baseCost - 20) {
            return ActionPrinter.endRound();
        }
        List<Pathogen> acutePathogens = ActionHelper.pathogensNotInQuarantine(round);
        if (acutePathogens.size() > 1) {
            Pathogen vaccPath = ActionHelper.findSlowlyPathogen(acutePathogens);
            if (!ActionHelper.isVaccineInDevelopment(round, vaccPath)
                    && !ActionHelper.isVaccineAvailable(round, vaccPath)
                    && round.points - Action.DEVELOPVACCINE.baseCost >= 0) {
                return ActionPrinter.developVaccine(vaccPath);

            } else if (ActionHelper.isVaccineAvailable(round, vaccPath)
                    && round.points - Action.DEPLOYVACCINE.baseCost >= 20) {
                City city = null;
                int cultureValue = -9;
                int tempCulture;

                Collection<City> cities = round.cities.values();
                int newestPathogenRound =
                        ActionHelper.findHighestPathogenRound(
                                cities, vaccPath, ActionHelper::isVaccineDeployed);
                for (City tempC :
                        ActionHelper.getCitiesWithGivenOutbreakRound(newestPathogenRound, cities)) {
                    if (ActionHelper.isCityInfectedWithPathogen(tempC, vaccPath)
                            || ActionHelper.isVaccineDeployed(tempC, vaccPath)) {
                        continue;
                    }
                    tempCulture =
                            tempC.economy.level
                                    + tempC.government.level
                                    + tempC.hygiene.level
                                    + tempC.awareness.level;
                    if (cultureValue < tempCulture) {
                        city = tempC;
                        cultureValue = tempCulture;
                    }
                }
                if (city != null) {
                    return ActionPrinter.deployVaccine(vaccPath, city);
                }
            }
            return ActionPrinter.endRound();

        } else if (acutePathogens.size() == 1) {
            Pathogen medPath = ActionHelper.findSlowlyPathogen(acutePathogens);
            if (!ActionHelper.isMedicationInDevelopment(round, medPath)
                    && !ActionHelper.isMedicationAvailable(round, medPath)
                    && round.points - Action.DEVELOPMEDICATION.baseCost >= 20) {
                return ActionPrinter.developMedication(medPath);
            } else if (ActionHelper.isMedicationAvailable(round, medPath)
                    && round.points - Action.DEPLOYMEDICATION.baseCost >= 20) {
                City city = null;
                double infectedPopTemp = 0;
                double infectedPop = 0;
                double prevalence = 0;

                for (City tempC : round.cities.values()) {
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
