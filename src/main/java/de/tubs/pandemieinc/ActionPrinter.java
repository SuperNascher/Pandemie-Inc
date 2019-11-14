package de.tubs.pandemieinc;

import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;

public class ActionPrinter {

    public static String endRound() {
        return "{\"type\": \"endRound\"}";
    }

    public static String putUnderQuarantine(City city, int rounds) {
        String baseString = "{\"type\": \"putUnderQuarantine\", " +
            "\"city\": \"%s\", \"rounds\": %d}";
        return String.format(baseString, city.name, rounds);
    }

    public static String closeAirport(City city, int rounds) {
        String baseString = "{\"type\": \"closeAirport\", " +
            "\"city\": \"%s\", \"rounds\": %d}";
        return String.format(baseString, city.name, rounds);
    }

    public static String closeConnection(City fromCity, City toCity, int rounds) {
        String baseString = "{\"type\": \"closeConnection\", " +
            "\"fromCity\": \"%s\", \"toCity\": \"%s\", \"rounds\": %d}";
        return String.format(baseString, fromCity.name, toCity.name, rounds);
    }

    public static String developVaccine(Pathogen pathogen) {
        String baseString = "{\"type\": \"developVaccine\", \"pathogen\": \"%s\"}";
        return String.format(baseString, pathogen.name);
    }

    public static String deployVaccine(Pathogen pathogen, City city) {
        String baseString = "{\"type\": \"deployVaccine\", " +
            "\"pathogen\": \"%s\", \"city\": \"%s\"}";
        return String.format(baseString, pathogen.name, city.name);
    }

    public static String developMedication(Pathogen pathogen) {
        String baseString = "{\"type\": \"developMedication\", \"pathogen\": \"%s\"}";
        return String.format(baseString, pathogen.name);
    }

    public static String deployMedication(Pathogen pathogen, City city) {
        String baseString = "{\"type\": \"deployMedication\", " +
            "\"pathogen\": \"%s\", \"city\": \"%s\"}";
        return String.format(baseString, pathogen.name, city.name);
    }

    public static String exertInfluence(City city) {
        String baseString = "{\"type\": \"exertInfluence\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }

    public static String callElections(City city) {
        String baseString = "{\"type\": \"callElections\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }

    public static String applyHygienicMeasures(City city) {
        String baseString = "{\"type\": \"applyHygienicMeasures\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }

    public static String launchCampaign(City city) {
        String baseString = "{\"type\": \"launchCampaign\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }
}
