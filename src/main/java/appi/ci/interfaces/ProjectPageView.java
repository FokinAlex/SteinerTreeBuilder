package appi.ci.interfaces;

import java.util.List;

public interface ProjectPageView {

    @Deprecated List<ProjectPointView> getPoints();
    @Deprecated List<ProjectEdgeView> getEdges();

    ProjectPointView addNewPoint(double x, double y);
    boolean addPoint(ProjectPointView point);
    boolean removePoint(ProjectPointView point);

    ProjectEdgeView addNewEdge(ProjectPointView firstEndpoint, ProjectPointView secondEndpoint);
    boolean addEdge(ProjectEdgeView edge);
    boolean removeEdge(ProjectEdgeView edge);
}
