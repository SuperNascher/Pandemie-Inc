package de.tubs.pandemieinc;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import de.tubs.pandemieinc.events.BaseEvent;
import java.util.List;
import java.util.Map;

/** The city class to represent the city information from the given "JSON round". */
public class City {
    // Fixed attributes of a city
    public String name;
    public double latitude;
    public double longitude;
    public Map<String, City> connections;

    // Variable attributes of a city (changes during "rounds")
    public long population;
    public Strength economy;
    public Strength government;
    public Strength hygiene;
    public Strength awareness;

    @JsonInclude(Include.NON_EMPTY)
    public List<BaseEvent> events;

    public City(String name) {
        this.name = name;
    }

    public City() {}

    /**
     * Return the connections to the other cities as String array for the city serialization.
     *
     * @return The connections of the given City as String array.
     */
    @JsonGetter("connections")
    public String[] getConnectionsForJson() {
        return this.connections.values().stream().map(city -> city.name).toArray(String[]::new);
    }
}
