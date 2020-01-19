package de.tubs.pandemieinc;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/** Pathogen class to represent a Pathogen from the given "JSON round". */
public class Pathogen implements Comparable<Pathogen> {
    public final String name;
    public final Strength infectivity;
    public final Strength lethality;
    public final Strength duration;
    public final Strength mobility;

    Pathogen(
            String name,
            Strength infectivity,
            Strength mobility,
            Strength duration,
            Strength lethality) {
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

    /**
     * Parse a Pathogen from the given JsonNode.
     *
     * @param node The JsonNode that contains the "parsed" pathogen.
     * @return The parsed Pathogen instance or null on errors.
     */
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

    /** Modify toString() method to return the name of the given Pathogen. */
    @Override
    public String toString() {
        return String.format("Pathogen (%s)", this.name);
    }

    /** Select the order of the pathogens by the attributes. */
    @Override
    public int compareTo(Pathogen o) {
        // Compare to infectivity
        int compareValue = this.infectivity.compareTo(o.infectivity);
        if (compareValue != 0) {
            return compareValue;
        }

        // Compare to lethality
        compareValue = this.lethality.compareTo(o.lethality);
        if (compareValue != 0) {
            return compareValue;
        }

        // Compare to duration
        compareValue = this.duration.compareTo(o.duration);
        if (compareValue != 0) {
            return compareValue;
        }

        // Compare to mobility
        compareValue = this.mobility.compareTo(o.mobility);
        return compareValue;
    }

    /**
     * Parse the first round pathogens to the specified input layer for our neural networks.
     *
     * @param pathogens The list of pathogens that was encountered in the first round.
     * @return A float array as the specified input layer for our neural networks.
     */
    public static float[] pathogensToNetwork(List<Pathogen> pathogens) {

        // Path 1 Attributes, Path 2 Attributes, Path 3 Attributes
        // I1 L1 D1 M1 | I2 L2 D2 M2 | I3 L3 D3 M3
        float[] array = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        int i = 0;
        for (Pathogen pathogen : pathogens) {
            array[i] = pathogen.infectivity.strengthToNetwork();
            i = i + 1;
            array[i] = pathogen.lethality.strengthToNetwork();
            i = i + 1;
            array[i] = pathogen.duration.strengthToNetwork();
            i = i + 1;
            array[i] = pathogen.mobility.strengthToNetwork();
            i = i + 1;
        }
        return array;
    }
}
