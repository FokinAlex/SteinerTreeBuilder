package gui;

import appi.ci.interfaces.ProjectEdgeView;
import appi.ci.interfaces.ProjectPageView;
import appi.ci.interfaces.ProjectPointView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class PagePane extends AnchorPane implements ProjectPageView {

    private List<ProjectPointView> points;
    private List<ProjectEdgeView> edges;

    public PagePane() {
        this.points = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    @Deprecated
    @Override
    public List<ProjectPointView> getPoints() {
        return points;
    }

    @Deprecated
    @Override
    public List<ProjectEdgeView> getEdges() {
        return edges;
    }

    @Override
    public PagePoint addNewPoint(double x, double y) {
        PagePoint point = new PagePoint(x, y);
        return this.addPoint(point)? point : null;
    }

    @Override
    public boolean addPoint(ProjectPointView point) {
        if (!(point instanceof PagePoint)) return false;
        this.points.add(point);
        this.getChildren().add((PagePoint) point);
        return true;
    }

    @Override
    public boolean removePoint(ProjectPointView point) {
        if (point instanceof PagePoint) {
            ((PagePoint) point).layoutXProperty().unbind();
            ((PagePoint) point).layoutYProperty().unbind();
        }
        return this.points.remove(point) & this.getChildren().remove(point);
    }

    @Override
    public PageEdge addNewEdge(ProjectPointView firstEndpoint, ProjectPointView secondEndpoint) {
        if (!(firstEndpoint instanceof PagePoint && secondEndpoint instanceof PagePoint)) return null;
        PageEdge edge = new PageEdge((PagePoint) firstEndpoint, (PagePoint) secondEndpoint);
        return this.addEdge(edge)? edge : null;
    }

    @Override
    public boolean addEdge(ProjectEdgeView edge) {
        if (!(edge instanceof PageEdge)) return false;
        this.edges.add(edge);
        this.getChildren().add((PageEdge) edge);
        return true;
    }

    @Override
    public boolean removeEdge(ProjectEdgeView edge) {
        return this.edges.remove(edge) & this.getChildren().remove(edge);
    }
}