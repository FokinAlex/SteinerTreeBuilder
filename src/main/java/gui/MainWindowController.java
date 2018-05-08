package gui;

import appi.ci.ProjectController;
import appi.ci.SteinerExactAlgorithms;
import appi.ci.SteinerHeuristicAlgorithms;
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
    @FXML private RadioMenuItem addEdgeRMI;

    // "Algorithms" menu:
    @FXML private Menu algorithmsMenu;
    @FXML private Menu steinerExactAlgorithms;
    @FXML private Menu steinerHeuristicAlgorithms;

    // Panes:
    @FXML private ScrollPane projectViewPane;
    @FXML private AnchorPane projectPropertiesPane;
    @FXML private ScrollPane terminalPropertiesPane;
    @FXML private TextField terminalXValue;
    @FXML private TextField terminalYValue;

    @FXML private ScrollPane edgePropertiesPane;
    @FXML private TextField edgeFirstEndpointXValue;
    @FXML private TextField edgeFirstEndpointYValue;
    @FXML private TextField edgeSecondEndpointXValue;
    @FXML private TextField edgeSecondEndpointYValue;
    @FXML private Label edgeLenghtValue;

    @FXML private Button deleteTerminal;
    @FXML private Button deleteEdge;

    // Footer:
    @FXML private Label leftStatus;
    @FXML private Label rightStatus;

    // Actions:
    @FXML
    public void initialize() {
        this.terminalPropertiesPane.visibleProperty().set(false);
        this.edgePropertiesPane.visibleProperty().set(false);
        SteinerExactAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> ((PagePane) this.projectViewPane.getContent()).execute(type));
            this.steinerExactAlgorithms.getItems().add(menuItem);
        });
        SteinerHeuristicAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> ((PagePane) this.projectViewPane.getContent()).execute(type));
            this.steinerHeuristicAlgorithms.getItems().add(menuItem);
        });
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

    @FXML
    public boolean addEdgeAction() {
        setLeftStatus("Add edge action");
        ((PagePane) projectViewPane.getContent()).edgeAdditionMode(this.addEdgeRMI.isSelected());
        return true;
    }

    @FXML
    private boolean deleteTerminalAction() {
        setLeftStatus("Delete terminal action");
        ((PagePane) projectViewPane.getContent()).deleteSelectedTerminal();
        return true;
    }

    @FXML
    private boolean deleteEdgeAction() {
        setLeftStatus("Delete edge action");
        ((PagePane) projectViewPane.getContent()).deleteSelectedEdge();
        return true;
    }

    private void setBindings() {
        terminalPropertiesPane.visibleProperty()
                .bind(((PagePane) projectViewPane.getContent()).selectedTerminalProperty());
        edgePropertiesPane.visibleProperty()
                .bind(((PagePane) projectViewPane.getContent()).selectedEdgeProperty());
        ((PagePane) projectViewPane.getContent()).setSelectedTerminalXProperty(terminalXValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setSelectedTerminalYProperty(terminalYValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setFirstTerminalXProperty(edgeFirstEndpointXValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setFirstTerminalYProperty(edgeFirstEndpointYValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setSecondTerminalXProperty(edgeSecondEndpointXValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setSecondTerminalYProperty(edgeSecondEndpointYValue.textProperty());
        ((PagePane) projectViewPane.getContent()).setEdgeLengthProperty(edgeLenghtValue.textProperty());
    }


    private boolean uploadProjectWorkspace(Project project) {
        if (project != null) {
            clearProjectViewPane();
            this.projectViewPane.setContent(new PagePane((GraphPage) project.getCurrentPage()));
            this.algorithmsMenu.setDisable(false);
            this.projectMenu.setDisable(false);
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
