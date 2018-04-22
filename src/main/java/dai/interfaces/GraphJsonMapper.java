package dai.interfaces;

import core.exceptions.IllegalComponentException;
import core.exceptions.IllegalLocationException;
import core.interfaces.STBGraph;
import org.json.simple.JSONObject;

public interface GraphJsonMapper<Json extends JSONObject, Graph extends STBGraph> {

    Graph JsonToGraph(Json json) throws IllegalComponentException, IllegalLocationException;
    Json GraphToJson(Graph graph);
}
