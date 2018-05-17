package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import appi.ci.interfaces.ProjectEdgeView;
import appi.ci.interfaces.ProjectPageView;
import appi.ci.interfaces.ProjectPointView;
import core.implementations.GraphPage;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBAlgorithm;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import gui.PageEdge;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import utils.IdUtils;
import utils.vuu.StringDoubleConverter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GraphPagePaneController {

    Map<ProjectPointView, STBTerminal> points;
    Map<ProjectEdgeView, STBEdge> edges;

    private ProjectPointView selectedPoint;
    private ProjectEdgeView selectedEdge;

    private ProjectPageView pageView;
    private GraphPage page;

    private BooleanProperty hasSelectedPoint = new SimpleBooleanProperty(false);
    private BooleanProperty hasSelectedEdge = new SimpleBooleanProperty(false);
    private BooleanProperty edgeAdditionMode = new SimpleBooleanProperty(false);
    private BooleanProperty singleEdgeAdditionMode = new SimpleBooleanProperty(false);
    private BooleanProperty algorithmInProgress = new SimpleBooleanProperty(false);

    private StringProperty selectedPointXProperty;
    private StringProperty selectedPointYProperty;
    private StringProperty firstPointXProperty;
    private StringProperty firstPointYProperty;
    private StringProperty secondPointXProperty;
    private StringProperty secondPointYProperty;

    private DoubleProperty edgeLengthDoubleProperty = new SimpleDoubleProperty();

    private ChangeListener<String> selectedTerminalXValueListener;
    private ChangeListener<String> selectedTerminalYValueListener;
    private ChangeListener<String> firstTerminalXValueListener;
    private ChangeListener<String> firstTerminalYValueListener;
    private ChangeListener<String> secondTerminalXValueListener;
    private ChangeListener<String> secondTerminalYValueListener;

    public GraphPagePaneController(ProjectPageView pageView, GraphPage page) {
        // TODO: set type
        this.pageView = pageView;
        this.page = page;
        this.points = new HashMap<>();
        this.edges = new HashMap<>();
        this.graphInit();
    }

    public GraphPage getPage() {
        return this.page;
    }

    public void execute(AlgorithmType type, GraphPagePaneController resultController) {
        STBAlgorithm algorithm = type.getInstance(this.page.getGraph());
        this.algorithmInProgress.bind(algorithm.inProgressProperty());
        this.algorithmInProgress.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.algorithmInProgress.unbind();
                EuclideanGraph result = (EuclideanGraph) algorithm.getResult();
                result.getAllVertexes().forEach(vertex -> {
                    resultController.addPoint((STBTerminal) vertex);
                    resultController.page.getGraph().addVertex((STBTerminal) vertex);
                });
                result.getAllEdges().forEach(edge -> {
                    resultController.addEdge((STBEdge) edge);
                    resultController.page.getGraph().addEdge((STBEdge) edge);
                });
                // TODO: algorithm.getResult() -> report
                // TODO: unblock something < - - - +
            }
        });
        // TODO: block something - - - - - - - - - +
        algorithm.run();
//        TODO: run algorithms in different threads
//        Thread thread = new Thread(algorithm);
//        thread.start();
    }

    public ProjectPointView addPoint(STBTerminal terminal) {
        // TODO: check type
        ProjectPointView pointView = this.pageView.addNewPoint(terminal.getLocation().getXProperty().get(), terminal.getLocation().getYProperty().get());
        pointView.isSelectedProperty().addListener((observable, oldValue, newValue) -> {
            if ((this.edgeAdditionMode.get() || this.singleEdgeAdditionMode.get()) && this.hasSelectedPoint.get() && newValue)  {
                if (!pointView.equals(this.selectedPoint)) {
                    this.addEdge(this.selectedPoint, pointView);
                }
                this.singleEdgeAdditionMode.set(false);
            }
            if (newValue) this.select(pointView);
            else this.unselect(pointView);
        });
        terminal.getLocation().getXProperty().bind(pointView.xPropertyProperty());
        terminal.getLocation().getYProperty().bind(pointView.yPropertyProperty());
//        TODO: if something must change in real time
//        algorithmInProgress.addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                terminal.getLocation().getXProperty().unbind();
//                terminal.getLocation().getYProperty().unbind();
//                pointView.xPropertyProperty().bind(this.terminal.getLocation().getXProperty());
//                pointView.yPropertyProperty().bind(this.terminal.getLocation().getYProperty());
//            } else {
//                pointView.xPropertyProperty().unbind();
//                pointView.yPropertyProperty().unbind();
//                terminal.getLocation().getXProperty().bind(pointView.xPropertyProperty());
//                terminal.getLocation().getYProperty().bind(pointView.yPropertyProperty());
//            }
//        });
        this.points.put(pointView, terminal);
        return pointView;
    }

    public ProjectPointView addPoint(double x, double y) {
        STBTerminal terminal = new EuclideanTerminal(new EuclideanLocation(x, y), IdUtils.getTerminalId(this.page.getGraph()));
        ProjectPointView pointView = this.addPoint(terminal);
        this.page.getGraph().addVertex(terminal);
        return pointView;
    }

    public void deleteSelectedPoint() {
        Iterator iterator = this.pageView.getEdges().iterator();
        while (iterator.hasNext()) {
            ProjectEdgeView edge = (PageEdge) iterator.next();
            if (edge.getFirstEndpoint().equals(this.selectedPoint) || edge.getSecondEndpoint().equals(this.selectedPoint)) {
                this.selectedEdge = edge;
                iterator.remove();
                this.deleteSelectedEdge();
            }
        }
        STBTerminal terminal = this.points.get(this.selectedPoint);
        terminal.getLocation().getXProperty().unbind();
        terminal.getLocation().getYProperty().unbind();
        this.pageView.removePoint(this.selectedPoint);
        this.points.remove(this.selectedPoint);
        this.selectedPoint.delete();
        this.page.getGraph().removeVertex(terminal);
    }

    public ProjectPointView getSelectedPoint() {
        return this.selectedPoint;
    }

    public ProjectEdgeView addEdge(STBEdge edge) {
        // TODO: check type
        final ProjectEdgeView[] edgeView = new ProjectEdgeView[1];
        this.points.forEach((firstPoint, firstTerminal) -> this.points.forEach((secondPoint, secondTerminal) -> {
            if (firstTerminal.equals(edge.getFirstEndpoint()) && secondTerminal.equals(edge.getSecondEndpoint())) {
                edgeView[0] = this.pageView.addNewEdge(firstPoint, secondPoint);
                edgeView[0].isSelectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) this.select(edgeView[0]);
                    else this.unselect(edgeView[0]);
                });
                this.edges.put(edgeView[0], edge);
            }
        }));
        return edgeView[0];
    }

    public ProjectEdgeView addEdge(ProjectPointView firstEndpoint, ProjectPointView secondEndpoint) {
        STBEdge edge = new EuclideanEdge(
                (EuclideanTerminal) this.points.get(firstEndpoint),
                (EuclideanTerminal) this.points.get(secondEndpoint)
        );
        ProjectEdgeView edgeView = this.addEdge(edge);
        this.page.getGraph().addEdge(edge);
        return edgeView;
    }

    public void deleteSelectedEdge() {
        STBEdge edge = this.edges.get(this.selectedEdge);
        this.pageView.removeEdge(this.selectedEdge); // returns false
        this.edges.remove(this.selectedEdge);
        this.selectedEdge.delete();
        this.page.getGraph().removeEdge(edge);
    }

    public ProjectEdgeView getSelectedEdge() {
        return this.selectedEdge;
    }

    private void graphInit() {
        this.page.getGraph().getAllVertexes().forEach(terminal -> this.addPoint((STBTerminal) terminal));
        this.page.getGraph().getAllEdges().forEach(edge -> this.addEdge((STBEdge) edge));
    }

    private void select(ProjectPointView point) {
        this.selectedPoint = point;
        this.pageView.getPoints().forEach(tempPoint -> { if (!tempPoint.equals(point)) tempPoint.unselect(); });
        this.hasSelectedPoint.set(true);
        this.pageView.getEdges().forEach(ProjectEdgeView::unselect);
        this.hasSelectedEdge.set(false);
        this.selectedPointXProperty.set(point.xPropertyProperty().get() + "");
        this.selectedPointYProperty.set(point.yPropertyProperty().get() + "");
        this.selectedTerminalXValueListener = (observable, oldValue, newValue) -> point.xPropertyProperty().set(StringDoubleConverter.convert(newValue));
        this.selectedTerminalYValueListener = (observable, oldValue, newValue) -> point.yPropertyProperty().set(StringDoubleConverter.convert(newValue));
        this.selectedPointXProperty.addListener(selectedTerminalXValueListener);
        this.selectedPointYProperty.addListener(selectedTerminalYValueListener);
    }

    private void select(ProjectEdgeView edge) {
        this.selectedEdge = edge;
        this.pageView.getPoints().forEach(ProjectPointView::unselect);
        this.hasSelectedPoint.set(false);
        this.pageView.getEdges().forEach(tempEdge -> { if (!tempEdge.equals(edge)) tempEdge.unselect(); });
        this.hasSelectedEdge.set(true);
        this.selectedEdge.getFirstEndpoint().isFirstEndpoint().set(true);
        this.selectedEdge.getSecondEndpoint().isSecondEndpoint().set(true);
        this.firstPointXProperty.set(edge.getFirstEndpoint().xPropertyProperty().get() + "");
        this.firstPointYProperty.set(edge.getFirstEndpoint().yPropertyProperty().get() + "");
        this.secondPointXProperty.set(edge.getSecondEndpoint().xPropertyProperty().get() + "");
        this.secondPointYProperty.set(edge.getSecondEndpoint().yPropertyProperty().get() + "");
        this.edgeLengthDoubleProperty.bind(edges.get(selectedEdge).getLengthProperty());
        this.firstTerminalXValueListener = (observable, oldValue, newValue) -> edge.getFirstEndpoint().xPropertyProperty().set(StringDoubleConverter.convert(newValue));
        this.firstTerminalYValueListener = (observable, oldValue, newValue) -> edge.getFirstEndpoint().yPropertyProperty().set(StringDoubleConverter.convert(newValue));
        this.secondTerminalXValueListener = (observable, oldValue, newValue) -> edge.getSecondEndpoint().xPropertyProperty().set(StringDoubleConverter.convert(newValue));
        this.secondTerminalYValueListener = (observable, oldValue, newValue) -> edge.getSecondEndpoint().yPropertyProperty().set(StringDoubleConverter.convert(newValue));
        this.firstPointXProperty.addListener(firstTerminalXValueListener);
        this.firstPointYProperty.addListener(firstTerminalYValueListener);
        this.secondPointXProperty.addListener(secondTerminalXValueListener);
        this.secondPointYProperty.addListener(secondTerminalYValueListener);
    }

    private void unselect(ProjectPointView point) {
        if (selectedTerminalXValueListener != null) {
            this.selectedPointXProperty.removeListener(this.selectedTerminalXValueListener);
            this.selectedTerminalXValueListener = null;
        }
        if (selectedTerminalYValueListener != null) {
            this.selectedPointYProperty.removeListener(this.selectedTerminalYValueListener);
            this.selectedTerminalYValueListener = null;
        }
        if (point.equals(selectedPoint)) {
            this.selectedPoint = null;
            this.hasSelectedEdge.set(false);
            this.hasSelectedPoint.set(false);
        }
    }

    private void unselect(ProjectEdgeView edge) {
        if (firstTerminalXValueListener != null) {
            this.firstPointXProperty.removeListener(this.firstTerminalXValueListener);
            this.firstTerminalXValueListener = null;

        }
        if (firstTerminalYValueListener != null) {
            this.firstPointYProperty.removeListener(this.firstTerminalYValueListener);
            this.firstTerminalYValueListener = null;
        }
        if (secondTerminalXValueListener != null) {
            this.secondPointXProperty.removeListener(this.secondTerminalXValueListener);
            this.secondTerminalXValueListener = null;
        }
        if (secondTerminalYValueListener != null) {
            this.secondPointYProperty.removeListener(this.secondTerminalYValueListener);
            this.secondTerminalYValueListener = null;
        }
        if (edge.equals(selectedEdge)) {
            this.selectedEdge.getFirstEndpoint().isFirstEndpoint().set(false);
            this.selectedEdge.getSecondEndpoint().isSecondEndpoint().set(false);
            this.selectedEdge = null;
            this.hasSelectedEdge.set(false);
            this.hasSelectedPoint.set(false);
            this.edgeLengthDoubleProperty.unbind();
        }
    }

    BooleanProperty selectedTerminalProperty() {
        return this.hasSelectedPoint;
    }

    BooleanProperty selectedEdgeProperty() {
        return this.hasSelectedEdge;
    }

    BooleanProperty edgeAdditionModeProperty() {
        return this.edgeAdditionMode;
    }

    public BooleanProperty algorithmInProgressProperty() {
        return this.algorithmInProgress;
    }

    public DoubleProperty edgeLengthProperty() {
        return this.edgeLengthDoubleProperty;
    }

    public void setSingleEdgeAdditionModeProperty(BooleanProperty property) {
        this.singleEdgeAdditionMode = property;
    }

    public void setSelectedPointXPropertyFollower(StringProperty property) {
        this.selectedPointXProperty = property;
    }

    public void setSelectedPointYPropertyFollower(StringProperty property) {
        this.selectedPointYProperty = property;
    }

    public void setFirstPointXPropertyFollower(StringProperty property) {
        this.firstPointXProperty = property;
    }

    public void setFirstPointYPropertyFollower(StringProperty property) {
        this.firstPointYProperty = property;
    }

    public void setSecondPointXPropertyFollower(StringProperty property) {
        this.secondPointXProperty = property;
    }

    public void setSecondPointYPropertyFollower(StringProperty property) {
        this.secondPointYProperty = property;
    }

    public void restoreProperties() {
        if (selectedPoint != null) {
            this.selectedPointXProperty.set(this.selectedPoint.xPropertyProperty().get() + "");
            this.selectedPointYProperty.set(this.selectedPoint.yPropertyProperty().get() + "");
            this.selectedPointXProperty.addListener(this.selectedTerminalXValueListener);
            this.selectedPointYProperty.addListener(this.selectedTerminalYValueListener);
        }
        if (this.selectedEdge != null) {
            this.firstPointXProperty.set(this.selectedEdge.getFirstEndpoint().xPropertyProperty().get() + "");
            this.firstPointYProperty.set(this.selectedEdge.getFirstEndpoint().yPropertyProperty().get() + "");
            this.secondPointXProperty.set(this.selectedEdge.getSecondEndpoint().xPropertyProperty().get() + "");
            this.secondPointYProperty.set(this.selectedEdge.getSecondEndpoint().yPropertyProperty().get() + "");
            this.firstPointXProperty.addListener(this.firstTerminalXValueListener);
            this.firstPointYProperty.addListener(this.firstTerminalYValueListener);
            this.secondPointXProperty.addListener(this.secondTerminalXValueListener);
            this.secondPointYProperty.addListener(this.secondTerminalYValueListener);
        }
    }

    public void clearProperties() {
        if (selectedTerminalXValueListener != null) this.selectedPointXProperty.removeListener(this.selectedTerminalXValueListener);
        if (selectedTerminalYValueListener != null) this.selectedPointYProperty.removeListener(this.selectedTerminalYValueListener);
        if (firstTerminalXValueListener != null) this.firstPointXProperty.removeListener(this.firstTerminalXValueListener);
        if (firstTerminalYValueListener != null) this.firstPointYProperty.removeListener(this.firstTerminalYValueListener);
        if (secondTerminalXValueListener != null) this.secondPointXProperty.removeListener(this.secondTerminalXValueListener);
        if (secondTerminalYValueListener != null) this.secondPointYProperty.removeListener(this.secondTerminalYValueListener);
    }
}
