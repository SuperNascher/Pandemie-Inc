package de.tubs.pandemieinc;

/** Helper for the Implementation to return the actions as JSON String. */
public class ActionPrinter {

    /**
     * Format the action endRound as JSON action object.
     *
     * @return The endRound as JSON action object.
     */
    public static String endRound() {
        return "{\"type\": \"endRound\"}";
    }

    /**
     * Format the action putUnderQuarantine as JSON action object.
     *
     * @param city A city that should be in quarantine
     * @param rounds the amount of rounds, that the city should be in quarantine.
     * @return The putUnderQuarantine as JSON action object.
     */
    public static String putUnderQuarantine(City city, int rounds) {
        String baseString =
                "{\"type\": \"putUnderQuarantine\", " + "\"city\": \"%s\", \"rounds\": %d}";
        return String.format(baseString, city.name, rounds);
    }

    /**
     * Format the action closeAirport as JSON action object.
     *
     * @param city A city that should be in quarantine
     * @param rounds the amount of rounds for the closed airport of the city.
     * @return The closeAirport as JSON action object.
     */
    public static String closeAirport(City city, int rounds) {
        String baseString = "{\"type\": \"closeAirport\", " + "\"city\": \"%s\", \"rounds\": %d}";
        return String.format(baseString, city.name, rounds);
    }

    /**
     * Format the action closeConnection as JSON action object.
     *
     * @param fromCity The "start" city for the connection.
     * @param toCity The targeted city for the connection.
     * @param rounds the amount of rounds for the closed connection.
     * @return The closeConnection as JSON action object.
     */
    public static String closeConnection(City fromCity, City toCity, int rounds) {
        String baseString =
                "{\"type\": \"closeConnection\", "
                        + "\"fromCity\": \"%s\", \"toCity\": \"%s\", \"rounds\": %d}";
        return String.format(baseString, fromCity.name, toCity.name, rounds);
    }

    /**
     * Format the action developVaccine as JSON action object.
     *
     * @param pathogen The target pathogen for the vaccine.
     * @return The developVaccine as JSON action object.
     */
    public static String developVaccine(Pathogen pathogen) {
        String baseString = "{\"type\": \"developVaccine\", \"pathogen\": \"%s\"}";
        return String.format(baseString, pathogen.name);
    }

    /**
     * Format the action deployVaccine as JSON action object.
     *
     * @param pathogen The pathogen, where the vaccine is available.
     * @param city The city, where the vaccine should be applied.
     * @return The deployVaccine as JSON action object.
     */
    public static String deployVaccine(Pathogen pathogen, City city) {
        String baseString =
                "{\"type\": \"deployVaccine\", " + "\"pathogen\": \"%s\", \"city\": \"%s\"}";
        return String.format(baseString, pathogen.name, city.name);
    }

    /**
     * Format the action developMedication as JSON action object.
     *
     * @param pathogen The target pathogen for the medication.
     * @return The developMedication as JSON action object.
     */
    public static String developMedication(Pathogen pathogen) {
        String baseString = "{\"type\": \"developMedication\", \"pathogen\": \"%s\"}";
        return String.format(baseString, pathogen.name);
    }

    /**
     * Format the action deployMedication as JSON action object.
     *
     * @param pathogen The pathogen, where the medication is available.
     * @param city The city, where the medication should be applied.
     * @return The deployMedication as JSON action object.
     */
    public static String deployMedication(Pathogen pathogen, City city) {
        String baseString =
                "{\"type\": \"deployMedication\", " + "\"pathogen\": \"%s\", \"city\": \"%s\"}";
        return String.format(baseString, pathogen.name, city.name);
    }

    /**
     * Format the action exertInfluence as JSON action object.
     *
     * @param city The city, where the "exertInfluence" should be applied.
     * @return The exertInfluence as JSON action object.
     */
    public static String exertInfluence(City city) {
        String baseString = "{\"type\": \"exertInfluence\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }

    /**
     * Format the action callElections as JSON action object.
     *
     * @param city The city, where the "callElections" should be applied.
     * @return The callElections as JSON action object.
     */
    public static String callElections(City city) {
        String baseString = "{\"type\": \"callElections\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }

    /**
     * Format the action applyHygienicMeasures as JSON action object.
     *
     * @param city The city, where the "applyHygienicMeasures" should be applied.
     * @return The applyHygienicMeasures as JSON action object.
     */
    public static String applyHygienicMeasures(City city) {
        String baseString = "{\"type\": \"applyHygienicMeasures\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }

    /**
     * Format the action launchCampaign as JSON action object.
     *
     * @param city The city, where the "launchCampaign" should be applied.
     * @return The launchCampaign as JSON action object.
     */
    public static String launchCampaign(City city) {
        String baseString = "{\"type\": \"launchCampaign\", \"city\": \"%s\"}";
        return String.format(baseString, city.name);
    }
}
