package core.implementations.euclidean;

import core.implementations.abstractions.AbstractWeightedGraph;

import java.util.HashSet;
import java.util.Set;

public class EuclideanGraph<Terminal extends EuclideanTerminal, Edge extends EuclideanEdge<Terminal, Weight>, Weight extends Double> extends AbstractWeightedGraph<Terminal, Edge, Weight> {

    // Default modifier here is for the package-private access
    EuclideanGraph() {
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
    public Weight getWeight() {
        return (Weight) this.edges.stream()
                .map(e -> EuclideanMetric.METRIC.apply(
                    e.getFirstEndpoint().getLocation(),
                    e.getSecondEndpoint().getLocation()))
                .reduce(Weight::sum)
                .orElse(0.);
    }
}
