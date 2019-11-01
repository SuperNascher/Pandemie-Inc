package de.tubs.pandemieinc.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class BaseAction {
    // Variables for the output/response
    protected String type;

    // Only print the amount of rounds, if an action implements that
    // and has a value != 0
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int rounds = 0;

    // Variables that are not printed in the result
    @JsonIgnore
    // Used to determine the cost per round
    public int roundCost = 0;

    @JsonIgnore
    // Used to determine the "normal" cost
    public int baseCost = 0;

    public int totalCost() {
        return this.rounds * this.roundCost + this.baseCost;
    }

    public int minCost() {
        return this.roundCost + this.baseCost;
    }

    public String getType() {
        return this.type;
    }
}
