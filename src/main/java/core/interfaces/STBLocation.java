package core.interfaces;

import javafx.beans.property.DoubleProperty;

public interface STBLocation extends Cloneable {

    DoubleProperty getXProperty();
    DoubleProperty getYProperty();
}
