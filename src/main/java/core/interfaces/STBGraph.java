package core.interfaces;

import java.util.Set;

public interface STBGraph<V extends STBTerminal, E extends STBEdge> extends STBComponent {

    Set<V> getAllVertexes();
    boolean addVertex(V vertex);
    boolean addVertexes(V... vertexes);
    boolean removeVertex(V vertex);
    boolean removeVertexes(V... vertexes);

    Set<E> getAllEdges();
    boolean addEdge(E edge);
    boolean addEdges(E... edges);
    boolean removeEdge(E edge);
    boolean removeEdges(E... edges);

}
