package core.implementations.abstractions;

import core.interfaces.STBAlgorithm;
import core.interfaces.STBComponent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class AbstractAlgorithm<Argument extends STBComponent, Result> implements STBAlgorithm<Argument, Result> {

    protected Argument argument;
    protected BooleanProperty inProgress = new SimpleBooleanProperty(false);

    public AbstractAlgorithm(Argument argument) {
        this.argument = argument;
    };

    @Override
    public BooleanProperty inProgressProperty() {
        return inProgress;
    }
}
