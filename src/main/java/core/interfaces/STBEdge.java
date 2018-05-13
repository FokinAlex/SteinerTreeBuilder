package core.interfaces;

import javafx.beans.property.DoubleProperty;

public interface STBEdge<T extends STBTerminal> extends STBComponent, Cloneable {

    T getFirstEndpoint();
    T getSecondEndpoint();

    DoubleProperty getLengthProperty();
    double getLength();
}
