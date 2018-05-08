package core.implementations.abstractions;

import core.implementations.euclidean.EuclideanGraph;

public abstract class AbstractSteinerTreeAlgorithm<Argument extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractAlgorithm<Argument, Result> {

    protected Result result;

    public AbstractSteinerTreeAlgorithm(Argument argument) {
        super(argument);
    }

    @Override
    public Result getResult() {
        return result;
    }
}
