package gui;

import appi.ci.*;
import appi.ci.interfaces.Project;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import utils.DialogUtils;
import utils.FileChooserUtils;
import utils.vuu.StringDoubleConverter;

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
    @FXML private MenuItem renameProjectMI;
    @FXML private MenuItem projectPropertiesMI;

    // "Page" menu:
    @FXML private Menu pageMenu;
    @FXML private MenuItem addTerminalMI;
    @FXML private RadioMenuItem addTerminalsRMI;
    @FXML private MenuItem addEdgeMI;
    @FXML private RadioMenuItem addEdgesRMI;
    @FXML private MenuItem renamePageMI;
    @FXML private MenuItem closePageMI;
    @FXML private MenuItem removePageMI;
    @FXML private Menu resultPageMenu;

    // "Algorithms" menu:
    @FXML private Menu algorithmsMenu;
    @FXML private Menu steinerExactAlgorithms;
    @FXML private Menu steinerHeuristicAlgorithms;

    // Panes:
    @FXML private BorderPane mainPane;

    @FXML private ToolBar toolBar;
    @FXML private ToggleButton cursorTB;
    @FXML private ToggleButton addTerminalTB;
    @FXML private ToggleButton addEdgeTB;

    @FXML private AnchorPane projectViewPane;

    @FXML private AnchorPane projectPropertiesPane;

    @FXML private TreeView projectTV;

    @FXML private Label graphWeight;

    @FXML private ScrollPane terminalPropertiesPane;
    @FXML private TextField terminalXValue;
    @FXML private TextField terminalYValue;
    @FXML private Button deleteTerminal;

    @FXML private ScrollPane edgePropertiesPane;
    @FXML private TextField edgeFirstEndpointXValue;
    @FXML private TextField edgeFirstEndpointYValue;
    @FXML private TextField edgeSecondEndpointXValue;
    @FXML private TextField edgeSecondEndpointYValue;
    @FXML private Label edgeLenghtValue;
    @FXML private Button deleteEdge;

    // Footer:
    @FXML private Label leftStatus;
    @FXML private Label rightStatus;

    private ProjectViewController projectViewController;

    // Actions:
    @FXML
    public void initialize() {
        this.terminalPropertiesPane.visibleProperty().set(false);
        this.edgePropertiesPane.visibleProperty().set(false);
        this.projectMenu.disableProperty().bind(ProjectController.hasProject().not());
        this.algorithmsMenu.disableProperty().bind(pageMenu.disableProperty());
        this.graphWeight.visibleProperty().bind(pageMenu.disableProperty().not());
        this.toolBar.visibleProperty().bind(pageMenu.disableProperty().not());
        this.projectTV.visibleProperty().bind(ProjectController.hasProject());
        this.mainPane.visibleProperty().bind(ProjectController.hasProject());
        terminalXValue.setTextFormatter(new TextFormatter<>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.POSITIVE_FILTER));
        terminalYValue.setTextFormatter(new TextFormatter<>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.POSITIVE_FILTER));
        edgeFirstEndpointXValue.setTextFormatter(new TextFormatter<>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.POSITIVE_FILTER));
        edgeFirstEndpointYValue.setTextFormatter(new TextFormatter<>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.POSITIVE_FILTER));
        edgeSecondEndpointXValue.setTextFormatter(new TextFormatter<>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.POSITIVE_FILTER));
        edgeSecondEndpointYValue.setTextFormatter(new TextFormatter<>(StringDoubleConverter.CONVERTER, .0, StringDoubleConverter.POSITIVE_FILTER));
        SteinerExactAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.projectViewController.execute(type));
            this.steinerExactAlgorithms.getItems().add(menuItem);
        });
        SteinerHeuristicAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.projectViewController.execute(type));
            this.steinerHeuristicAlgorithms.getItems().add(menuItem);
        });
        OtherAlgorithms.ALGORITHMS.forEach((name, type) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> this.projectViewController.execute(type));
            this.algorithmsMenu.getItems().add(menuItem);
        });
    }

    @FXML
    public boolean newProjectAction() {
        setLeftStatus("New project action");
        // TODO: user must choose project type
        if (uploadProjectWorkspace(ProjectController.getNewProject(ProjectController.SIMPLE_PROJECT))) {
            setBindings();
            return true;
        }
        return false;
    }

    @FXML
    public boolean openProjectAction() {
        setLeftStatus("Open project action");
        if (uploadProjectWorkspace(ProjectController.openProject())) {
            setBindings();
            return true;
        }
        return false;
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
        this.projectTV.setRoot(null);
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
        this.projectViewController.addNewGraphPage(DialogUtils.showNameDialog(""));
        return true;
    }

    @FXML
    public boolean renameProjectAction() {
        setLeftStatus("Rename project action");
        String name = DialogUtils.showNameDialog(ProjectController.getProjectName());
        ProjectController.renameProject(name);
        return true;
    }

    @FXML
    public boolean showProjectPropertiesPageAction() {
        setLeftStatus("Show project properties action");
        return false;
    }

    @FXML
    public boolean addTerminalAction() {
        setLeftStatus("Add terminal action");
        Pair<Double, Double> result = DialogUtils.showNewEuclideanTerminalDialog();
        if (result != null) {
            this.projectViewController.getCurrentPageController().addPoint(result.getKey(), result.getValue());
            return true;
        }
        return false;
    }

    @FXML
    public boolean addTerminalsAction() {
        setLeftStatus("Add terminals action");
        this.addTerminalTB.setSelected(true);
        this.addTerminalsRMI.setSelected(true);
        this.cursorTB.setSelected(false);
        this.addEdgeTB.setSelected(false);
        this.addEdgesRMI.setSelected(false);
        this.projectViewController.setSingleEdgeAdditionModeProperty(false);
        this.projectViewController.setTerminalAdditionModeProperty(this.addTerminalsRMI.isSelected());
        return true;
    }

    @FXML
    public boolean addEdgeAction() {
        setLeftStatus("Add edge action");
        this.cursorTB.setSelected(false);
        this.addTerminalTB.setSelected(false);
        this.addTerminalsRMI.setSelected(false);
        this.projectViewController.setSingleEdgeAdditionModeProperty(true);
        return true;
    }

    @FXML
    public boolean addEdgesAction() {
        setLeftStatus("Add edges action");
        this.addEdgeTB.setSelected(true);
        this.addEdgesRMI.setSelected(true);
        this.cursorTB.setSelected(false);
        this.addTerminalTB.setSelected(false);
        this.addTerminalsRMI.setSelected(false);
        this.projectViewController.setEdgeAdditionModeProperty(this.addEdgesRMI.isSelected());
        return true;
    }

    @FXML
    public boolean renamePageAction() {
        setLeftStatus("Rename page action");
        return this.projectViewController.renameCurrentPage(DialogUtils.showNameDialog(this.projectViewController.getCurrentPageController().getPage().nameProperty().getValue()));
    }

    @FXML
    public boolean closePageAction() {
        setLeftStatus("Close page action");
        this.projectViewController.closeTab(this.projectViewController.getSelectionModel().getSelectedItem());
        return true;
    }

    @FXML
    public boolean removePageAction() {
        setLeftStatus("Remove page action");
        return this.projectViewController.removeTab(this.projectViewController.getSelectionModel().getSelectedItem());
    }

    @FXML
    public boolean showResultsAction() {
        setLeftStatus("Show result action");
        DialogUtils.showResultsDialog(this.projectViewController.getResults());
        return true;
    }

    @FXML
    public boolean importORLTasksAction() {
        setLeftStatus("Import ORL tasks action");
        return this.projectViewController.importORLTask(FileChooserUtils.getORLibraryFileChooser(ProjectController.getDirectory()).showOpenDialog(null));
    }

    @FXML
    public boolean importORLResultsAction() {
        setLeftStatus("Import ORL results action");
        return this.projectViewController.importORLResult(FileChooserUtils.getORLibraryFileChooser(ProjectController.getDirectory()).showOpenDialog(null));
    }

    @FXML
    public boolean getHehAction() {
        setLeftStatus("Get heh action");
        return this.projectViewController.getHeh(FileChooserUtils.getORLibraryFileChooser(ProjectController.getDirectory()).showOpenDialog(null));
    }

    @FXML
    private boolean deleteTerminalAction() {
        setLeftStatus("Delete terminal action");
        this.projectViewController.getCurrentPageController().deleteSelectedPoint();
        return true;
    }

    @FXML
    private boolean deleteEdgeAction() {
        setLeftStatus("Delete edge action");
        this.projectViewController.getCurrentPageController().deleteSelectedEdge();
        return true;
    }

    @FXML
    private boolean cursorToggleAction() {
        this.addEdgeTB.setSelected(false);
        this.addTerminalTB.setSelected(false);
        this.addTerminalsRMI.setSelected(false);
        this.addEdgesRMI.setSelected(false);
        this.projectViewController.setEdgeAdditionModeProperty(false);
        this.projectViewController.setSingleEdgeAdditionModeProperty(false);
        this.projectViewController.setTerminalAdditionModeProperty(false);
        return true;
    }

    @FXML
    private boolean startAnalAction() {
        this.projectViewController.startAnal(SteinerHeuristicAlgorithms.REDUCTIONAL_ALGORITHM, 1);
        this.projectViewController.startAnal(SteinerHeuristicAlgorithms.GRAVITATIONAL_ALGORITHM, 1);
        this.projectViewController.startAnal(SteinerHeuristicAlgorithms.INCREMENTAL_OPTIMIZATION_ALGORITHM, 1);
        return true;
    }

    @FXML
    public boolean savePageAsReportAction() {
        this.projectViewController.savePageAsReport();
        return true;
    }

    private boolean uploadProjectWorkspace(Project project) {
        if (project != null) {
            this.projectViewPane.getChildren().removeIf(node -> true);
            this.projectViewController = new ProjectViewController(project, this.projectTV);
            this.projectViewPane.getChildren().add(this.projectViewController);
            return true;
        }
        return false;
    }

    private void setBindings() {
        this.terminalPropertiesPane.visibleProperty().bind(this.projectViewController.selectedPointProperty());
        this.edgePropertiesPane.visibleProperty().bind(this.projectViewController.selectedEdgeProperty());
        this.projectViewController.setHasCurrentPagePropertyFollower(this.pageMenu.disableProperty());
        this.projectViewController.setIsResultPagePropertyFollower(this.resultPageMenu.disableProperty());
        this.projectViewController.setSelectedPointXPropertyFollower(this.terminalXValue.textProperty());
        this.projectViewController.setSelectedPointYPropertyFollower(this.terminalYValue.textProperty());
        this.projectViewController.setFirstPointXPropertyFollower(this.edgeFirstEndpointXValue.textProperty());
        this.projectViewController.setFirstPointYPropertyFollower(this.edgeFirstEndpointYValue.textProperty());
        this.projectViewController.setSecondPointXPropertyFollower(this.edgeSecondEndpointXValue.textProperty());
        this.projectViewController.setSecondPointYPropertyFollower(this.edgeSecondEndpointYValue.textProperty());
        this.projectViewController.setEdgeLengthPropertyFollower(this.edgeLenghtValue.textProperty());
        this.projectViewController.setGraphWeightPropertyFollower(this.graphWeight.textProperty());
    }

    private void setLeftStatus(String leftStatus) {
        this.leftStatus.setText("Last action: " + leftStatus);
    }

    private void setRightStatus(String rightStatus) {
        this.rightStatus.setText(rightStatus);
    }
}
