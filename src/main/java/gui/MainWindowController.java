package gui;

import appi.ci.ProjectController;
import appi.ci.interfaces.Project;
import core.implementations.GraphPage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import utils.DialogUtils;

public class MainWindowController {

    @FXML private MenuBar menuBar;

    // "File" menu:
    @FXML private Menu fileMenu;
    @FXML private MenuItem newProjectMI;
    @FXML private MenuItem openProjectMI;
    @FXML private MenuItem saveProjectMI;
    @FXML private MenuItem closeProjectMI;

    // "Edit" menu:
    @FXML private Menu editMenu;
    @FXML private MenuItem undoMI;
    @FXML private MenuItem redoMI;
    @FXML private MenuItem cutMI;
    @FXML private MenuItem copyMI;
    @FXML private MenuItem pasteMI;
    @FXML private MenuItem deleteMI;
    @FXML private MenuItem selectAllMI;

    // "Project" menu:
    @FXML private Menu projectMenu;
    @FXML private MenuItem addTerminalMI;
    // TODO: project menu

    // Panes:
    @FXML private ScrollPane projectViewPane;
    @FXML private AnchorPane projectPropertiesPane;
    @FXML private ScrollPane propertiesContainerPane;
    @FXML private AnchorPane terminalPropertiesPane;
    @FXML private TextField terminalXValue;
    @FXML private TextField terminalYValue;

    // Footer:
    @FXML private Label leftStatus;
    @FXML private Label rightStatus;

    // Actions:
    @FXML
    public void initialize() {
        this.terminalPropertiesPane.visibleProperty().setValue(false);
    }

    @FXML
    public boolean newProjectAction() {
        setLeftStatus("New project action");
        // TODO: user must chooser project type
        uploadProjectWorkspace(ProjectController.getNewProject(ProjectController.SIMPLE_PROJECT));
        // TODO: move bindings out of here
        setBindings();
        return true;
    }

    @FXML
    public boolean openProjectAction() {
        setLeftStatus("Open project action");
        // TODO: user must choose file
        uploadProjectWorkspace(ProjectController.openProject());
        setBindings();
        return true;
    }

    @FXML
    public boolean saveProjectAction() {
        setLeftStatus("Save project action");
        // TODO: choose project type
        return ProjectController.saveProject();
    }

    @FXML
    public boolean closeProjectAction() {
        setLeftStatus("Close project action");
        return ProjectController.closeProject();
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
    public boolean addTerminalAction() {
        setLeftStatus("Add node action");
        // TODO: block button if no project
        Pair<Double, Double> result = DialogUtils.showNewEuclideanTerminalDialog();
        if (result != null) {
            ((PagePane) projectViewPane.getContent()).addTerminal(result.getKey(), result.getValue());
            return true;
        }
        return false;
    }

    private void setBindings() {
        terminalPropertiesPane.visibleProperty()
                .bind(((PagePane) projectViewPane.getContent()).selectedTerminalProperty());
        ((PagePane) projectViewPane.getContent()).setSelectedTerminalXProperty(terminalXValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setSelectedTerminalYProperty(terminalYValue.textProperty());
    }


    private boolean uploadProjectWorkspace(Project project) {
        if (project != null) {
            clearProjectViewPane();
            this.projectViewPane.setContent(new PagePane((GraphPage) project.getCurrentPage()));
            return true;
        }
        return false;
    }

    private void clearProjectViewPane() {
        this.projectViewPane.setContent(null);
    }

    private void setLeftStatus(String leftStatus) {
        this.leftStatus.setText("Last action: " + leftStatus);
    }

    private void setRightStatus(String rightStatus) {
        this.rightStatus.setText(rightStatus);
    }
}
