package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.Round;
import java.util.function.Supplier;

public abstract class ImplementationDecorator implements PandemieImpl {

    PandemieImpl implementation;

    public ImplementationDecorator(PandemieImpl implementation) {
        this.implementation = implementation;
    }

    public ImplementationDecorator(Supplier<PandemieImpl> constructor) {
        this.implementation = constructor.get();
    }

    public String selectAction(Round round) {
        // Forward the call to the implementation
        return this.implementation.selectAction(round);
    }
}
