package core.implementations;

import core.exceptions.IllegalComponentException;
import core.interfaces.STBComponent;
import core.interfaces.STBGraph;

import java.util.HashMap;
import java.util.Map;

public class ResultGraphPage<Graph extends STBGraph, Component extends STBComponent> extends GraphPage<Graph, Component> {

    private Map<String, String> properties;

    public ResultGraphPage(Graph graph) throws IllegalComponentException {
        super(graph);
        this.properties = new HashMap<>();
    }

    public void putProperty(String property, String value) {
        this.properties.put(property, value);
    }

    public void putProperties(Map<String, String> properties) {
        this.properties.putAll(properties);
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }
}
