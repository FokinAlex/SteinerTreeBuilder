package gui;

import core.exceptions.IllegalLocationException;
import core.implementations.GraphPage;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.AnchorPane;
import utils.vuu.StringDoubleConverter;

import java.util.ArrayList;
import java.util.List;

public class PagePane extends AnchorPane {

    private GraphPage page;
    private List<PagePoint> regularPoints;
    private BooleanProperty hasSelectedTerminal = new SimpleBooleanProperty(false);
    private StringProperty selectedTerminalXProperty;
    private StringProperty selectedTerminalYProperty;

    public PagePane(GraphPage page) {
        this.regularPoints = new ArrayList<>();
        this.page = page;
        this.page.getGraph().getAllVertexes().forEach(terminal -> {
            this.addRegularPoint(
                    ((EuclideanTerminal) terminal).getLocation().getX(),
                    ((EuclideanTerminal) terminal).getLocation().getY()
            );
        });
        this.page.getGraph().getAllEdges().forEach(edge -> {
            // TODO: do something
        });
    }

    public void addTerminal(double x, double y) {
        try {
            this.page.getGraph().addVertex(new EuclideanTerminal(new EuclideanLocation(x, y)));
            this.addRegularPoint(x, y);
        } catch (IllegalLocationException e) {
            // Never be here
        }
    }

    private void addRegularPoint(double x, double y) {
        PagePoint point = new PagePoint(x, y);
        this.regularPoints.add(point);
        this.getChildren().add(point);
    }

    private ChangeListener<String> x_listener;
    private ChangeListener<String> y_listener;
    public void select(PagePoint exception) {
        this.hasSelectedTerminal.set(false);
        this.regularPoints.forEach(PagePoint::unselect);
        if (x_listener != null) this.selectedTerminalXProperty.removeListener(x_listener);
        if (y_listener != null) this.selectedTerminalYProperty.removeListener(y_listener);
        if (exception == null) return;
        this.hasSelectedTerminal.set(true);
        exception.select();
        this.selectedTerminalXProperty.set(exception.getLayoutX() + "");
        this.selectedTerminalYProperty.set(exception.getLayoutY() + "");
        this.x_listener = (observable, oldValue, newValue) -> exception.layoutXProperty().set(StringDoubleConverter.convert(newValue));
        this.y_listener = (observable, oldValue, newValue) -> exception.layoutYProperty().set(StringDoubleConverter.convert(newValue));
        this.selectedTerminalXProperty.addListener(x_listener);
        this.selectedTerminalYProperty.addListener(y_listener);
    }

    public BooleanProperty selectedTerminalProperty() {
        return this.hasSelectedTerminal;
    }

    public void setSelectedTerminalXProperty(StringProperty property) {
        this.selectedTerminalXProperty = property;
    }

    public void setSelectedTerminalYProperty(StringProperty property) {
        this.selectedTerminalYProperty = property;
    }
}
