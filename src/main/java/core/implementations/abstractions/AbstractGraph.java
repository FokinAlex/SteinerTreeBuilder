package core.implementations.abstractions;

import core.interfaces.STBEdge;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractGraph<Terminal extends STBTerminal, Edge extends STBEdge> implements STBGraph<Terminal, Edge> {

    protected Set<Terminal> vertexes;
    protected Set<Edge> edges;
    protected DoubleProperty weightProperty = new SimpleDoubleProperty(0.);

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
        Iterator<Edge> iterator = this.edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.getFirstEndpoint().equals(vertex) || edge.getSecondEndpoint().equals(vertex)) iterator.remove();
        }
        return this.vertexes.remove(vertex);
    }

    @Override
    public boolean removeVertexes(Set<Terminal> vertexes) {
        this.vertexes.forEach(vertex -> {
            Iterator<Edge> iterator = this.edges.iterator();
            while (iterator.hasNext()) {
                Edge edge = iterator.next();
                if (edge.getFirstEndpoint().equals(vertex) || edge.getSecondEndpoint().equals(vertex)) {
                    this.weightProperty.set(this.weightProperty.get() - edge.getLength());
                    iterator.remove();
                }
            }
        });
        return this.vertexes.removeAll(vertexes);
    }

    @Override
    public Set<Edge> getAllEdges() {
        return this.edges;
    }

    @Override
    public boolean addEdge(Edge edge) {
        this.weightProperty.set(this.weightProperty.get() + edge.getLength());
        edge.getLengthProperty().addListener((observable, oldValue, newValue) -> this.weightProperty.set(this.weightProperty.get() + ((double) newValue - (double) oldValue)));
        return this.edges.add(edge);
    }

    @Override
    public boolean addEdges(Set<Edge> edges) {
        edges.forEach(edge -> {
            this.weightProperty.set(this.weightProperty.get() + edge.getLength());
            edge.getLengthProperty().addListener((observable, oldValue, newValue) -> this.weightProperty.set(this.weightProperty.get() + ((double) newValue - (double) oldValue)));
        });
        return this.edges.addAll(edges);
    }

    @Override
    public boolean removeEdge(Edge edge) {
        this.weightProperty.set(this.weightProperty.get() - edge.getLength());
        return this.edges.remove(edge);
    }

    @Override
    public boolean removeEdges(Set<Edge> edges) {
        edges.forEach(edge -> this.weightProperty.set(this.weightProperty.get() - edge.getLength()));
        return this.edges.removeAll(edges);
    }

    @Override
    public double getWeight() {
        Optional<Double> weight = this.edges.stream()
                .map(Edge::getLength)
                .reduce((a, b) -> a + b);
        return weight.isPresent() ? weight.get() : 0;
    }

    @Override
    public DoubleProperty weightProperty() {
        return this.weightProperty;
    }

    @Override
    protected abstract STBGraph clone() throws CloneNotSupportedException;
}
