package core.implementations.abstractions;

import core.interfaces.STBEdge;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;

import java.util.Set;

public abstract class AbstractGraph<Terminal extends STBTerminal, Edge extends STBEdge> implements STBGraph<Terminal, Edge> {

    protected Set<Terminal> vertexes;
    protected Set<Edge> edges;

    public Set<Terminal> getAllVertexes() {
        return this.vertexes;
    }

    public boolean addVertex(Terminal vertex) {
        return this.vertexes.add(vertex);
    }

    public boolean addVertexes(Set<Terminal> vertexes) {
        return this.vertexes.addAll(vertexes);
    }

    public boolean removeVertex(Terminal vertex) {
        return this.vertexes.remove(vertex);
    }

    public boolean removeVertexes(Set<Terminal> vertexes) {
        return this.vertexes.removeAll(vertexes);
    }

    public Set<Edge> getAllEdges() {
        return this.edges;
    }

    public boolean addEdge(Edge edge) {
        return this.edges.add(edge);
    }

    public boolean addEdges(Set<Edge> edges) {
        return this.edges.addAll(edges);
    }

    public boolean removeEdge(Edge edge) {
        return this.edges.remove(edge);
    }

    public boolean removeEdges(Set<Edge> edges) {
        return this.edges.removeAll(edges);
    }
}
