package dai.implementations;

import core.exceptions.IllegalComponentException;
import core.exceptions.IllegalLocationException;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.types.STBEdgeType;
import core.types.STBTerminalType;
import dai.interfaces.GraphJsonMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class EuclideanGraphJsonMapper<Json extends JSONObject, Graph extends EuclideanGraph> implements GraphJsonMapper<Json, Graph> {

    @Override
    public Graph JsonToGraph(Json root) throws IllegalComponentException, IllegalLocationException {
        EuclideanGraph graph = new EuclideanGraph();
        Map<Integer, EuclideanTerminal> terminals = new HashMap<>();
        EuclideanTerminal currentTerminal;
        Set<EuclideanEdge> edges = new HashSet<>();
        EuclideanEdge currentEdge;

        JSONObject jGraph = (JSONObject) root.get("graph");
        JSONArray jTerminals = (JSONArray) jGraph.get("terminals");
        Iterator<JSONObject> iterator;
        iterator = jTerminals.iterator();
        while (iterator.hasNext()) {
            JSONObject jTerminal = (JSONObject) iterator.next().get("terminal");
            Integer jTerminalId = ((Long) jTerminal.get("id")).intValue();
            String jTerminalType = (String) jTerminal.get("type");
            JSONObject jTerminalLocation = (JSONObject) jTerminal.get("location");
            Double jTerminalLocationX = (Double) jTerminalLocation.get("x");
            Double jTerminalLocationY = (Double) jTerminalLocation.get("y");
            currentTerminal = new EuclideanTerminal(new EuclideanLocation(jTerminalLocationX, jTerminalLocationY));
            currentTerminal.type = STBTerminalType.valueOf(jTerminalType);
            terminals.put(jTerminalId, currentTerminal);
        }
        JSONArray jEdges = (JSONArray) jGraph.get("edges");
        iterator = jEdges.iterator();
        while (iterator.hasNext()) {
            JSONObject jEdge = (JSONObject) iterator.next().get("edge");
            String jEdgeType = (String) jEdge.get("type");
            Integer jEdgeFirstEndpoint = ((Long) jEdge.get("firstendpoint")).intValue();
            Integer jEdgeSecondEndpoint = ((Long) jEdge.get("secondendpoint")).intValue();
            currentEdge = new EuclideanEdge(terminals.get(jEdgeFirstEndpoint), terminals.get(jEdgeSecondEndpoint));
            currentEdge.type = STBEdgeType.valueOf(jEdgeType);
            edges.add(currentEdge);
        }
        graph.addVertexes(terminals.values().stream().collect(Collectors.toSet()));
        graph.addEdges(edges);
        return (Graph) graph;
    }

    @Override
    public Json GraphToJson(Graph graph) {
        JSONObject root = new JSONObject();
        JSONObject jGraph = new JSONObject();
        JSONArray jTerminals = new JSONArray();
        Map<Integer, EuclideanTerminal> terminals = new HashMap<>();
        graph.getAllVertexes().forEach(terminal -> terminals.put(terminals.size(), (EuclideanTerminal) terminal));
        JSONArray jEdges = new JSONArray();
        Set<EuclideanEdge> edges = new HashSet<>();
        graph.getAllEdges().forEach(edge -> edges.add((EuclideanEdge) edge));

        root.put("graph", jGraph);
        jGraph.put("terminals", jTerminals);
        terminals.forEach((id, terminal) -> {
            JSONObject jTerminal = new JSONObject();
            JSONObject jTerminalsValues = new JSONObject();
            JSONObject jTerminalLocation = new JSONObject();
            jTerminalLocation.put("x", terminal.getLocation().getX());
            jTerminalLocation.put("y", terminal.getLocation().getY());
            jTerminalsValues.put("id", id);
            jTerminalsValues.put("type", terminal.type.toString());
            jTerminalsValues.put("location", jTerminalLocation);
            jTerminal.put("terminal", jTerminalsValues);
            jTerminals.add(jTerminal);
        });
        jGraph.put("edges", jEdges);
        edges.forEach(edge -> {
            JSONObject jEdge = new JSONObject();
            JSONObject jEdgesValues = new JSONObject();
            jEdgesValues.put("type", edge.type.toString());
            terminals.forEach((id, terminal) -> {
                if(edge.getFirstEndpoint().equals(terminal)) jEdgesValues.put("firstendpoint", id);
                if(edge.getSecondEndpoint().equals(terminal)) jEdgesValues.put("secondendpoint", id);
            });
            jEdge.put("edge", jEdgesValues);
            jEdges.add(jEdge);
        });
        return (Json) root;
    }
}
