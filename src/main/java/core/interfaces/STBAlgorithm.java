package core.interfaces;

import javafx.beans.property.BooleanProperty;

public interface STBAlgorithm<Argument extends STBComponent, Result> extends Runnable {

    BooleanProperty inProgressProperty();
    Result getResult();
}