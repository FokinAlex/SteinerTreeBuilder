package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import core.implementations.algorithms.other.DelaunayTriangulationAlgorithm;
import core.implementations.algorithms.other.KruskallAlgorithm;
import core.implementations.euclidean.EuclideanGraph;
import core.interfaces.STBAlgorithm;
import core.interfaces.STBComponent;
import javafx.beans.property.BooleanProperty;

import java.util.HashMap;
import java.util.Map;

public enum OtherAlgorithms implements STBAlgorithm, AlgorithmType {
    KRUSKALL_ALGORITHM {

        private KruskallAlgorithm algorithm;

        @Override
        public STBAlgorithm getInstance(STBComponent argument) {
            this.algorithm = new KruskallAlgorithm((EuclideanGraph) argument);
            return this.algorithm;
        }

        @Override
        public String shortName() {
            return "MST";
        }

        @Override
        public String fullName() {
            return "Kruskall MST Algorithm";
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
    DELAUNAY_TRIANGULATION {

        private DelaunayTriangulationAlgorithm algorithm;

        @Override
        public STBAlgorithm getInstance(STBComponent argument) {
            this.algorithm = new DelaunayTriangulationAlgorithm((EuclideanGraph) argument);
            return this.algorithm;
        }

        @Override
        public String shortName() {
            return "Triangulation";
        }

        @Override
        public String fullName() {
            return "Delaunay Triangulation Algorithm";
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

    public static Map<String, OtherAlgorithms> ALGORITHMS = new HashMap<String, OtherAlgorithms>() {{
        this.put("Kruskall Algorithm", KRUSKALL_ALGORITHM);
        this.put("Delaunay Triangulation", DELAUNAY_TRIANGULATION);
    }};
}