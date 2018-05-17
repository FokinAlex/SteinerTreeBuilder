package utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import utils.vuu.StringDoubleConverter;

import java.util.Optional;

public final class DialogUtils {

    private final static Dialog<Pair<Double, Double>> NEW_EUCLIDEAN_TERMINAL_DIALOG = new Dialog() {{
        this.setTitle("New terminal");
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        Label information = new Label("Insert new terminal location");
        pane.add(information, 0,0);
        Label xLabel = new Label("x:");
        pane.add(xLabel, 0,1);
        TextField xValue = new TextField();
        TextFormatter<Double> xFormatter = new TextFormatter<Double>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.FILTER);
        xValue.setTextFormatter(xFormatter);
        pane.add(xValue, 1, 1);
        Label yLabel = new Label("y:");
        pane.add(yLabel, 0,2);
        TextField yValue = new TextField();
        TextFormatter<Double> yFormatter = new TextFormatter<Double>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.FILTER);
        yValue.setTextFormatter(yFormatter);
        pane.add(yValue, 1, 2);

        ButtonType okType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        this.getDialogPane().setContent(pane);
        this.setResultConverter(button -> okType.equals(button) ? new Pair<>(Double.valueOf(xValue.getText()), Double.valueOf(yValue.getText())) : null);
    }};

    public static Pair<Double, Double> showNewEuclideanTerminalDialog() {
        Optional<Pair<Double, Double>> optional = NEW_EUCLIDEAN_TERMINAL_DIALOG.showAndWait();
        return optional.isPresent() ? optional.get() : null;
    }

    public static String showNameDialog() {
        Dialog<String> dialog = new Dialog();
        dialog.setTitle("Name dialog");
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        Label information = new Label("Write new name here:");
        pane.add(information, 0,0);

        TextField nameField = new TextField();
        pane.add(nameField, 1, 0);

        ButtonType okType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(pane);
        dialog.setResultConverter(button -> okType.equals(button) ? nameField.getText() : null);

        Optional<String> optional = dialog.showAndWait();
        return optional.isPresent() ? optional.get() : null;
    }
}
