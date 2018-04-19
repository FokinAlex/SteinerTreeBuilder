package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class STBApplication extends Application {

    @Override
    public void start(final Stage stage) {

        Pane pane = new Pane();
        Label label = new Label("Heh?");
        Scene scene = new Scene(pane,500, 500);

        stage.setScene(scene);
        stage.setTitle("STB");
        stage.show();
    }

    //* TODO: add this to STBMain.java:
    public static void main(String[] args) {
        STBApplication.launch(args);
    }
    // */
}
