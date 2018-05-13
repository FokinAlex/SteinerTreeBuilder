package utils;

import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;

public final class IdUtils {

    private static STBGraph lastGraph;
    private static STBGraph graph;
    private static long _lastId;

    public static void setGraph(STBGraph graph) {
        IdUtils.graph = graph;
    }

    public static long getTerminalId() {
        return getTerminalId(graph);
    }

    public static long getTerminalId(STBGraph graph) {
        if (graph.equals(lastGraph)) {
            _lastId = getNextTerminalId(_lastId);
        } else {
            lastGraph = graph;
            _lastId = getNextTerminalId(1);
        }
        return _lastId++;
    }

    private static long getNextTerminalId(long lastId) {
        boolean changed;
        while (true) {
            changed = false;
            for (Object terminal : lastGraph.getAllVertexes()) {
                if (((STBTerminal) terminal).getId() == lastId) {
                    lastId++;
                    changed = true;
                    break;
                }
            }
            if (!changed) break;
        }
        return lastId;
    }
}
