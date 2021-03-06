package core.interfaces;

import javafx.beans.property.DoubleProperty;

import java.util.Set;

public interface STBGraph<V extends STBTerminal, E extends STBEdge> extends STBComponent, Cloneable {

    Set<V> getAllVertexes();
    boolean addVertex(V vertex);
    boolean addVertexes(Set<V> vertexes);
    boolean removeVertex(V vertex);
    boolean removeVertexes(Set<V> vertexes);

    Set<E> getAllEdges();
    boolean addEdge(E edge);
    boolean addEdges(Set<E> edges);
    boolean removeEdge(E edge);
    boolean removeEdges(Set<E> edges);

    double getWeight();
    DoubleProperty weightProperty();
}
