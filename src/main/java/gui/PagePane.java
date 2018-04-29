package gui;

import core.implementations.GraphPage;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
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
    private PagePoint selectedPoint;
    private List<PagePoint> regularPoints;
    private List<PageEdge> regularEdges;

    private BooleanProperty edgeAdditionMode = new SimpleBooleanProperty(false);
    private BooleanProperty hasSelectedTerminal = new SimpleBooleanProperty(false);

    private StringProperty selectedTerminalXProperty;
    private StringProperty selectedTerminalYProperty;

    public PagePane(GraphPage page) {
        this.regularPoints = new ArrayList<>();
        this.regularEdges = new ArrayList<>();
        this.page = page;
        this.page.getGraph().getAllVertexes().forEach(terminal -> {
            // TODO: change to property ???
            PagePoint point = this.addRegularPoint(
                    ((STBTerminal) terminal).getLocation().getXProperty().get(),
                    ((STBTerminal) terminal).getLocation().getYProperty().get()
            );
            point.setTerminal((STBTerminal) terminal);
        });
        this.page.getGraph().getAllEdges().forEach(edge ->
            regularPoints.forEach(firstEndpoint ->
                regularPoints.forEach(secondEndpoint -> {
                    if ((firstEndpoint.getTerminal()).equals(((EuclideanEdge) edge).getFirstEndpoint()) &&
                        (secondEndpoint.getTerminal()).equals(((EuclideanEdge) edge).getSecondEndpoint())) {
                        PageEdge regularEdge = this.addRegularEdge(firstEndpoint, secondEndpoint);
                        regularEdge.setEdge((STBEdge) edge);
        }})));
    }

    public void edgeAditionModeOn() {
        this.edgeAdditionMode.set(true);
    }

    public PagePoint getSelectedPoint() {
        return selectedPoint;
    }

    public void addTerminal(double x, double y) {
        PagePoint point = this.addRegularPoint(x, y);
        this.page.getGraph().addVertex(point.genTerminal(x, y));
    }

    public void addEdge(PagePoint firstEndpoint, PagePoint secondEndpoint) {
        PageEdge edge = this.addRegularEdge(firstEndpoint, secondEndpoint);
        this.page.getGraph().addEdge(edge.genEdge(firstEndpoint.getTerminal(), secondEndpoint.getTerminal()));
        this.edgeAdditionMode.set(false);
    }

    private PagePoint addRegularPoint(double x, double y) {
        PagePoint regularPoint = new PagePoint(x, y);
        this.regularPoints.add(regularPoint);
        this.getChildren().add(regularPoint);
        return regularPoint;
    }

    private PageEdge addRegularEdge(PagePoint firstEndpoint, PagePoint secondEndpoint) {
        PageEdge regularEdge = new PageEdge(firstEndpoint, secondEndpoint);
        this.regularEdges.add(regularEdge);
        this.getChildren().add(regularEdge);
        return regularEdge;
    }

    private ChangeListener<String> x_listener;
    private ChangeListener<String> y_listener;
    public void select(PagePoint point) {
        this.selectedPoint = point;
        this.hasSelectedTerminal.set(false);
        this.regularPoints.forEach(PagePoint::unselect);
        if (x_listener != null) this.selectedTerminalXProperty.removeListener(x_listener);
        if (y_listener != null) this.selectedTerminalYProperty.removeListener(y_listener);
        if (point == null) return;
        this.hasSelectedTerminal.set(true);
        point.select();
        this.selectedTerminalXProperty.set(point.getLayoutX() + "");
        this.selectedTerminalYProperty.set(point.getLayoutY() + "");
        this.x_listener = (observable, oldValue, newValue) -> point.layoutXProperty().set(StringDoubleConverter.convert(newValue));
        this.y_listener = (observable, oldValue, newValue) -> point.layoutYProperty().set(StringDoubleConverter.convert(newValue));
        this.selectedTerminalXProperty.addListener(x_listener);
        this.selectedTerminalYProperty.addListener(y_listener);
    }

    public BooleanProperty selectedTerminalProperty() {
        return this.hasSelectedTerminal;
    }

    public BooleanProperty edgeAdditionModeProperty() {
        return this.edgeAdditionMode;
    }

    public void setSelectedTerminalXProperty(StringProperty property) {
        this.selectedTerminalXProperty = property;
    }

    public void setSelectedTerminalYProperty(StringProperty property) {
        this.selectedTerminalYProperty = property;
    }
}
