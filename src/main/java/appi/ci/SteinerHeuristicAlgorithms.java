package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import core.implementations.algorithms.steiner.GravitationalAlgorithm;
import core.implementations.algorithms.steiner.IncrementalOptimizationAlgorithm;
import core.implementations.algorithms.steiner.SmithLeeLiebmanAlgorithm;
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
        public String shortName() {
            return "IOA";
        }

        @Override
        public String fullName() {
            return "Incremental Optimization Algorithm";
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

    },
    GRAVITATIONAL_ALGORITHM {

        private GravitationalAlgorithm algorithm;

        @Override
        public STBAlgorithm getInstance(STBComponent argument) {
            this.algorithm = new GravitationalAlgorithm((EuclideanGraph) argument);
            return this.algorithm;
        }

        @Override
        public String shortName() {
            return "GA";
        }

        @Override
        public String fullName() {
            return "Gravitational Algorithm";
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
    },
    REDUCTIONAL_ALGORITHM {

        private SmithLeeLiebmanAlgorithm algorithm;

        @Override
        public STBAlgorithm getInstance(STBComponent argument) {
            this.algorithm = new SmithLeeLiebmanAlgorithm((EuclideanGraph) argument);
            return this.algorithm;
        }

        @Override
        public String shortName() {
            return "RA";
        }

        @Override
        public String fullName() {
            return "Reductional Algorithm";
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
        this.put("Алгоритм добавочной оптимизации", INCREMENTAL_OPTIMIZATION_ALGORITHM);
        this.put("Гравитационный алгоритм", GRAVITATIONAL_ALGORITHM);
        this.put("Редукционный алгоритм", REDUCTIONAL_ALGORITHM);
    }};
}
