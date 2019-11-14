package de.tubs.pandemieinc;

import de.tubs.pandemieinc.Strength;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

public class Pathogen {
    public final String name;
    public final Strength infectivity;
    public final Strength mobility;
    public final Strength duration;
    public final Strength lethality;

    Pathogen(String name, Strength infectivity, Strength mobility, Strength duration, Strength lethality) {
        this.name = name;
        this.infectivity = infectivity;
        this.mobility = mobility;
        this.duration = duration;
        this.lethality = lethality;
    }

    Pathogen(String name, String infectivity, String mobility, String duration, String lethality) {
        this.name = name;
        this.infectivity = Strength.fromString(infectivity);
        this.mobility = Strength.fromString(mobility);
        this.duration = Strength.fromString(duration);
        this.lethality = Strength.fromString(lethality);
    }

    public static Pathogen fromJsonNode(JsonNode node) {
        String[] attributes = {"name", "infectivity", "mobility", "duration", "lethality"};
        String[] values = new String[5];

        // Try to parse the Pathogen attributes
        for (int i = 0; i < 5; i++) {
            JsonNode value = node.get(attributes[i]);
            if (value == null || !value.isTextual()) {
                return null;
            }
            values[i] = value.textValue();
        }

        // Return the Pathogen
        return new Pathogen(values[0], values[1], values[2], values[3], values[4]);
    }

    @Override
    public String toString() {
        return String.format("Pathogen (%s)", this.name);
    }
}
