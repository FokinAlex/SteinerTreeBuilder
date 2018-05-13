package core.implementations.euclidean;

import core.implementations.abstractions.AbstractGraph;

import java.util.HashSet;
import java.util.Set;

public class EuclideanGraph<Terminal extends EuclideanTerminal, Edge extends EuclideanEdge<Terminal>> extends AbstractGraph<Terminal, Edge> implements Cloneable {

    public EuclideanGraph() {
        // TODO: choose Set implementation
        this.vertexes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    @Override
    public Set<Terminal> getAllVertexes() {
        return super.getAllVertexes();
    }

    @Override
    public boolean addVertex(Terminal vertex) {
        return super.addVertex(vertex);
    }

    @Override
    public boolean addVertexes(Set<Terminal> vertexes) {
        return super.addVertexes(vertexes);
    }

    @Override
    public boolean removeVertex(Terminal vertex) {
        return super.removeVertex(vertex);
    }

    @Override
    public boolean removeVertexes(Set<Terminal> vertexes) {
        return super.removeVertexes(vertexes);
    }

    @Override
    public Set<Edge> getAllEdges() {
        return super.getAllEdges();
    }

    @Override
    public boolean addEdge(Edge edge) {
        return super.addEdge(edge);
    }

    @Override
    public boolean addEdges(Set<Edge> edges) {
        return super.addEdges(edges);
    }

    @Override
    public boolean removeEdge(Edge edge) {
        return super.removeEdge(edge);
    }

    @Override
    public boolean removeEdges(Set<Edge> edges) {
        return super.removeEdges(edges);
    }

    @Override
    public EuclideanGraph clone() throws CloneNotSupportedException {
        EuclideanGraph clone = new EuclideanGraph();
        this.vertexes.forEach(vertex -> {
            try {
                clone.vertexes.add(vertex.clone());
            } catch (CloneNotSupportedException e) { }
        });
        this.edges.forEach(edge ->
            clone.vertexes.forEach(cloneFirstEndpoint ->
                clone.vertexes.forEach(cloneSecondEndpoint -> {
                    if (edge.getFirstEndpoint().getId() == ((Terminal) cloneFirstEndpoint).getId() &&
                            edge.getSecondEndpoint().getId() == ((Terminal) cloneSecondEndpoint).getId())
                        clone.addEdge(new EuclideanEdge((Terminal) cloneFirstEndpoint, (Terminal) cloneSecondEndpoint));
                })
            )
        );
        return clone;
    }



    //    @Deprecated // TODO: swap this temporary solution with something
//    @Override
//    public EuclideanGraph clone() throws CloneNotSupportedException {
//        EuclideanGraph clone = null;
//        try {
//            File file = new File("temp.stb");
//            JsonUtils.writeJsonToFile(PageDataAccess.EUCLIDEAN.toJson(this), file);
//            clone = (EuclideanGraph) PageDataAccess.EUCLIDEAN.fromJson(JsonUtils.readJsonFromFile(file));
//        } catch (IOException | ParseException | IllegalComponentException | IllegalLocationException e) {
//            e.printStackTrace();
//        }
//        return clone;
//    }
}
