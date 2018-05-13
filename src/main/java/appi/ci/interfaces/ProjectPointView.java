package appi.ci.interfaces;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;

public interface ProjectPointView {

    BooleanProperty isFirstEndpoint();
    BooleanProperty isSecondEndpoint();
    BooleanProperty isSelectedProperty();

    DoubleProperty xPropertyProperty();
    DoubleProperty yPropertyProperty();

    void select();
    void unselect();
    void delete();
}
