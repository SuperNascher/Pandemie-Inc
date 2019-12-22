package de.tubs.pandemieinc;

import de.tubs.pandemieinc.events.BaseEvent;
import de.tubs.pandemieinc.events.MedicationAvailableEvent;
import de.tubs.pandemieinc.events.MedicationDeployedEvent;
import de.tubs.pandemieinc.events.MedicationInDevelopmentEvent;
import de.tubs.pandemieinc.events.OutbreakEvent;
import de.tubs.pandemieinc.events.PathogenEncounteredEvent;
import de.tubs.pandemieinc.events.QuarantineEvent;
import de.tubs.pandemieinc.events.VaccineAvailableEvent;
import de.tubs.pandemieinc.events.VaccineDeployedEvent;
import de.tubs.pandemieinc.events.VaccineInDevelopmentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
* ActionHelper class to supply
* functions for the implementations.
*/
public class ActionHelper {

    /**
    * Get all possible actions from the given round.
    *
    * @param round The given round instance
    * @return List of all Actions that are possible with the given round.
    */
    public static List<Action> getPossibleActions(Round round) {
        List<Action> possibleActions = new ArrayList<Action>();

        // Filter through non dependent actions
        for (Action action : Action.nonDependentActions()) {
            if ((action.baseCost + action.roundCost) <= round.points) {
                possibleActions.add(action);
            }
        }

        Map<String, Action> dependentActions = Action.dependentActions();
        for (BaseEvent event : round.events) {
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

    /**
    * Get the cities with only one pathogen.
    *
    * @param round The given round instance
    * @return A Map, where key is the city and value is Pathogen and
    *   the cities have only one pathogen.
    */
    public static Map<City, Pathogen> getCitiesWithOnePathogen(Round round) {
        Map<City, Pathogen> singleCityPathogens = new HashMap<City, Pathogen>();
        for (City city : round.cities.values()) {
            int pathCounter = 0;
            Pathogen pathogen = null;
            // Iterate through the events to find Pathogens in the City
            for (BaseEvent event : city.events) {
                if (event.eventName == OutbreakEvent.eventName) {
                    pathCounter = pathCounter + 1;
                    pathogen = ((OutbreakEvent) event).pathogen;
                }
            }

            // Skip, if amount of pathogens != 1
            if (pathCounter != 1) {
                continue;
            }
            singleCityPathogens.put(city, pathogen);
        }
        return singleCityPathogens;
    }

    /**
    * Find the deadliest pathogen from the given pathogens.
    *
    * @param pathogens A collection of pathogens.
    * @return The pathogen, that is the dangerous from the given collection.
    */
    public static Pathogen findDeadlyPathogen(Iterable<Pathogen> pathogens) {
        float killRating = 10;
        Pathogen deadlyPath = null;
        for (Pathogen temp : pathogens) {
            float lethality = temp.lethality.level;
            float duration = temp.duration.level;
            float infectivity = temp.infectivity.level;
            float mobility = temp.mobility.level;

            if (infectivity == -2) {
                continue;
            }
            if (killRating == 10) {
                killRating = lethality + mobility + duration / 2;
                deadlyPath = temp;

            } else {
                if (killRating < lethality + mobility + duration / 2) {
                    killRating = lethality + mobility + duration / 2;
                    deadlyPath = temp;
                }
            }
        }
        return deadlyPath;
    }

    /**
    * Find the fastes pathogen from the given pathogens.
    *
    * @param pathogens A collection of pathogens.
    * @return The pathogen, that is the fastes from the given collection.
    */
    public static Pathogen findFastlyPathogen(Iterable<Pathogen> pathogen) {
        float spreadRating = 10;
        Pathogen fastPath = null;

        for (Pathogen temp : pathogen) {
            float duration = temp.duration.level;
            float infectivity = temp.infectivity.level;

            if (infectivity == -2) {
                continue;
            }
            if (spreadRating == 10) {
                spreadRating = duration - infectivity;
                fastPath = temp;

            } else {
                if (spreadRating > duration - infectivity) {
                    spreadRating = duration - infectivity;
                    fastPath = temp;
                }
            }
        }
        return fastPath;
    }

    /**
    * Find the slowest pathogen from the given pathogens.
    *
    * @param pathogens A collection of pathogens.
    * @return The pathogen, that is the slowest from the given collection.
    */
    public static Pathogen findSlowlyPathogen(Iterable<Pathogen> pathogen) {

        float spreadRating = 10;
        Pathogen slowPath = null;

        for (Pathogen temp : pathogen) {
            float duration = temp.duration.level;
            float infectivity = temp.infectivity.level;

            if (infectivity == -2) {
                continue;
            }
            if (spreadRating == 10) {
                spreadRating = duration - infectivity;
                slowPath = temp;

            } else {
                if (spreadRating < duration - infectivity) {
                    spreadRating = duration - infectivity;
                    slowPath = temp;
                }
            }
        }
        return slowPath;
    }

    /**
    * Get all pathogens that are not in quarantine.
    *
    * @param round The given round instance
    * @return A list of pathogens, where the pathogens are not "in quarantine".
    */
    public static List<Pathogen> pathogensNotInQuarantine(Round round) {
        List<Pathogen> acutePathogens = new ArrayList<>();
        for (Pathogen tempP : round.pathogens.values()) {
            for (City city : round.cities.values()) {
                boolean acute = false;
                for (BaseEvent event : city.events) {
                    if (event.eventName == OutbreakEvent.eventName) {
                        if (((OutbreakEvent) event).pathogen == tempP) {
                            acute = true;
                        }
                    } else if (event.eventName == QuarantineEvent.eventName) {
                        acute = false;
                        break;
                    }
                }
                if (acute == true) {
                    acutePathogens.add(tempP);
                    break;
                }
            }
        }
        return acutePathogens;
    }

    /**
    * Check, if a vaccine for the given pathogen is in development.
    *
    * @param round The given round instance.
    * @param pathogen The pathogen for the vaccine.
    * @return True, if the vaccine for the given pathogen is in development, otherwise false.
    */
    public static boolean isVaccineInDevelopment(Round round, Pathogen pathogen) {
        for (BaseEvent event : round.events) {
            if (event.eventName == VaccineInDevelopmentEvent.eventName) {
                if (((VaccineInDevelopmentEvent) event).pathogen == pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Check, if a vaccine for the given pathogen is available.
    *
    * @param round The given round instance.
    * @param pathogen The pathogen for the vaccine.
    * @return True, if the vaccine for the given pathogen is available, otherwise false.
    */
    public static boolean isVaccineAvailable(Round round, Pathogen pathogen) {
        for (BaseEvent event : round.events) {
            if (event.eventName == VaccineAvailableEvent.eventName) {
                if (((VaccineAvailableEvent) event).pathogen == pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Check, if a vaccine for the given pathogen is deployed in the city.
    *
    * @param city The given city instance.
    * @param pathogen The pathogen for the vaccine.
    * @return True, if the vaccine for the given pathogen is deployed in the city,
    *   otherwise false.
    */
    public static boolean isVaccineDeployed(City city, Pathogen pathogen) {
        for (BaseEvent event : city.events) {
            if (event.eventName == VaccineDeployedEvent.eventName) {
                if (((VaccineDeployedEvent) event).pathogen == pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Check, if the given city has the pathogen (infected).
    *
    * @param city The given city instance.
    * @param pathogen The given pathogen instance.
    * @return True, if the given city is infected with the given pathogen,
    *   otherwise false.
    */
    public static boolean isCityInfectedWithPathogen(City city, Pathogen pathogen) {
        for (BaseEvent event : city.events) {
            if (event.eventName == OutbreakEvent.eventName) {
                if (pathogen == ((OutbreakEvent) event).pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Check, if a medication for the given pathogen is in development.
    *
    * @param round The given round instance.
    * @param pathogen The pathogen for the medication.
    * @return True, if the medication for the given pathogen is in development, otherwise false.
    */
    public static boolean isMedicationInDevelopment(Round round, Pathogen pathogen) {
        for (BaseEvent event : round.events) {
            if (event.eventName == MedicationInDevelopmentEvent.eventName) {
                if (((MedicationInDevelopmentEvent) event).pathogen == pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Check, if a medication for the given pathogen is available.
    *
    * @param round The given round instance.
    * @param pathogen The pathogen for the medication.
    * @return True, if the medication for the given pathogen is available, otherwise false.
    */
    public static boolean isMedicationAvailable(Round round, Pathogen pathogen) {
        for (BaseEvent event : round.events) {
            if (event.eventName == MedicationAvailableEvent.eventName) {
                if (((MedicationAvailableEvent) event).pathogen == pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Check, if a medication for the given pathogen is deployed in the city.
    *
    * @param city The given city instance.
    * @param pathogen The pathogen for the medication.
    * @return True, if the medication for the given pathogen is deployed in the city,
    *   otherwise false.
    */
    public static boolean isMedicationDeployed(City city, Pathogen pathogen) {
        for (BaseEvent event : city.events) {
            if (event.eventName == MedicationDeployedEvent.eventName) {
                if (pathogen == ((MedicationDeployedEvent) event).pathogen) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Find the highest amount of rounds that a pathogen has in the given
    * city collection.
    *
    * @param cities A given collection of cities.
    * @param pathogen The given pathogen.
    * @return The highest round counter that the given pathogen has in one of the cities
    *   in the given city collection.
    */
    public static int findHighestPathogenRound(Iterable<City> cities, Pathogen pathogen) {
        int infectionRound = 0;
        for (City tempC : cities) {
            if (isCityInfectedWithPathogen(tempC, pathogen)) {
                continue;
            }
            for (BaseEvent tempE : tempC.events) {
                if (tempE.eventName == OutbreakEvent.eventName) {
                    OutbreakEvent event = (OutbreakEvent) tempE;
                    if (event.sinceRound > infectionRound) {
                        infectionRound = event.sinceRound;
                    }
                }
            }
        }
        return infectionRound;
    }

    /**
    * Find the highest amount of rounds that a pathogen has in the given
    * city collection.
    *
    * @param cities A given collection of cities.
    * @param pathogen The given pathogen.
    * @param cityPredicate Filter function to restrict cities (e.g. VaccineEvents, MedicationEvents).
    *   The city will be skipped, if the cityPredicate returns true.
    * @return The highest round counter that the given pathogen has in one of the cities
    *   and the cityPredicate returned false for that, in the given city collection.
    */
    public static int findHighestPathogenRound(Iterable<City> cities, Pathogen pathogen, BiPredicate<City, Pathogen> cityPredicate) {
        int infectionRound = 0;
        for (City tempC : cities) {
            if (isCityInfectedWithPathogen(tempC, pathogen) || cityPredicate.test(tempC, pathogen)) {
                continue;
            }
            for (BaseEvent tempE : tempC.events) {
                if (tempE.eventName == OutbreakEvent.eventName) {
                    OutbreakEvent event = (OutbreakEvent) tempE;
                    if (event.sinceRound > infectionRound) {
                        infectionRound = event.sinceRound;
                    }
                }
            }
        }
        return infectionRound;
    }

    /**
    * Find the cities, where the round of the OutbreakEvent is the same as the given round.
    *
    * @param round The round counter for the pathogen
    * @param cities A given collection of cities.
    * @return A list of cities, where the round of the OutbreakEvent is the same
    *   as the given round.
    */
    public static List<City> getCitiesWithGivenOutbreakRound(int round, Iterable<City> cities) {
        List<City> newCities = new ArrayList<>();
        for (City tempC : cities) {
            for (BaseEvent event : tempC.events) {
                if (event.eventName == OutbreakEvent.eventName) {
                    if (((OutbreakEvent) event).sinceRound == round) {
                        newCities.add(tempC);
                        break;
                    }
                }
            }
        }
        return newCities;
    }
}
