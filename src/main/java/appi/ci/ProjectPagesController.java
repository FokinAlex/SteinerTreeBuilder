package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import appi.ci.interfaces.Project;
import core.exceptions.IllegalComponentException;
import core.implementations.GraphPage;
import core.implementations.euclidean.EuclideanGraph;
import gui.PagePane;
import gui.PagePoint;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class ProjectPagesController extends TabPane {

    private Project project;
    private Map<Tab, GraphPagePaneController> tabs;

    private Tab currentTab;
    private Property<GraphPagePaneController> currentPageController = new SimpleObjectProperty<>();

    private BooleanProperty selectedPointProperty = new SimpleBooleanProperty();
    private BooleanProperty selectedEdgeProperty = new SimpleBooleanProperty();
    private BooleanProperty terminalAddtionModeProperty = new SimpleBooleanProperty();
    private BooleanProperty edgeAdditionModeProperty = new SimpleBooleanProperty();
    private BooleanProperty singleEdgeAdditionModeProperty = new SimpleBooleanProperty();
    private BooleanProperty hasntCurrentPage;

    private StringProperty selectedPointXProperty;
    private StringProperty selectedPointYProperty;
    private StringProperty firstPointXProperty;
    private StringProperty firstPointYProperty;
    private StringProperty secondPointXProperty;
    private StringProperty secondPointYProperty;

    private StringProperty edgeLengthStringProperty;

    private ChangeListener<Number> edgeLengthListener = (observable, oldValue, newValue) -> edgeLengthStringProperty.set("Length: " + ((Math.round((Double) newValue * 1000) / 1000.)));

    public ProjectPagesController(Project project) {
        this.project = project;
        this.tabs = new HashMap<>();
        AnchorPane.setTopAnchor(this, 0.);
        AnchorPane.setLeftAnchor(this, 0.);
        AnchorPane.setRightAnchor(this, 0.);
        AnchorPane.setBottomAnchor(this, 0.);
        this.project.getPages().forEach(page -> {
            if (page instanceof GraphPage) this.addNewGraphPage((GraphPage) page);
        });
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.currentTab = newValue;
            this.unbindings(this.tabs.get(oldValue));
            this.currentPageController.setValue(this.tabs.get(newValue));
            this.bindings(tabs.get(newValue));
        });
        this.currentPageController.setValue(this.tabs.get(this.getSelectionModel().getSelectedItem()));
        this.currentTab = this.getSelectionModel().getSelectedItem();
        this.bindings(currentPageController.getValue());
    }

    public void execute(AlgorithmType type) {
        Tab tab = this.addNewGraphPage(this.currentPageController.getValue().getPage().getName() + " IOA result");
        this.currentPageController.getValue().execute(type, this.tabs.get(tab));
        this.getSelectionModel().select(tab);
    }

    private void bindings(GraphPagePaneController target) {
        if(target != null) {
            this.selectedPointProperty.bind(target.selectedTerminalProperty());
            this.selectedEdgeProperty.bind(target.selectedEdgeProperty());
            target.edgeAdditionModeProperty().bind(this.edgeAdditionModeProperty);
            target.restoreProperties();
            target.edgeLengthProperty().addListener(this.edgeLengthListener);
        }
    }

    private void unbindings(GraphPagePaneController target) {
        if (target != null) {
            this.singleEdgeAdditionModeProperty.set(false);
            this.selectedPointProperty.unbind();
            this.selectedEdgeProperty.unbind();
            target.edgeAdditionModeProperty().unbind();
            target.clearProperties();
            target.edgeLengthProperty().removeListener(this.edgeLengthListener);
        }
    }

    public Tab addNewGraphPage(String name) {
        try {
            //TODO: ask type
            GraphPage page = new GraphPage(new EuclideanGraph());
            if (name != null && !name.isEmpty()) page.setName(name);
            this.project.addPage(page);
            return this.addNewGraphPage(page);
        } catch (IllegalComponentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Tab addNewGraphPage(GraphPage page) {
        PagePane pageView = new PagePane();
        GraphPagePaneController pageController = new GraphPagePaneController(pageView, page);
        pageController.setTerminalAdditionModePropertyFollower(this.terminalAddtionModeProperty);
        pageController.setSingleEdgeAdditionModePropertyFollower(this.singleEdgeAdditionModeProperty);
        pageController.setSelectedPointXPropertyFollower(this.selectedPointXProperty);
        pageController.setSelectedPointYPropertyFollower(this.selectedPointYProperty);
        pageController.setFirstPointXPropertyFollower(this.firstPointXProperty);
        pageController.setFirstPointYPropertyFollower(this.firstPointYProperty);
        pageController.setSecondPointXPropertyFollower(this.secondPointXProperty);
        pageController.setSecondPointYPropertyFollower(this.secondPointYProperty);
        Tab tab = new Tab(page.getName());
        ScrollPane wrap = new ScrollPane();
        wrap.setOnMouseClicked(event -> {
            if (this.terminalAddtionModeProperty.get())
                pageController.addPoint(
                        event.getX() - PagePoint.HALO_SIZE / 2,
                        event.getY() - PagePoint.HALO_SIZE / 2
                );
        });
        wrap.setContent(pageView);
        tab.setContent(wrap);
        tabs.put(tab, pageController);
        this.getTabs().add(tab);
        return tab;
    }

    public boolean removePage(Tab tab) {
        this.getSelectionModel().selectFirst();
        GraphPagePaneController controller = this.tabs.remove(tab);
        return this.getTabs().remove(tab) & this.project.removePage(controller.getPage());
    }

    public boolean renameCurrentPage(String name) {
        if (name != null && !name.isEmpty()) {
            this.currentTab.setText(name);
            this.currentPageController.getValue().getPage().setName(name);
            return true;
        }
        return false;
    }

    public GraphPagePaneController getCurrentPageController() {
        return currentPageController.getValue();
    }

    public BooleanProperty selectedPointProperty() {
        return selectedPointProperty;
    }

    public BooleanProperty selectedEdgeProperty() {
        return selectedEdgeProperty;
    }

    public void setTerminalAdditionModeProperty(boolean value) {
        if (value) {
            this.setSingleEdgeAdditionModeProperty(false);
            this.setEdgeAdditionModeProperty(false);
        }
        this.terminalAddtionModeProperty.set(value);
    }

    public void setEdgeAdditionModeProperty(boolean value) {
        if (value) {
            this.setTerminalAdditionModeProperty(false);
            this.setSingleEdgeAdditionModeProperty(false);
        }
        this.edgeAdditionModeProperty.set(value);
    }

    public void setSingleEdgeAdditionModeProperty(boolean value) {
        if (value) {
            this.setTerminalAdditionModeProperty(false);
            this.setEdgeAdditionModeProperty(false);
        }
        this.singleEdgeAdditionModeProperty.set(value);
    }

    public void setSelectedPointXPropertyFollower(StringProperty property) {
        this.selectedPointXProperty = property;
        this.tabs.forEach((tab, controller) -> controller.setSelectedPointXPropertyFollower(property));
    }

    public void setSelectedPointYPropertyFollower(StringProperty property) {
        this.selectedPointYProperty = property;
        this.tabs.forEach((tab, controller) -> controller.setSelectedPointYPropertyFollower(property));
    }

    public void setFirstPointXPropertyFollower(StringProperty property) {
        this.firstPointXProperty = property;
        this.tabs.forEach((tab, controller) -> controller.setFirstPointXPropertyFollower(property));
    }

    public void setFirstPointYPropertyFollower(StringProperty property) {
        this.firstPointYProperty = property;
        this.tabs.forEach((tab, controller) -> controller.setFirstPointYPropertyFollower(property));
    }

    public void setSecondPointXPropertyFollower(StringProperty property) {
        this.secondPointXProperty = property;
        this.tabs.forEach((tab, controller) -> controller.setSecondPointXPropertyFollower(property));
    }

    public void setSecondPointYPropertyFollower(StringProperty property) {
        this.secondPointYProperty = property;
        this.tabs.forEach((tab, controller) -> controller.setSecondPointYPropertyFollower(property));
    }

    public void setHasCurrentPagePropertyFollower(BooleanProperty property) {
        this.hasntCurrentPage = property;
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.hasntCurrentPage.set(newValue == null));
        if (this.getSelectionModel().getSelectedItem() != null) this.hasntCurrentPage.set(false);
    }

    public void setEdgeLengthPropertyFollower(StringProperty property) {
        this.edgeLengthStringProperty = property;
    }
}