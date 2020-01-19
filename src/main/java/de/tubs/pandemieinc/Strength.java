package de.tubs.pandemieinc;

import com.fasterxml.jackson.annotation.JsonValue;

/** Enum to map the "strength" or "power" of a given attribute (City, Pathogen) */
public enum Strength {
    VERY_LOW(-2, "--"),
    LOW(-1, "-"),
    MIDDLE(0, "o"),
    HIGH(1, "+"),
    VERY_HIGH(2, "++");

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

    public float strengthToNetwork() {
        if (this.level == -2) {
            return 0.2f;
        }

        if (this.level == -1) {
            return 0.4f;
        }

        if (this.level  == 0) {
            return 0.6f;
        }

        if (this.level == 1) {
            return 0.8f;
        }

        if (this.level == 2) {
            return 1.0f;
        }

        // Should not happen
        return 0.0f;
    }
}
