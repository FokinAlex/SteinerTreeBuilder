package utils.vuu;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class StringDoubleConverter {

    public static final UnaryOperator<TextFormatter.Change> FILTER = change -> {
        String text = change.getControlNewText();
        return Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?").matcher(text).matches()? change : null;
    };

    public static final StringConverter<Double> CONVERTER = new StringConverter<Double>() {
        @Override
        public Double fromString(String value) {
            return (value.isEmpty() || "-".equals(value) || ".".equals(value) || "-.".equals(value)) ? 0.0 : Double.valueOf(value);
        }

        @Override
        public String toString(Double value) {
            return value.toString();
        }
    };

    public static String convert(Double value) {
        return CONVERTER.toString(value);
    }

    public static Double convert(String value) {
        return CONVERTER.fromString(value);
    }
}
