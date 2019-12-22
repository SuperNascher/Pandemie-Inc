package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.Round;

public class EndRoundImplementation implements PandemieImpl {

    public EndRoundImplementation() {}

    public String selectAction(Round round) {
        return ActionPrinter.endRound();
    }
}
