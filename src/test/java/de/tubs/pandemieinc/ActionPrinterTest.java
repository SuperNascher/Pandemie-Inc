package de.tubs.pandemieinc;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * Testing the ActionPrinter class. (Returns the given action as JSON String for the client) We use
 * jackson to "parse" our JSON String as JSON Node to be independent of the String formatting.
 *
 * @see de.tubs.pandemieinc.ActionPrinter
 */
class ActionPrinterTests {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testEndRound() throws JsonProcessingException {
        String expectedType = "endRound";
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.endRound());
        assertEquals(expectedType, rootNode.path("type").textValue());
    }

    @Test
    void testPutUnderQuarantine() throws JsonProcessingException {
        String expectedType = "putUnderQuarantine";
        City testCity = new City("Test");
        int rounds = 2;

        // Parse the response
        JsonNode rootNode =
                this.objectMapper.readTree(ActionPrinter.putUnderQuarantine(testCity, rounds));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(testCity.name, rootNode.path("city").textValue());
        assertEquals(rounds, rootNode.path("rounds").asInt());
    }

    @Test
    void testCloseAirport() throws JsonProcessingException {
        String expectedType = "closeAirport";
        City testCity = new City("Test");
        int rounds = 3;

        // Parse the response
        JsonNode rootNode =
                this.objectMapper.readTree(ActionPrinter.closeAirport(testCity, rounds));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(testCity.name, rootNode.path("city").textValue());
        assertEquals(rounds, rootNode.path("rounds").asInt());
    }

    @Test
    void testCloseConnection() throws JsonProcessingException {
        String expectedType = "closeConnection";
        City fromCity = new City("A");
        City toCity = new City("B");
        int rounds = 4;

        // Parse the response
        JsonNode rootNode =
                this.objectMapper.readTree(ActionPrinter.closeConnection(fromCity, toCity, rounds));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(fromCity.name, rootNode.path("fromCity").textValue());
        assertEquals(toCity.name, rootNode.path("toCity").textValue());
        assertEquals(rounds, rootNode.path("rounds").asInt());
    }

    @Test
    void testDevelopVaccine() throws JsonProcessingException {
        String expectedType = "developVaccine";
        Pathogen pathogen =
                new Pathogen(
                        "Dr. Evil",
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE);

        // Parse the response
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.developVaccine(pathogen));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(pathogen.name, rootNode.path("pathogen").textValue());
    }

    @Test
    void testDeployVaccine() throws JsonProcessingException {
        String expectedType = "deployVaccine";
        City city = new City("Test");
        Pathogen pathogen =
                new Pathogen(
                        "Dr. Evil",
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE);

        // Parse the response
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.deployVaccine(pathogen, city));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(pathogen.name, rootNode.path("pathogen").textValue());
        assertEquals(city.name, rootNode.path("city").textValue());
    }

    @Test
    void testDevelopMedication() throws JsonProcessingException {
        String expectedType = "developMedication";
        Pathogen pathogen =
                new Pathogen(
                        "Dr. Evil",
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE);

        // Parse the response
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.developMedication(pathogen));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(pathogen.name, rootNode.path("pathogen").textValue());
    }

    @Test
    void testDeployMedication() throws JsonProcessingException {
        String expectedType = "deployMedication";
        City city = new City("Test");
        Pathogen pathogen =
                new Pathogen(
                        "Dr. Evil",
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE,
                        Strength.MIDDLE);

        // Parse the response
        JsonNode rootNode =
                this.objectMapper.readTree(ActionPrinter.deployMedication(pathogen, city));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(pathogen.name, rootNode.path("pathogen").textValue());
        assertEquals(city.name, rootNode.path("city").textValue());
    }

    @Test
    void testExertInfluence() throws JsonProcessingException {
        String expectedType = "exertInfluence";
        City testCity = new City("Test");

        // Parse the response
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.exertInfluence(testCity));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(testCity.name, rootNode.path("city").textValue());
    }

    @Test
    void testCallElections() throws JsonProcessingException {
        String expectedType = "callElections";
        City testCity = new City("Test");

        // Parse the response
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.callElections(testCity));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(testCity.name, rootNode.path("city").textValue());
    }

    @Test
    void testApplyHygienicMeasures() throws JsonProcessingException {
        String expectedType = "applyHygienicMeasures";
        City testCity = new City("Test");

        // Parse the response
        JsonNode rootNode =
                this.objectMapper.readTree(ActionPrinter.applyHygienicMeasures(testCity));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(testCity.name, rootNode.path("city").textValue());
    }

    @Test
    void testLaunchCampaign() throws JsonProcessingException {
        String expectedType = "launchCampaign";
        City testCity = new City("Test");

        // Parse the response
        JsonNode rootNode = this.objectMapper.readTree(ActionPrinter.launchCampaign(testCity));

        // Check the output JSON
        assertEquals(expectedType, rootNode.path("type").textValue());
        assertEquals(testCity.name, rootNode.path("city").textValue());
    }
}
