package core.implementations.abstractions;

import core.interfaces.STBTerminal;
import core.interfaces.STBWeighted;

public abstract class AbstractWeightedGraph<Terminal extends STBTerminal, Edge extends AbstractWeightedEdge<Terminal, Weight>, Weight> extends AbstractGraph<Terminal, Edge> implements STBWeighted<Weight> {
}
