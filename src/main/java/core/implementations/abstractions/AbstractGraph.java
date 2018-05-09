package core.implementations.abstractions;

import core.interfaces.STBEdge;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;

import java.util.Set;

public abstract class AbstractGraph<Terminal extends STBTerminal, Edge extends STBEdge> implements STBGraph<Terminal, Edge> {

    protected Set<Terminal> vertexes;
    protected Set<Edge> edges;

    @Override
    public Set<Terminal> getAllVertexes() {
        return this.vertexes;
    }

    @Override
    public boolean addVertex(Terminal vertex) {
        return this.vertexes.add(vertex);
    }

    @Override
    public boolean addVertexes(Set<Terminal> vertexes) {
        return this.vertexes.addAll(vertexes);
    }

    @Override
    public boolean removeVertex(Terminal vertex) {
        this.edges.forEach(edge -> {
            if (edge.getFirstEndpoint().equals(vertex) || edge.getSecondEndpoint().equals(vertex)) this.removeEdge(edge);
        });
        return this.vertexes.remove(vertex);
    }

    @Override
    public boolean removeVertexes(Set<Terminal> vertexes) {
        this.vertexes.forEach(vertex -> this.edges.forEach(edge -> {
            if (edge.getFirstEndpoint().equals(vertex) || edge.getSecondEndpoint().equals(vertex)) this.removeEdge(edge);
        }));
        return this.vertexes.removeAll(vertexes);
    }

    @Override
    public Set<Edge> getAllEdges() {
        return this.edges;
    }

    @Override
    public boolean addEdge(Edge edge) {
        return this.edges.add(edge);
    }

    @Override
    public boolean addEdges(Set<Edge> edges) {
        return this.edges.addAll(edges);
    }

    @Override
    public boolean removeEdge(Edge edge) {
        return this.edges.remove(edge);
    }

    @Override
    public boolean removeEdges(Set<Edge> edges) {
        return this.edges.removeAll(edges);
    }

    @Override
    public double getWeight() {
        return this.edges.stream()
                .map(Edge::getLength)
                .reduce((a, b) -> a + b)
                .get();
    }

    @Override
    protected abstract STBGraph clone() throws CloneNotSupportedException;
}
