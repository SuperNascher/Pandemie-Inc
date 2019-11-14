package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.Round;
import de.tubs.pandemieinc.ActionHelper;
import de.tubs.pandemieinc.Action;
import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Supplier;

public class BogoImplementation {

    public Round round;
    public Map<Action, Supplier<String>> actionMapping;
    private Random random;

    public BogoImplementation(Round round) {
        this.round = round;
        this.random = new Random();

        this.actionMapping = new HashMap<Action, Supplier<String>>();
        this.actionMapping.put(Action.ENDROUND, ActionPrinter::endRound);
        this.actionMapping.put(Action.PUTUNDERQUARANTINE, this::putUnderQuarantine);
        this.actionMapping.put(Action.CLOSEAIRPORT, this::closeAirport);
        this.actionMapping.put(Action.CLOSECONNECTION, this::closeConnection);
        this.actionMapping.put(Action.DEVELOPVACCINE, this::developVaccine);
        this.actionMapping.put(Action.DEPLOYVACCINE, this::deployVaccine);
        this.actionMapping.put(Action.DEVELOPMEDICATION, this::developMedication);
        this.actionMapping.put(Action.DEPLOYMEDICATION, this::deployMedication);
        this.actionMapping.put(Action.EXERTINFLUENCE, this::exertInfluence);
        this.actionMapping.put(Action.CALLELECTIONS, this::callElections);
        this.actionMapping.put(Action.APPLYHYGIENICMEASURES, this::applyHygienicMeasures);
        this.actionMapping.put(Action.LAUNCHCAMPAIGN, this::launchCampaign);
    }

    public String selectAction() {
        // Get the possible actions and pick that randomly
        List<Action> actions = ActionHelper.getPossibleActions(this.round);
        while (!actions.isEmpty()) {
            int actionNumber = this.random.nextInt(actions.size());
            Action action = actions.get(actionNumber);

            // Get the function from the actionMapping and error handling
            Supplier<String> fun = this.actionMapping.get(action);
            if (fun == null) {
                return ActionPrinter.endRound();
            }
            String actionResponse = fun.get();
            if (actionResponse != null) {
                return actionResponse;
            }
            actions.remove(action);
        }

        // Fallback to endRound, "should never happen"
        return ActionPrinter.endRound();
    }

    public String putUnderQuarantine() {
        int maxRound = (this.round.points - Action.PUTUNDERQUARANTINE.baseCost) /
            Action.PUTUNDERQUARANTINE.roundCost;
        int rounds;
        if (maxRound > 1) {
            rounds = random.nextInt((maxRound - 1) + 1) + 1;
        } else {
            rounds = 1;
        }

        City city = this.findCity(QuarantineEvent.eventName);
        if (city == null) {
            return null;
        }
        return ActionPrinter.putUnderQuarantine(city, rounds);
    }

    public String closeAirport() {
        int maxRound = (this.round.points - Action.CLOSEAIRPORT.baseCost) /
            Action.CLOSEAIRPORT.roundCost;
        int rounds;
        if (maxRound > 1) {
            rounds = random.nextInt((maxRound - 1) + 1) + 1;
        } else {
            rounds = 1;
        }

        City city = this.findCity(AirportClosedEvent.eventName);
        if (city == null) {
            return null;
        }
        return ActionPrinter.closeAirport(city, rounds);
    }

    public String closeConnection() {
        int maxRound = (this.round.points - Action.CLOSEAIRPORT.baseCost) /
            Action.CLOSEAIRPORT.roundCost;
        int rounds;
        if (maxRound > 1) {
            rounds = random.nextInt((maxRound - 1) + 1) + 1;
        } else {
            rounds = 1;
        }

        List<City> cities = this.round.cities.values().stream()
            .collect(Collectors.toList());
        while (!cities.isEmpty()) {
            // Get a random city for fromCity
            City fromCity = cities.remove(this.random.nextInt(cities.size()));

            // Get the connections of a city as List
            List<City> cityConnections = fromCity.connections.values().stream()
                .collect(Collectors.toList());

            // Remove cities from the connection that are already closed
            for (BaseEvent cityEvent : fromCity.events) {
                if (cityEvent.eventName.equals(ConnectionClosedEvent.eventName)) {
                    cityConnections.remove(((ConnectionClosedEvent) cityEvent).city);
                }
            }

            // Try to get toCity attribute for closeConnection action
            if (!cityConnections.isEmpty()) {
                City toCity = cityConnections
                    .get(this.random.nextInt(cityConnections.size()));
                return ActionPrinter.closeConnection(fromCity, toCity, rounds);
            }
        }
        return null;
    }

    public String developVaccine() {
        List<Pathogen> pathogens = this.round.pathogens.values().stream()
            .collect(Collectors.toList());

        for (BaseEvent globalEvent : this.round.events) {
            if (globalEvent.eventName.equals(VaccineAvailableEvent.eventName)) {
                pathogens.remove(((VaccineAvailableEvent) globalEvent).pathogen);
            }
        }

        if (!pathogens.isEmpty()) {
            return ActionPrinter.developVaccine(pathogens.get(this.random.nextInt(pathogens.size())));
        }
        return null;
    }

    public String developMedication() {
        List<Pathogen> pathogens = this.round.pathogens.values().stream()
            .collect(Collectors.toList());

        for (BaseEvent globalEvent : this.round.events) {
            if (globalEvent.eventName.equals(MedicationInDevelopmentEvent.eventName)) {
                pathogens.remove(((MedicationInDevelopmentEvent) globalEvent).pathogen);
            }
        }

        if (!pathogens.isEmpty()) {
            return ActionPrinter.developMedication(pathogens.get(this.random.nextInt(pathogens.size())));
        }
        return null;
    }

    public String deployVaccine() {
        List<Pathogen> pathogens = new ArrayList<Pathogen>();
        for (BaseEvent globalEvent : this.round.events) {
            if (MedicationAvailableEvent.eventName.equals(globalEvent.eventName)) {
                pathogens.add(((MedicationAvailableEvent) globalEvent).pathogen);
            }
        }

        while (!pathogens.isEmpty()) {
            Pathogen pathogen = pathogens.remove(this.random.nextInt(pathogens.size()));

            // Get all cities
            List<City> cities = this.round.cities.values().stream()
                .collect(Collectors.toList());
            while (!cities.isEmpty()) {
                // Get random city
                City city = cities.remove(this.random.nextInt(cities.size()));
                for (BaseEvent cityEvent : city.events) {
                    boolean isVaccinedEvent = VaccineDeployedEvent.eventName.equals(cityEvent.eventName);
                    if (isVaccinedEvent &&
                        ((VaccineDeployedEvent) cityEvent).pathogen == pathogen) {
                        continue;
                    }
                }

                return ActionPrinter.deployVaccine(pathogen, city);
            }
        }
        return null;
    }

    public String deployMedication() {
        List<Pathogen> pathogens = new ArrayList<Pathogen>();
        for (BaseEvent globalEvent : this.round.events) {
            if (MedicationAvailableEvent.eventName.equals(globalEvent.eventName)) {
                pathogens.add(((MedicationAvailableEvent) globalEvent).pathogen);
            }
        }

        while (!pathogens.isEmpty()) {
            Pathogen pathogen = pathogens.remove(this.random.nextInt(pathogens.size()));

            // Get all cities
            List<City> cities = this.round.cities.values().stream()
                .collect(Collectors.toList());
            while (!cities.isEmpty()) {
                // Get random city
                City city = cities.remove(this.random.nextInt(cities.size()));
                for (BaseEvent cityEvent : city.events) {
                    boolean isMedicatedEvent = MedicationDeployedEvent.eventName.equals(cityEvent.eventName);
                    if (isMedicatedEvent &&
                        ((MedicationDeployedEvent) cityEvent).pathogen == pathogen) {
                        continue;
                    }
                }
                return ActionPrinter.deployMedication(pathogen, city);
            }
        }
        return null;
    }

    public String exertInfluence() {
        City city = findCity();
        return ActionPrinter.exertInfluence(city);
    }

    public String callElections() {
        City city = findCity();
        return ActionPrinter.callElections(city);
    }

    public String applyHygienicMeasures() {
        City city = findCity();
        return ActionPrinter.applyHygienicMeasures(city);
    }

    public String launchCampaign() {
        City city = findCity();
        return ActionPrinter.launchCampaign(city);
    }

    public City findCity() {
        Object[] cities = this.round.cities.values().toArray();
        return (City) cities[this.random.nextInt(cities.length)];
    }

    public City findCity(String eventName) {
        List<City> cities = this.round.cities.values().stream()
            .collect(Collectors.toList());
        while (!cities.isEmpty()) {
            // Get random city
            City city = cities.remove(this.random.nextInt(cities.size()));
            // Check, if the event type is given, if not return city
            for (BaseEvent cityEvent : city.events) {
                if (eventName.equals(cityEvent.eventName)) {
                    continue;
                }
            }
            return city;
        }
        return null;
    }
}
