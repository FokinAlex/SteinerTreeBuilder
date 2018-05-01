package gui;

import appi.ci.interfaces.AlgorithmType;
import core.implementations.GraphPage;
import core.implementations.euclidean.EuclideanEdge;
import core.interfaces.STBAlgorithm;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.AnchorPane;
import utils.vuu.StringDoubleConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PagePane extends AnchorPane {

    private GraphPage page;
    private PagePoint selectedPoint;
    private List<PagePoint> regularPoints;
    private PageEdge selectedEdge;
    private List<PageEdge> regularEdges;

    private BooleanProperty edgeAdditionMode = new SimpleBooleanProperty(false);
    private BooleanProperty hasSelectedTerminal = new SimpleBooleanProperty(false);
    private BooleanProperty hasSelectedEdge = new SimpleBooleanProperty(false);
    private BooleanProperty algorithmInProgress = new SimpleBooleanProperty(false);

    private StringProperty selectedTerminalXProperty;
    private StringProperty selectedTerminalYProperty;
    private StringProperty firstTerminalXProperty;
    private StringProperty firstTerminalYProperty;
    private StringProperty secondTerminalXProperty;
    private StringProperty secondTerminalYProperty;

    private StringProperty edgeLengthStringProperty = new SimpleStringProperty();
    private DoubleProperty edgeLengthDoubleProperty = new SimpleDoubleProperty();

    private ChangeListener<String> selectedTerminalXValueListener;
    private ChangeListener<String> selectedTerminalYValueListener;
    private ChangeListener<String> firstTerminalXValueListener;
    private ChangeListener<String> firstTerminalYValueListener;
    private ChangeListener<String> secondTerminalXValueListener;
    private ChangeListener<String> secondTerminalYValueListener;

    public PagePane(GraphPage page) {
        this.regularPoints = new ArrayList<>();
        this.regularEdges = new ArrayList<>();
        this.page = page;
        this.page.getGraph().getAllVertexes().forEach(terminal -> {
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
        this.edgeLengthDoubleProperty.addListener((observable, oldValue, newValue) -> edgeLengthStringProperty.set("Length: " + ((Math.round((Double) newValue * 1000) / 1000.))));
    }

    public void execute(AlgorithmType type) {
        STBAlgorithm algorithm = type.getInstance(this.page.getGraph());
        this.algorithmInProgress.bind(algorithm.inProgressProperty());
        this.algorithmInProgress.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.algorithmInProgress.unbind();
                // TODO: this.algorithmInProgress.removeListener();
            }
        });
        Thread thread = new Thread(algorithm);
        thread.start();
    }

    public void edgeAdditionMode(boolean value) {
        this.edgeAdditionMode.set(value);
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
    }

    public void deleteSelectedEdge() {
        STBEdge edge = this.selectedEdge.getEdge();
        this.regularEdges.remove(selectedEdge);
        this.getChildren().remove(selectedEdge);
        this.selectedEdge.delete();
        this.selectEdge(null);
        this.page.getGraph().removeEdge(edge);
    }

    public void deleteSelectedTerminal() {
        Iterator iterator = this.regularEdges.iterator();
        while (iterator.hasNext()) {
            PageEdge edge = (PageEdge) iterator.next();
            if (edge.getFirstEndpoint().equals(selectedPoint) || edge.getSecondEndpoint().equals(selectedPoint)) {
                this.selectedEdge = edge;
                iterator.remove();
                this.deleteSelectedEdge();
            }
        }
        STBTerminal terminal = this.selectedPoint.getTerminal();
        this.regularPoints.remove(selectedPoint);
        this.getChildren().remove(selectedPoint);
        this.selectedPoint.delete();
        this.selectPoint(null);
        this.page.getGraph().removeVertex(terminal);
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

    public void selectEdge(PageEdge edge) {
        this.selectedEdge = edge;
        this.hasSelectedEdge.set(false);
        this.hasSelectedTerminal.set(false);
        this.regularEdges.forEach(PageEdge::unselect);
        this.regularPoints.forEach(PagePoint::unselect);
        this.edgeLengthDoubleProperty.unbind();
        if (firstTerminalXValueListener != null) this.firstTerminalXProperty.removeListener(this.firstTerminalXValueListener);
        if (firstTerminalYValueListener != null) this.firstTerminalYProperty.removeListener(this.firstTerminalYValueListener);
        if (secondTerminalXValueListener != null) this.secondTerminalXProperty.removeListener(this.secondTerminalXValueListener);
        if (secondTerminalYValueListener != null) this.secondTerminalYProperty.removeListener(this.secondTerminalYValueListener);
        if (edge == null) return;
        edge.select();
        this.hasSelectedEdge.set(true);
        this.firstTerminalXProperty.set(edge.getFirstEndpoint().getLayoutX() + "");
        this.firstTerminalYProperty.set(edge.getFirstEndpoint().getLayoutY() + "");
        this.secondTerminalXProperty.set(edge.getSecondEndpoint().getLayoutX() + "");
        this.secondTerminalYProperty.set(edge.getSecondEndpoint().getLayoutY() + "");
        this.edgeLengthDoubleProperty.bind(edge.getLengthProperty());
        this.firstTerminalXValueListener = (observable, oldValue, newValue) -> edge.getFirstEndpoint().layoutXProperty().set(StringDoubleConverter.convert(newValue));
        this.firstTerminalYValueListener = (observable, oldValue, newValue) -> edge.getFirstEndpoint().layoutYProperty().set(StringDoubleConverter.convert(newValue));
        this.secondTerminalXValueListener = (observable, oldValue, newValue) -> edge.getSecondEndpoint().layoutXProperty().set(StringDoubleConverter.convert(newValue));
        this.secondTerminalYValueListener = (observable, oldValue, newValue) -> edge.getSecondEndpoint().layoutYProperty().set(StringDoubleConverter.convert(newValue));
        this.firstTerminalXProperty.addListener(firstTerminalXValueListener);
        this.firstTerminalYProperty.addListener(firstTerminalYValueListener);
        this.secondTerminalXProperty.addListener(secondTerminalXValueListener);
        this.secondTerminalYProperty.addListener(secondTerminalYValueListener);
    }

    public void selectPoint(PagePoint point) {
        this.selectedPoint = point;
        this.hasSelectedEdge.set(false);
        this.hasSelectedTerminal.set(false);
        this.regularEdges.forEach(PageEdge::unselect);
        this.regularPoints.forEach(PagePoint::unselect);
        if (selectedTerminalXValueListener != null) this.selectedTerminalXProperty.removeListener(this.selectedTerminalXValueListener);
        if (selectedTerminalYValueListener != null) this.selectedTerminalYProperty.removeListener(this.selectedTerminalYValueListener);
        if (point == null) return;
        point.select();
        this.hasSelectedTerminal.set(true);
        this.selectedTerminalXProperty.set(point.getLayoutX() + "");
        this.selectedTerminalYProperty.set(point.getLayoutY() + "");
        this.selectedTerminalXValueListener = (observable, oldValue, newValue) -> point.layoutXProperty().set(StringDoubleConverter.convert(newValue));
        this.selectedTerminalYValueListener = (observable, oldValue, newValue) -> point.layoutYProperty().set(StringDoubleConverter.convert(newValue));
        this.selectedTerminalXProperty.addListener(selectedTerminalXValueListener);
        this.selectedTerminalYProperty.addListener(selectedTerminalYValueListener);
    }

    public BooleanProperty selectedTerminalProperty() {
        return this.hasSelectedTerminal;
    }

    public BooleanProperty selectedEdgeProperty() {
        return this.hasSelectedEdge;
    }

    public BooleanProperty edgeAdditionModeProperty() {
        return this.edgeAdditionMode;
    }

    public BooleanProperty algorithmInProgressProperty() {
        return this.algorithmInProgress;
    }

    public void setSelectedTerminalXProperty(StringProperty property) {
        this.selectedTerminalXProperty = property;
    }

    public void setSelectedTerminalYProperty(StringProperty property) {
        this.selectedTerminalYProperty = property;
    }

    public void setFirstTerminalXProperty(StringProperty property) {
        this.firstTerminalXProperty = property;
    }

    public void setFirstTerminalYProperty(StringProperty property) {
        this.firstTerminalYProperty = property;
    }

    public void setSecondTerminalXProperty(StringProperty property) {
        this.secondTerminalXProperty = property;
    }

    public void setSecondTerminalYProperty(StringProperty property) {
        this.secondTerminalYProperty = property;
    }

    public void setEdgeLengthProperty(StringProperty property) {
        this.edgeLengthStringProperty = property;
    }
}
