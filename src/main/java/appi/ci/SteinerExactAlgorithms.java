package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import core.implementations.algorithms.TestAlgorithm;
import core.interfaces.STBComponent;
import core.interfaces.STBExactAlgorithm;
import core.interfaces.STBGraph;
import javafx.beans.property.BooleanProperty;

import java.util.HashMap;
import java.util.Map;

public enum SteinerExactAlgorithms implements STBExactAlgorithm, AlgorithmType {
    TEST_ALGORITHM {

        private TestAlgorithm algorithm;

        @Override
        public STBExactAlgorithm getInstance(STBComponent argument) {
            this.algorithm = new TestAlgorithm((STBGraph) argument);
            return this.algorithm;
        }

        @Override
        public void run() {
            algorithm.run();
        }

        @Override
        public BooleanProperty inProgressProperty() {
            return this.algorithm.inProgressProperty();
        }
    };

    public static Map<String, SteinerExactAlgorithms> ALGORITHMS = new HashMap<String, SteinerExactAlgorithms>() {{
        this.put("Test Algorithm", TEST_ALGORITHM);
    }};
}
