package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class STBMainWindowController {

    @FXML MenuBar menuBar;

    // "File" menu:
    @FXML Menu fileMenu;
    @FXML MenuItem newProjectMI;
    @FXML MenuItem openProjectMI;
    @FXML MenuItem saveProjectMI;
    @FXML MenuItem closeProjectMI;

    // "Edit" menu:
    @FXML Menu editMenu;
    @FXML MenuItem undoMI;
    @FXML MenuItem redoMI;
    @FXML MenuItem cutMI;
    @FXML MenuItem copyMI;
    @FXML MenuItem pasteMI;
    @FXML MenuItem deleteMI;
    @FXML MenuItem selectAllMI;

    // "Project" menu:
    @FXML Menu projectMenu;
    @FXML MenuItem addNodeMI;
    // TODO: project menu

    // Panes:
    @FXML ScrollPane projectViewPane;
    @FXML AnchorPane projectEmptyPane;
    @FXML AnchorPane projectDetailsPane;

    // Footer:
    @FXML Label leftStatus;
    @FXML Label rightStatus;


    // Actions:
    @FXML
    public boolean newProjectAction() {
        setLeftStatus("New project action");
        return false;
    }

    @FXML
    public boolean openProjectAction() {
        setLeftStatus("Open project action");
        return false;
    }

    @FXML
    public boolean saveProjectAction() {
        setLeftStatus("Save project action");
        return false;
    }

    @FXML
    public boolean closeProjectAction() {
        setLeftStatus("Close project action");
        return false;
    }

    @FXML
    public boolean undoAction() {
        setLeftStatus("Undo action");
        return false;
    }

    @FXML
    public boolean redoAction() {
        setLeftStatus("Redo action");
        return false;
    }

    @FXML
    public boolean cutAction() {
        setLeftStatus("Cut action");
        return false;
    }

    @FXML
    public boolean copyAction() {
        setLeftStatus("Copy action");
        return false;
    }

    @FXML
    public boolean pasteAction() {
        setLeftStatus("Paste action");
        return false;
    }

    @FXML
    public boolean deleteAction() {
        setLeftStatus("Delete action");
        return false;
    }

    @FXML
    public boolean selectAllAction() {
        setLeftStatus("Select all action");
        return false;
    }

    @FXML
    public boolean addNodeAction() {
        setLeftStatus("Add node action");
        return false;
    }



    // TODO: do something else here



    private void setLeftStatus(String leftStatus) {
        this.leftStatus.setText("Last action: " + leftStatus);
    }

    private void setRightStatus(String rightStatus) {
        this.rightStatus.setText(rightStatus);
    }
}
