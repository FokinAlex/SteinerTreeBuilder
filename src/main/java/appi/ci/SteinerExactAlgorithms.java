package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import core.interfaces.STBAlgorithm;
import core.interfaces.STBComponent;
import core.interfaces.STBExactAlgorithm;
import javafx.beans.property.BooleanProperty;

import java.util.HashMap;
import java.util.Map;

public enum SteinerExactAlgorithms implements STBExactAlgorithm, AlgorithmType {
    VOID {
        @Override
        public void run() { }

        @Override
        public BooleanProperty inProgressProperty() {
            return null;
        }

        @Override
        public Object getResult() {
            return null;
        }

        @Override
        public STBAlgorithm getInstance(STBComponent argument) {
            return null;
        }

        @Override
        public String shortName() {
            return null;
        }
    };

    public static Map<String, SteinerExactAlgorithms> ALGORITHMS = new HashMap<String, SteinerExactAlgorithms>() {{
    }};
}
