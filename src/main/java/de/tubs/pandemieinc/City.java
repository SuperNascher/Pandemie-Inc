package de.tubs.pandemieinc;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include; 


import de.tubs.pandemieinc.Strength;
import de.tubs.pandemieinc.events.BaseEvent;

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

    public City() {
    }


    // Overwrite serialization for connections
    @JsonGetter("connections")
    public String[] getConnectionsForJson() {
        return this.connections.values().stream()
            .map(city -> city.name)
            .toArray(String[]::new);
    }
    /*
    public List<BaseEvent> getEvents() {
        return this.events;
    }
    */
}
