package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import core.implementations.algorithms.IncrementalOptimizationAlgorithm;
import core.implementations.euclidean.EuclideanGraph;
import core.interfaces.STBAlgorithm;
import core.interfaces.STBComponent;
import core.interfaces.STBHeuristicAlgorithm;
import javafx.beans.property.BooleanProperty;

import java.util.HashMap;
import java.util.Map;

public enum SteinerHeuristicAlgorithms implements STBHeuristicAlgorithm, AlgorithmType {
    INCREMENTAL_OPTIMIZATION_ALGORITHM {

        private IncrementalOptimizationAlgorithm algorithm;

        @Override
        public STBAlgorithm getInstance(STBComponent argument) {
            this.algorithm = new IncrementalOptimizationAlgorithm((EuclideanGraph) argument);
            return this.algorithm;
        }

        @Override
        public void run() {
            this.algorithm.run();
        }

        @Override
        public BooleanProperty inProgressProperty() {
            return this.algorithm.inProgressProperty();
        }

        @Override
        public EuclideanGraph getResult() {
            return this.algorithm.getResult();
        }

    };

    public static Map<String, SteinerHeuristicAlgorithms> ALGORITHMS = new HashMap<String, SteinerHeuristicAlgorithms>() {{
        this.put("Incremental optimization algorithm", INCREMENTAL_OPTIMIZATION_ALGORITHM);
    }};
}
