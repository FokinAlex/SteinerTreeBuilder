package core.implementations.abstractions;

import core.interfaces.STBTerminal;
import core.interfaces.STBWeighted;

public abstract class AbstractWeightedEdge<Terminal extends STBTerminal, Weight> extends AbstractEdge<Terminal> implements STBWeighted<Weight> {
}