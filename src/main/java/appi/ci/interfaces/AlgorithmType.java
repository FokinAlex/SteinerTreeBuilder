package appi.ci.interfaces;

import core.interfaces.STBAlgorithm;
import core.interfaces.STBComponent;

public interface AlgorithmType {

    STBAlgorithm getInstance(STBComponent argument);
    String shortName();
}
