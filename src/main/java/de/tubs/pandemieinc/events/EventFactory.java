package de.tubs.pandemieinc.events;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.events.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Factory class to create the event classes from the JSON (JsonNode).
 * The handling of parsing the events can be modificated through the
 * "parseMapping" variable.
 */
public class EventFactory {

	// Pathogen name, Pathogen class (to avoid creating unnessary Pathogen classes)
    public Map<String, Pathogen> pathogens;
    public Map<String, City> cities;
    public Map<String, Function<JsonNode, BaseEvent>> parserMapping;

    /**
     * Constructor for EventFactory with given city map.
     * @param cities City map, where the key is the String name of the city
     */
    public EventFactory(Map<String, City> cities) {
        this.cities = cities;
        this.pathogens = new HashMap<String, Pathogen>();
        this.parserMapping = new HashMap<String, Function<JsonNode, BaseEvent>>();

        // AntiVaccinationismEvent
        this.parserMapping
            .put(AntiVaccinationismEvent.eventName,
                 node -> this.parseSimpleEvent(node, "sinceRound",
                                               AntiVaccinationismEvent::new));

        // AirportClosedEvent
        this.parserMapping
            .put(AirportClosedEvent.eventName,
                 node -> this.parseTimedEvent(node, AirportClosedEvent::new));

        // BioTerrorismEvent
        this.parserMapping
            .put(BioTerrorismEvent.eventName,
                 node -> parsePathogenEvent(node, "round",
                                            BioTerrorismEvent::new));

        // CampaignLaunchedEvent
        this.parserMapping
            .put(CampaignLaunchedEvent.eventName,
                 node -> parseSimpleEvent(node, "round",
                                          CampaignLaunchedEvent::new));

        // ConnectionClosedEvent
        this.parserMapping.put(ConnectionClosedEvent.eventName,
                               this::parseConnectionClosedEvent);

        // EcnomonCrisisEvent
        this.parserMapping
            .put(EconomicCrisisEvent.eventName,
                 node -> this.parseSimpleEvent(node, "sinceRound",
                                               EconomicCrisisEvent::new));

        // ElectionsCalledEvent
        this.parserMapping
            .put(ElectionsCalledEvent.eventName,
                 node -> this.parseSimpleEvent(node, "round",
                                               ElectionsCalledEvent::new));

        // HygienicMeasuresAppliedEvent
        this.parserMapping
            .put(HygienicMeasuresAppliedEvent.eventName,
                 node ->
                 this.parseSimpleEvent(node, "round",
                                       HygienicMeasuresAppliedEvent::new));

        // InfluenceExertedEvent
        this.parserMapping
            .put(InfluenceExertedEvent.eventName,
                 node -> this.parseSimpleEvent(node, "round",
                                               InfluenceExertedEvent::new));

        // LargeScalePanicEvent
        this.parserMapping
            .put(LargeScalePanicEvent.eventName,
                 node -> this.parseSimpleEvent(node, "sinceRound",
                                               LargeScalePanicEvent::new));

        // MedicationAvailableEvent
        this.parserMapping
            .put(MedicationAvailableEvent.eventName,
                 node ->
                 this.parsePathogenEvent(node, "sinceRound",
                                         MedicationAvailableEvent::new));

        // MedicationDeployedEvent
        this.parserMapping
            .put(MedicationDeployedEvent.eventName,
                 node -> this.parsePathogenEvent(node, "round",
                                                 MedicationDeployedEvent::new));

        // MedicationInDevelopmentEvent
        this.parserMapping
            .put(MedicationInDevelopmentEvent.eventName,
                 node ->
                 this.parseInDevelopmentEvent(node,
                                              MedicationInDevelopmentEvent::new)
                 );

        // OutbreakEvent
        this.parserMapping.put(OutbreakEvent.eventName,
                               this::parseOutbreakEvent);

        // PathogenEncounteredEvent
        this.parserMapping
            .put(PathogenEncounteredEvent.eventName,
                 node -> this.parsePathogenEvent(node, "round",
                                                 PathogenEncounteredEvent::new)
                 );

        // QuarantineEvent
        this.parserMapping
            .put(QuarantineEvent.eventName,
                 node -> this.parseTimedEvent(node, QuarantineEvent::new));

        // UprisingEvent
        this.parserMapping.put(UprisingEvent.eventName,
                               this::parseUprisingEvent);

        // VaccineAvailableEvent
        this.parserMapping
            .put(VaccineAvailableEvent.eventName,
                 node -> this.parsePathogenEvent(node, "sinceRound",
                                                 VaccineAvailableEvent::new));

        // VaccineDeployedEvent
        this.parserMapping
            .put(VaccineDeployedEvent.eventName,
                 node -> this.parsePathogenEvent(node, "round",
                                                 VaccineDeployedEvent::new));

        // VaccineInDevelopmentEvent
        this.parserMapping
            .put(VaccineInDevelopmentEvent.eventName,
                 node ->
                 this.parseInDevelopmentEvent(node,
                                              VaccineInDevelopmentEvent::new));
    }

    /**
     * Parse the event from a JsonNode object.
     * @param node A event JsonNode object
     * @return The event object or null, if an error occurred
     */
    public BaseEvent parseFromJsonNode(JsonNode node) {
        // Parse the event type
        JsonNode eventType = node.get("type");
        if (eventType == null) {
            return null;
        }

        // Try to parse the event from the know ones
        String type = eventType.textValue();
        Function<JsonNode, BaseEvent> parseFun = this.parserMapping.get(type);
        if (parseFun != null) {
            return parseFun.apply(node);
        }

        return this.parseOtherEvent(node);
    }

    /**
     * Parse an OtherEvent object from a given JsonNode.
     * @param node A event JsonNode object, where type
     */
    public OtherEvent parseOtherEvent(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fields = mapper.convertValue(node, new TypeReference<HashMap<String, Object>>(){});
        try {
            String debug = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fields);
            System.out.println(debug);
        } catch (Exception e) {
            System.out.println("Nope");
            System.out.println(e);
        }
        return new OtherEvent(fields);
    }

    public BaseEvent parseSimpleEvent(JsonNode node, String roundAttribute,
                                      Supplier<SimpleEvent> constructor) {
        // Parse the round attribute from the JSON node
        JsonNode value = node.path(roundAttribute);
        if (!value.isInt()) {
            return null;
        }

        // Create the event and set the round
        SimpleEvent event = constructor.get();
        event.setRound(value.asInt());
        return event;
	}

    public BaseEvent parsePathogenEvent(JsonNode node, String roundAttribute,
                                        Supplier<PathogenEvent> constructor) {
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }

        // Try to get the Pathogen instance or generate them
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }

        // Parse round
        value = node.path(roundAttribute);
        if (!value.isInt()) {
            return null;
        }

        // Create event
        PathogenEvent event = constructor.get();
        event.pathogen = pathogen;
        event.setRound(value.asInt());
        return event;
    }

    public BaseEvent parseTimedEvent(JsonNode node,
                                     Supplier<TimedEvent> constructor) {
        // Parse sinceRound
        JsonNode value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        TimedEvent event = constructor.get();
        event.untilRound = untilRound;
        event.sinceRound = sinceRound;
        return event;
    }

    public BaseEvent parseInDevelopmentEvent
        (JsonNode node, Supplier<InDevelopmentEvent> constructor) {
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to get the Pathogen instance or generate them
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }

        // Parse sinceRound
        value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        InDevelopmentEvent event = (InDevelopmentEvent) constructor.get();
        event.pathogen = pathogen;
        event.untilRound = untilRound;
        event.sinceRound = sinceRound;
        return event;
    }

    public BaseEvent parseConnectionClosedEvent(JsonNode node) {
        JsonNode value = node.get("city");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to get the City instance
        City city = this.cities.get(value.textValue());
        if (city == null) {
            return null;
        }

        // Parse sinceRound
        value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("untilRound");
        if (!value.isInt()) {
            return null;
        }
        int untilRound = value.asInt();

        ConnectionClosedEvent event = new ConnectionClosedEvent();
        event.city = city;
        event.untilRound = untilRound;
        event.sinceRound = sinceRound;
        return event;
    }

    public BaseEvent parseUprisingEvent(JsonNode node) {
        // Parse sinceRound
        JsonNode value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();

        // Parse untilRound
        value = node.path("participants");
        if (!value.isInt()) {
            return null;
        }
        int participants = value.asInt();

        UprisingEvent event = new UprisingEvent(sinceRound, participants);
        return event;
    }

    public OutbreakEvent parseOutbreakEvent(JsonNode node) {
        // Parse the Pathogen
        JsonNode value = node.path("pathogen").path("name");
        if (value.isMissingNode()) {
            return null;
        }
        // Try to generate the Pathogen instance
        Pathogen pathogen = this.pathogens.get(value.textValue());
        if (pathogen == null) {
            pathogen = Pathogen.fromJsonNode(node.get("pathogen"));
            if (pathogen == null) {
                return null;
            }
            this.pathogens.put(pathogen.name, pathogen);
        }
        // Parse sinceRound
        value = node.path("sinceRound");
        if (!value.isInt()) {
            return null;
        }
        int sinceRound = value.asInt();
        // Parse prevalence
        value = node.path("prevalence");
        if (!value.isDouble()) {
            return null;
        }
        double prevalence = value.asDouble();
        return new OutbreakEvent(pathogen, prevalence, sinceRound);
    }
}
