package gui;

import appi.ci.GraphPagePaneController;
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

    private GraphPagePaneController graphPageController;

    // Actions:
    @FXML
    public void initialize() {
        this.terminalPropertiesPane.visibleProperty().set(false);
        this.edgePropertiesPane.visibleProperty().set(false);
        SteinerExactAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.graphPageController.execute(type));
            this.steinerExactAlgorithms.getItems().add(menuItem);
        });
        SteinerHeuristicAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.graphPageController.execute(type));
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
            this.graphPageController.addPoint(result.getKey(), result.getValue());
            return true;
        }
        return false;
    }

    @FXML
    public boolean addEdgeAction() {
        setLeftStatus("Add edge action");
        this.graphPageController.edgeAdditionMode(this.addEdgeRMI.isSelected());
        return true;
    }

    @FXML
    private boolean deleteTerminalAction() {
        setLeftStatus("Delete terminal action");
        this.graphPageController.deleteSelectedPoint();
        return true;
    }

    @FXML
    private boolean deleteEdgeAction() {
        setLeftStatus("Delete edge action");
        this.graphPageController.deleteSelectedEdge();
        return true;
    }

    private void setBindings() {
        terminalPropertiesPane.visibleProperty()
                .bind(graphPageController.selectedTerminalProperty());
        edgePropertiesPane.visibleProperty()
                .bind(graphPageController.selectedEdgeProperty());
        graphPageController.setSelectedPointXPropertyFollower(terminalXValue.textProperty());
        graphPageController.setSelectedPointYPropertyFollower(terminalYValue.textProperty());
        graphPageController.setFirstPointXPropertyFollower(edgeFirstEndpointXValue.textProperty());
        graphPageController.setFirstPointYPropertyFollower(edgeFirstEndpointYValue.textProperty());
        graphPageController.setSecondPointXPropertyFollower(edgeSecondEndpointXValue.textProperty());
        graphPageController.setSecondPointYPropertyFollower(edgeSecondEndpointYValue.textProperty());
        graphPageController.setEdgeLengthPropertyFollower(edgeLenghtValue.textProperty());
    }


    private boolean uploadProjectWorkspace(Project project) {
        if (project != null) {
            this.projectViewPane.setContent(null);
            PagePane pagePane = new PagePane();
            this.graphPageController = new GraphPagePaneController(pagePane, (GraphPage) project.getCurrentPage());
            this.projectViewPane.setContent(pagePane);
            this.algorithmsMenu.setDisable(false);
            this.projectMenu.setDisable(false);
            return true;
        }
        return false;
    }

    private void setLeftStatus(String leftStatus) {
        this.leftStatus.setText("Last action: " + leftStatus);
    }

    private void setRightStatus(String rightStatus) {
        this.rightStatus.setText(rightStatus);
    }
}
