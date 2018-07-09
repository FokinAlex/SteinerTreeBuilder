package utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import utils.vuu.StringDoubleConverter;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public final class DialogUtils {

    private final static Dialog<Pair<Double, Double>> NEW_EUCLIDEAN_TERMINAL_DIALOG = new Dialog() {{
        this.setTitle("Новый терминал");
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        Label information = new Label("Введите координаты вершины");
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

    public static String showNameDialog(String oldValue) {
        Dialog<String> dialog = new Dialog();
        dialog.setTitle("Выбор имени");
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        Label information = new Label("Введите имя:");
        pane.add(information, 0,0);

        TextField nameField = new TextField(oldValue);
        pane.add(nameField, 1, 0);

        ButtonType okType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(pane);
        dialog.setResultConverter(button -> okType.equals(button) ? nameField.getText() : null);

        Optional<String> optional = dialog.showAndWait();
        return optional.isPresent() ? optional.get() : null;
    }

    public static void showResultsDialog(Map<String, String> values) {
        Dialog<String> dialog = new Dialog();
        dialog.setTitle("Результаты");
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            pane.add(new Label((String) entry.getKey()), 0, i);
            pane.add(new Label((String) entry.getValue()), 1, i++);
        }
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY);
        dialog.getDialogPane().setContent(pane);

        dialog.showAndWait();
    }
}
