package core.interfaces;

import javafx.beans.property.BooleanProperty;

public interface STBAlgorithm<Argument extends STBComponent> extends Runnable {

    BooleanProperty inProgressProperty();
}