package appi.ci.interfaces;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.css.PseudoClass;

public interface ProjectPointView {

    BooleanProperty isFirstEndpoint();
    BooleanProperty isSecondEndpoint();
    BooleanProperty isSelectedProperty();

    DoubleProperty xPropertyProperty();
    DoubleProperty yPropertyProperty();

    void setTerminalType(PseudoClass type);

    void select();
    void unselect();
    void delete();
}
