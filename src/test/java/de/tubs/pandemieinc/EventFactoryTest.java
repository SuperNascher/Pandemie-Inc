package de.tubs.pandemieinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tubs.pandemieinc.events.AirportClosedEvent;
import de.tubs.pandemieinc.events.AntiVaccinationismEvent;
import de.tubs.pandemieinc.events.BioTerrorismEvent;
import de.tubs.pandemieinc.events.CampaignLaunchedEvent;
import de.tubs.pandemieinc.events.ConnectionClosedEvent;
import de.tubs.pandemieinc.events.EconomicCrisisEvent;
import de.tubs.pandemieinc.events.ElectionsCalledEvent;
import de.tubs.pandemieinc.events.HygienicMeasuresAppliedEvent;
import de.tubs.pandemieinc.events.InfluenceExertedEvent;
import de.tubs.pandemieinc.events.LargeScalePanicEvent;
import de.tubs.pandemieinc.events.MedicationAvailableEvent;
import de.tubs.pandemieinc.events.MedicationDeployedEvent;
import de.tubs.pandemieinc.events.MedicationInDevelopmentEvent;
import de.tubs.pandemieinc.events.OtherEvent;
import de.tubs.pandemieinc.events.OutbreakEvent;
import de.tubs.pandemieinc.events.PathogenEncounteredEvent;
import de.tubs.pandemieinc.events.QuarantineEvent;
import de.tubs.pandemieinc.events.UprisingEvent;
import de.tubs.pandemieinc.events.VaccineAvailableEvent;
import de.tubs.pandemieinc.events.VaccineDeployedEvent;
import de.tubs.pandemieinc.events.VaccineInDevelopmentEvent;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

/**
 * Testing the EventFactory class.
 *
 * @see de.tubs.pandemieinc.EventFactory
 */
class EventFactoryTests {

    ObjectMapper objectMapper = new ObjectMapper();
    EventFactory eventFactory = new EventFactory(new HashMap());

    @Test
    void testParseAiportClosedEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"airportClosed\", \"sinceRound\": 3, \"untilRound\": 4}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        AirportClosedEvent event =
                (AirportClosedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, AirportClosedEvent.eventName);
        assertEquals(event.sinceRound, 3);
        assertEquals(event.untilRound, 4);
    }

    @Test
    void testAntiVaccinationismEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"antiVaccinationism\", \"sinceRound\": 5}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        AntiVaccinationismEvent event =
                (AntiVaccinationismEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, AntiVaccinationismEvent.eventName);
        assertEquals(event.sinceRound, 5);
    }

    @Test
    void testBioTerrorismEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"type\": \"bioTerrorism\", "
                        + "\"pathogen\" : {\"name\" : \"Dr. Evil\", "
                        + "\"infectivity\" : \"o\", "
                        + "\"mobility\" : \"o\", \"duration\" : \"o\", "
                        + "\"lethality\" : \"o\"},"
                        + " \"round\": 6}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        BioTerrorismEvent event = (BioTerrorismEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, BioTerrorismEvent.eventName);
        assertEquals(event.round, 6);
    }

    @Test
    void testCampaignLaunchedEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"campaignLaunched\", " + " \"round\": 7}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        CampaignLaunchedEvent event =
                (CampaignLaunchedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, CampaignLaunchedEvent.eventName);
        assertEquals(event.round, 7);
    }

    @Test
    void testConnectionClosedEvent() throws JsonProcessingException {
        City city = new City("A");
        HashMap<String, City> map = new HashMap<String, City>();
        map.put("A", city);
        this.eventFactory.cities = map;

        String eventAsJson =
                "{\"city\": \"A\", \"sinceRound\": 8, \"type\": "
                        + "\"connectionClosed\", \"untilRound\": 9}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        ConnectionClosedEvent event =
                (ConnectionClosedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, ConnectionClosedEvent.eventName);
        assertEquals(event.city, city);
        assertEquals(event.sinceRound, 8);
        assertEquals(event.untilRound, 9);
    }

    @Test
    void testEconomicCrisisEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"economicCrisis\", " + " \"sinceRound\": 10}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        EconomicCrisisEvent event =
                (EconomicCrisisEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, EconomicCrisisEvent.eventName);
        assertEquals(event.sinceRound, 10);
    }

    @Test
    void testElectionsCalledEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"electionsCalled\", " + " \"round\": 11}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        ElectionsCalledEvent event =
                (ElectionsCalledEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, ElectionsCalledEvent.eventName);
        assertEquals(event.round, 11);
    }

    @Test
    void testHygienicMeasuresAppliedEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"hygienicMeasuresApplied\", " + " \"round\": 12}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        HygienicMeasuresAppliedEvent event =
                (HygienicMeasuresAppliedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, HygienicMeasuresAppliedEvent.eventName);
        assertEquals(event.round, 12);
    }

    @Test
    void testInfluenceExertedEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"influenceExerted\", " + " \"round\": 13}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        InfluenceExertedEvent event =
                (InfluenceExertedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, InfluenceExertedEvent.eventName);
        assertEquals(event.round, 13);
    }

    @Test
    void testLargeScalePanicEvent() throws JsonProcessingException {
        String eventAsJson = "{\"type\": \"largeScalePanic\", " + " \"sinceRound\": 14}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        LargeScalePanicEvent event =
                (LargeScalePanicEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, LargeScalePanicEvent.eventName);
        assertEquals(event.sinceRound, 14);
    }

    @Test
    void testMedicationAvailableEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"sinceRound\": 15, "
                        + "\"type\": \"medicationAvailable\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        MedicationAvailableEvent event =
                (MedicationAvailableEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, MedicationAvailableEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.sinceRound, 15);
    }

    @Test
    void testMedicationDeployedEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"round\": 16, "
                        + "\"type\": \"medicationDeployed\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        MedicationDeployedEvent event =
                (MedicationDeployedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, MedicationDeployedEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.round, 16);
    }

    @Test
    void testMedicationInDevelopmentEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"sinceRound\": 17, "
                        + "\"type\": \"medicationInDevelopment\", \"untilRound\": 18}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        MedicationInDevelopmentEvent event =
                (MedicationInDevelopmentEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, MedicationInDevelopmentEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.sinceRound, 17);
        assertEquals(event.untilRound, 18);
    }

    @Test
    void testOutbreakEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, "
                        + "\"prevalence\": 0.6881594372801876, "
                        + "\"sinceRound\": 19, "
                        + "\"type\": \"outbreak\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        OutbreakEvent event = (OutbreakEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, OutbreakEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.prevalence, 0.6881594372801876);
        assertEquals(event.sinceRound, 19);
    }

    @Test
    void testPathogenEncounteredEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"round\": 20, "
                        + "\"type\": \"pathogenEncountered\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        PathogenEncounteredEvent event =
                (PathogenEncounteredEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, PathogenEncounteredEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.round, 20);
    }

    @Test
    void testQuarantineEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"sinceRound\": 21, " + "\"type\": \"quarantine\", " + "\"untilRound\": 22}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        QuarantineEvent event = (QuarantineEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, QuarantineEvent.eventName);
        assertEquals(event.sinceRound, 21);
        assertEquals(event.untilRound, 22);
    }

    @Test
    void testUprisingEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"participants\": 42, " + "\"sinceRound\": 23, " + "\"type\": \"uprising\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        UprisingEvent event = (UprisingEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, UprisingEvent.eventName);
        assertEquals(event.participants, 42);
        assertEquals(event.sinceRound, 23);
    }

    @Test
    void testVaccineAvailableEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"sinceRound\": 24, "
                        + "\"type\": \"vaccineAvailable\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        VaccineAvailableEvent event =
                (VaccineAvailableEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, VaccineAvailableEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.sinceRound, 24);
    }

    @Test
    void testVaccineDeployedEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"round\": 25, "
                        + "\"type\": \"vaccineDeployed\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        VaccineDeployedEvent event =
                (VaccineDeployedEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, VaccineDeployedEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.round, 25);
    }

    @Test
    void testVaccineInDevelopmentEvent() throws JsonProcessingException {
        String eventAsJson =
                "{\"pathogen\": {\"duration\": \"-\", \"infectivity\": \"++\", "
                        + "\"lethality\": \"++\", \"mobility\": \"+\", \"name\": "
                        + "\"Admiral Trips\"}, \"sinceRound\": 26, "
                        + "\"type\": \"vaccineInDevelopment\", \"untilRound\": 27}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse event
        VaccineInDevelopmentEvent event =
                (VaccineInDevelopmentEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, VaccineInDevelopmentEvent.eventName);
        assertEquals(event.pathogen.name, "Admiral Trips");
        assertEquals(event.sinceRound, 26);
        assertEquals(event.untilRound, 27);
    }

    @Test
    void testParseOtherEvent() throws JsonProcessingException {
        String eventAsJson = "{\"test\": \"Value\", \"test2\": 4, \"type\": \"unknown\"}";
        JsonNode rootNode = this.objectMapper.readTree(eventAsJson);

        // Parse Event
        OtherEvent event = (OtherEvent) this.eventFactory.parseFromJsonNode(rootNode);
        assertNotNull(event);
        assertEquals(event.eventName, OtherEvent.eventName);
        String testField = event.fields.get("test");
        assertNotNull(testField);
        assertEquals(testField, "Value");
        String test2Field = event.fields.get("test2");
        assertNotNull(test2Field);
        assertEquals(test2Field, "4");
    }
}
