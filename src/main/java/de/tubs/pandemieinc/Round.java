package de.tubs.pandemieinc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.tubs.pandemieinc.events.BaseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Pathogen class to represent the "JSON round" structure.
*/
@JsonDeserialize(using = RoundDeserializer.class)
public class Round {

    // Fields that are required
    public int round;
    public String outcome;
    public int points;
    public Map<String, City> cities;
    public List<BaseEvent> events;
    public String error = "";

    // Optional fields
    @JsonIgnore public Map<String, Pathogen> pathogens;
}

/**
* Deserializer class for Round.
*/
class RoundDeserializer extends JsonDeserializer<Round> {

    /**
    * Custom parser for round class.
    *
    * @param jsonParser The given parser instance from jackson with the
    *   parsed JSON (round object).
    * @param deserializationContext The context with addional settings or reuseable objects.
    * @return The parsed round instance.
    */
    @Override
    public Round deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode tree = jsonParser.getCodec().readTree(jsonParser);
        Map<String, JsonNode> cityLocations = new HashMap<String, JsonNode>();
        Map<String, JsonNode> cityEvents = new HashMap<String, JsonNode>();

        Round round = new Round();
        round.round = tree.get("round").intValue();
        round.outcome = tree.get("outcome").textValue();
        round.points = tree.get("points").intValue();

        // Parse the cities
        round.cities = new HashMap<String, City>();
        EventFactory eventFactory = new EventFactory(round.cities);

        for (JsonNode node : tree.get("cities")) {
            City city = new City();
            city.name = node.get("name").textValue();
            city.latitude = node.get("latitude").doubleValue();
            city.longitude = node.get("longitude").doubleValue();
            city.population = node.get("population").longValue();
            // Strength attributes
            try {
                city.economy = Strength.fromString(node.get("economy").textValue());
                city.government = Strength.fromString(node.get("government").textValue());
                city.hygiene = Strength.fromString(node.get("hygiene").textValue());
                city.awareness = Strength.fromString(node.get("awareness").textValue());
            } catch (IllegalArgumentException e) {
                throw new JsonParseException(jsonParser, e.getMessage());
            }

            // Add city to the map
            round.cities.put(city.name, city);

            // Prepare city events parsing
            city.events = new ArrayList<BaseEvent>();
            JsonNode eventsNode = node.get("events");
            if (eventsNode != null) {
                cityEvents.put(city.name, eventsNode);
            }
            city.connections = new HashMap<String, City>();
            JsonNode connections = node.path("connections");
            cityLocations.put(city.name, connections);
        }

        // Parse the city events
        for (Map.Entry<String, JsonNode> cityEventsEntry : cityEvents.entrySet()) {
            City city = round.cities.get(cityEventsEntry.getKey());
            for (JsonNode eventNode : cityEventsEntry.getValue()) {
                BaseEvent event = eventFactory.parseFromJsonNode(eventNode);
                if (event == null) {
                    throw new JsonParseException(jsonParser,
                        "Could not parse given event object.");
                }
                city.events.add(event);
            }
        }

        // Apply the city connections
        for (Map.Entry<String, JsonNode> cityLocation : cityLocations.entrySet()) {
            City city = round.cities.get(cityLocation.getKey());
            for (JsonNode cityNode : cityLocation.getValue()) {
                City targetCity = round.cities.get(cityNode.textValue());
                if (targetCity != null) {
                    city.connections.put(targetCity.name, targetCity);
                }
            }
        }

        // Events parsing
        round.events = new ArrayList<BaseEvent>();
        for (JsonNode eventNode : tree.path("events")) {
            BaseEvent event = eventFactory.parseFromJsonNode(eventNode);
                if (event == null) {
                    throw new JsonParseException(jsonParser,
                        "Could not parse given city event object.");
                }
            round.events.add(event);
        }

        // "Errors"
        JsonNode errorNode = tree.get("error");
        if (errorNode != null) {
            round.error = errorNode.asText();
        }

        // Put the pathogens in the round for implementations
        // that work directly with given pathogens
        round.pathogens = eventFactory.pathogens;
        return round;
    }
}
