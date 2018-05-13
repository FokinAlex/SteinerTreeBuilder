package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class JfxApplication extends Application {

    private static final String MW_FXML = "MainWindow.fxml";
    private static final String SKIN_CSS = "stylesheets/stylesheets.css";

    private MainWindowController controller;
    private Parent rootElement;
    private Scene scene;

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        final InputStream stream = getClass().getResourceAsStream(MW_FXML);

        this.rootElement = loader.load(stream);
        this.controller = loader.getController();
        this.scene = new Scene(rootElement, 800, 600);
        this.scene.getStylesheets().add(getClass().getResource(SKIN_CSS).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JfxApplication.launch(args);
    }
}
