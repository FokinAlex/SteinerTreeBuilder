package core.implementations.abstractions;

import core.interfaces.STBAlgorithm;
import core.interfaces.STBComponent;

public abstract class AbstractAlgorithm<Argument extends STBComponent> implements STBAlgorithm<Argument> {

    protected Argument argument;

    public AbstractAlgorithm(Argument argument) {
        this.argument = argument;
    };
}
