package dai;

import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBGraph;
import core.types.STBEdgeType;
import core.types.STBTerminalType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public enum PageDataAccess {
    // TODO: this need some fragmentation when more then one
    EUCLIDEAN {
        @Override
        public JSONObject toJson(STBGraph graph) {
            JSONObject root = new JSONObject();
            JSONObject jGraph = new JSONObject();
            JSONArray jTerminals = new JSONArray();
            JSONArray jEdges = new JSONArray();

            root.put("graph", jGraph);
            jGraph.put("terminals", jTerminals);
            graph.getAllVertexes().forEach(terminal -> {
                JSONObject jTerminal = new JSONObject();
                JSONObject jTerminalsValues = new JSONObject();
                JSONObject jTerminalLocation = new JSONObject();
                jTerminalLocation.put("x", ((EuclideanTerminal) terminal).getLocation().getXProperty().get());
                jTerminalLocation.put("y", ((EuclideanTerminal) terminal).getLocation().getYProperty().get());
                jTerminalsValues.put("id", ((EuclideanTerminal) terminal).getId());
                jTerminalsValues.put("type", ((EuclideanTerminal) terminal).type.toString());
                jTerminalsValues.put("location", jTerminalLocation);
                jTerminal.put("terminal", jTerminalsValues);
                jTerminals.add(jTerminal);
            });
            jGraph.put("edges", jEdges);
            graph.getAllEdges().forEach(edge -> {
                JSONObject jEdge = new JSONObject();
                JSONObject jEdgesValues = new JSONObject();
                jEdgesValues.put("type", ((EuclideanEdge) edge).type.toString());
                graph.getAllVertexes().forEach(terminal -> {
                    if(((EuclideanEdge) edge).getFirstEndpoint().equals(terminal)) jEdgesValues.put("firstendpoint", ((EuclideanTerminal) terminal).getId());
                    if(((EuclideanEdge) edge).getSecondEndpoint().equals(terminal)) jEdgesValues.put("secondendpoint", ((EuclideanTerminal) terminal).getId());
                });
                jEdge.put("edge", jEdgesValues);
                jEdges.add(jEdge);
            });
            return root;
        }

        @Override
        public STBGraph fromJson(JSONObject json) {
            EuclideanGraph graph = new EuclideanGraph();
            Set<EuclideanTerminal> terminals = new HashSet<>();
            EuclideanTerminal currentTerminal;
            Set<EuclideanEdge> edges = new HashSet<>();

            JSONObject jGraph = (JSONObject) json.get("graph");
            JSONArray jTerminals = (JSONArray) jGraph.get("terminals");
            Iterator<JSONObject> iterator;
            iterator = jTerminals.iterator();
            while (iterator.hasNext()) {
                JSONObject jTerminal = (JSONObject) iterator.next().get("terminal");
                Long jTerminalId = (Long) jTerminal.get("id");
                String jTerminalType = (String) jTerminal.get("type");
                JSONObject jTerminalLocation = (JSONObject) jTerminal.get("location");
                Double jTerminalLocationX = (Double) jTerminalLocation.get("x");
                Double jTerminalLocationY = (Double) jTerminalLocation.get("y");
                currentTerminal = new EuclideanTerminal(new EuclideanLocation(jTerminalLocationX, jTerminalLocationY), jTerminalId);
                currentTerminal.type = STBTerminalType.valueOf(jTerminalType);
                terminals.add(currentTerminal);
            }
            JSONArray jEdges = (JSONArray) jGraph.get("edges");
            iterator = jEdges.iterator();
            while (iterator.hasNext()) {
                JSONObject jEdge = (JSONObject) iterator.next().get("edge");
                String jEdgeType = (String) jEdge.get("type");
                Integer jEdgeFirstEndpoint = ((Long) jEdge.get("firstendpoint")).intValue();
                Integer jEdgeSecondEndpoint = ((Long) jEdge.get("secondendpoint")).intValue();
                terminals.forEach(firstEndpoint ->
                    terminals.forEach(secondEndpoint -> {
                        if (firstEndpoint.getId() == jEdgeFirstEndpoint && secondEndpoint.getId() == jEdgeSecondEndpoint) {
                            EuclideanEdge currentEdge = new EuclideanEdge(firstEndpoint, secondEndpoint);
                            currentEdge.type = STBEdgeType.valueOf(jEdgeType);
                            edges.add(currentEdge);
                        }
                }));
            }
            graph.addVertexes(terminals);
            graph.addEdges(edges);
            return graph;
        }
    };

    public abstract JSONObject toJson(STBGraph graph);
    public abstract STBGraph fromJson(JSONObject json);
}