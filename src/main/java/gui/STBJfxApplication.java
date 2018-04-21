package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class STBJfxApplication extends Application {

    private static final String MW_FXML = "MainWindow.fxml";

    private STBMainWindowController controller;
    private Parent rootElement;
    private Scene scene;

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        final InputStream stream = getClass().getResourceAsStream(MW_FXML);

        rootElement = loader.load(stream);
        controller = loader.getController();
        scene = new Scene(rootElement, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    //* TODO: add this to STBMain.java:
    public static void main(String[] args) {
        STBJfxApplication.launch(args);
    }
    // */
}
