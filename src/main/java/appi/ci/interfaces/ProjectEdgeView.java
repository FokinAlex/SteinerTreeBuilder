package appi.ci.interfaces;

import javafx.beans.property.BooleanProperty;

public interface ProjectEdgeView {

    ProjectPointView getFirstEndpoint();
    ProjectPointView getSecondEndpoint();

    BooleanProperty isSelectedProperty();

    void select();
    void unselect();
    void delete();
}
