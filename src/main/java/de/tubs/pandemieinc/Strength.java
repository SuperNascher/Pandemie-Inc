package de.tubs.pandemieinc;

import com.fasterxml.jackson.annotation.JsonValue;

/** Enum to map the "strength" or "power" of a given attribute (City, Pathogen) */
public enum Strength {
    VERY_HIGH(2, "++"),
    HIGH(1, "+"),
    MIDDLE(0, "o"),
    LOW(-1, "-"),
    VERY_LOW(-2, "--");

    public final int level;
    public final String representation;

    Strength(int level, String representation) {
        this.level = level;
        this.representation = representation;
    }

    /**
     * Parse the Strength from the given String.
     *
     * @param value The strength value as String.
     * @return The parsed Strength enum.
     */
    public static Strength fromString(String value) {
        for (Strength strength : Strength.values()) {
            if (value.equals(strength.representation)) {
                return strength;
            }
        }

        String formatErrorMsg = "No Strength with representation '%s' found.";
        throw new IllegalArgumentException(String.format(formatErrorMsg, value));
    }

    /** Print the representation for "toString()". */
    @Override
    @JsonValue
    public String toString() {
        return this.representation;
    }
}
