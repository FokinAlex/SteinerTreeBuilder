package core.implementations.euclidean;

import core.exceptions.IllegalComponentException;
import core.implementations.abstractions.AbstractGraph;
import core.interfaces.STBGraph;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class EuclideanGraph<Terminal extends EuclideanTerminal, Edge extends EuclideanEdge> extends AbstractGraph<Terminal, Edge> {

    // Default modifier here is for the package-private access
    EuclideanGraph() {
        // TODO: choose Set implementation
        this.vertexes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public double getLength() {
        return this.edges.stream()
                .map(edge -> EuclideanMetric.METRIC.apply(
                        edge.getFirstEndpoint().getLocation(),
                        edge.getSecondEndpoint().getLocation()))
                .reduce(Double::sum)
                .orElse(0.);
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
}