package core.implementations.euclidean;

import core.exceptions.IllegalComponentException;
import core.exceptions.IllegalLocationException;
import core.implementations.abstractions.AbstractGraph;
import core.interfaces.STBGraph;
import dai.PageDataAccess;
import org.json.simple.parser.ParseException;
import utils.iou.JsonUtils;

import java.io.File;
import java.io.IOException;
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
        EuclideanGraph clone = null;
        try {
            File file = new File("temp.stb");
            JsonUtils.writeJsonToFile(PageDataAccess.EUCLIDEAN.toJson(this), file);
            clone = (EuclideanGraph) PageDataAccess.EUCLIDEAN.fromJson(JsonUtils.readJsonFromFile(file));
        } catch (IOException | ParseException | IllegalComponentException | IllegalLocationException e) {
            e.printStackTrace();
        } finally {
            // TODO: clear file here
        }
        return clone;
    }
}
