package gui;

import appi.ci.*;
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
    @FXML private MenuItem addPageMI;
    @FXML private MenuItem projectPropertiesMI;

    // "Page" menu:
    @FXML private Menu pageMenu;
    @FXML private MenuItem addTerminalMI;
    @FXML private MenuItem addEdgeMI;
    @FXML private RadioMenuItem addEdgesRMI;
    @FXML private MenuItem renamePageMI;
    @FXML private MenuItem closePageMI;
    @FXML private MenuItem removePageMI;

    // "Algorithms" menu:
    @FXML private Menu algorithmsMenu;
    @FXML private Menu steinerExactAlgorithms;
    @FXML private Menu steinerHeuristicAlgorithms;

    // Panes:
    @FXML private AnchorPane projectViewPane;
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

    private ProjectPagesController pagesController;

    // Actions:
    @FXML
    public void initialize() {
        this.terminalPropertiesPane.visibleProperty().set(false);
        this.edgePropertiesPane.visibleProperty().set(false);
        this.projectMenu.disableProperty().bind(ProjectController.hasProject().not());
        this.algorithmsMenu.disableProperty().bind(pageMenu.disableProperty());
        SteinerExactAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.pagesController.execute(type));
            this.steinerExactAlgorithms.getItems().add(menuItem);
        });
        SteinerHeuristicAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.pagesController.execute(type));
            this.steinerHeuristicAlgorithms.getItems().add(menuItem);
        });
    }

    @FXML
    public boolean newProjectAction() {
        setLeftStatus("New project action");
        // TODO: user must chooser project type
        uploadProjectWorkspace(ProjectController.getNewProject(ProjectController.SIMPLE_PROJECT));
        setBindings();
        return true;
    }

    @FXML
    public boolean openProjectAction() {
        setLeftStatus("Open project action");
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
        this.terminalPropertiesPane.visibleProperty().unbind();
        this.terminalPropertiesPane.setVisible(false);
        this.edgePropertiesPane.visibleProperty().unbind();
        this.edgePropertiesPane.setVisible(false);
        if (ProjectController.closeProject()) projectViewPane.getChildren().remove(0);
        return true;
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
    public boolean addPageAction() {
        setLeftStatus("Add page action");
        this.pagesController.addNewGraphPage(DialogUtils.showNameDialog());
        return true;
    }

    @FXML
    public boolean showProjectPropertiesPageAction() {
        setLeftStatus("Show project properties action");
        return false;
    }

    @FXML
    public boolean addTerminalAction() {
        setLeftStatus("Add node action");
        // TODO: block button if no project
        Pair<Double, Double> result = DialogUtils.showNewEuclideanTerminalDialog();
        if (result != null) {
            this.pagesController.getCurrentPageController().addPoint(result.getKey(), result.getValue());
            return true;
        }
        return false;
    }

    @FXML
    public boolean addEdgeAction() {
        setLeftStatus("Add edge action");
        this.pagesController.setSingleEdgeAdditionModeProperty(true);
        this.addEdgesRMI.setSelected(false);
        return true;
    }

    @FXML
    public boolean addEdgesAction() {
        setLeftStatus("Add edges action");
        this.pagesController.setEdgeAdditionModeProperty(this.addEdgesRMI.isSelected());
        return true;
    }

    @FXML
    public boolean renamePageAction() {
        setLeftStatus("Rename page action");
        return this.pagesController.renameCurrentPage(DialogUtils.showNameDialog());
    }

    @FXML
    public boolean closePageAction() {
        setLeftStatus("Close page action");
        return this.pagesController.getTabs().remove(this.pagesController.getSelectionModel().getSelectedItem());
    }

    @FXML
    public boolean removePageAction() {
        setLeftStatus("Remove page action");
        return this.pagesController.removePage(this.pagesController.getSelectionModel().getSelectedItem());
    }

    @FXML
    private boolean deleteTerminalAction() {
        setLeftStatus("Delete terminal action");
        this.pagesController.getCurrentPageController().deleteSelectedPoint();
        return true;
    }

    @FXML
    private boolean deleteEdgeAction() {
        setLeftStatus("Delete edge action");
        this.pagesController.getCurrentPageController().deleteSelectedEdge();
        return true;
    }

    private boolean uploadProjectWorkspace(Project project) {
        if (project != null) {
            this.projectViewPane.getChildren().removeIf(node -> true);
            // TODO reorganize it
            this.pagesController = new ProjectPagesController(project);
            this.projectViewPane.getChildren().add(this.pagesController);
            return true;
        }
        return false;
    }

    private void setBindings() {
        this.terminalPropertiesPane.visibleProperty().bind(this.pagesController.selectedPointProperty());
        this.edgePropertiesPane.visibleProperty().bind(this.pagesController.selectedEdgeProperty());
        this.pagesController.setHasCurrentPagePropertyFollower(this.pageMenu.disableProperty());
        this.pagesController.setSelectedPointXPropertyFollower(this.terminalXValue.textProperty());
        this.pagesController.setSelectedPointYPropertyFollower(this.terminalYValue.textProperty());
        this.pagesController.setFirstPointXPropertyFollower(this.edgeFirstEndpointXValue.textProperty());
        this.pagesController.setFirstPointYPropertyFollower(this.edgeFirstEndpointYValue.textProperty());
        this.pagesController.setSecondPointXPropertyFollower(this.edgeSecondEndpointXValue.textProperty());
        this.pagesController.setSecondPointYPropertyFollower(this.edgeSecondEndpointYValue.textProperty());
        this.pagesController.setEdgeLengthPropertyFollower(this.edgeLenghtValue.textProperty());
    }

    private void setLeftStatus(String leftStatus) {
        this.leftStatus.setText("Last action: " + leftStatus);
    }

    private void setRightStatus(String rightStatus) {
        this.rightStatus.setText(rightStatus);
    }
}
