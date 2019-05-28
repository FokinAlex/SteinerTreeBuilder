package appi.ci;

import appi.ci.interfaces.AlgorithmType;
import appi.ci.interfaces.Project;
import core.exceptions.IllegalComponentException;
import core.implementations.GraphPage;
import core.implementations.ResultGraphPage;
import core.implementations.euclidean.EuclideanGraph;
import dai.ORLibraryAccess;
import gui.PagePane;
import gui.PagePoint;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import utils.iou.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectViewController extends TabPane {

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
    private BooleanProperty isntResultPage;

    private StringProperty selectedPointXProperty;
    private StringProperty selectedPointYProperty;
    private StringProperty firstPointXProperty;
    private StringProperty firstPointYProperty;
    private StringProperty secondPointXProperty;
    private StringProperty secondPointYProperty;

    private StringProperty edgeLengthStringProperty;
    private StringProperty graphWeightStringProperty;

    private ChangeListener<Number> edgeLengthListener = (observable, oldValue, newValue) -> edgeLengthStringProperty.set("Длина: " + newValue);
    private ChangeListener<Number> graphWeightListener = (observable, oldValue, newValue) -> graphWeightStringProperty.set("Вес: " + newValue);

    private ProjectTreeViewController projectTreeViewController;

    public ProjectViewController(Project project, TreeView treeView) {
        this.project = project;
        this.tabs = new HashMap<>();
        this.projectTreeViewController = new ProjectTreeViewController(treeView, project);
        this.projectTreeViewController.getClickedPageProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) this.addNewGraphPage((GraphPage) newValue);
        });
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
        Tab tab = this.addNewResultPage(this.currentPageController.getValue().getPage().nameProperty().get() + " | " + type.shortName());
        this.currentPageController.getValue().execute(type, this.tabs.get(tab), type.fullName());
        this.getSelectionModel().select(tab);
    }

    private void bindings(GraphPagePaneController target) {
        if(target != null) {
            this.selectedPointProperty.bind(target.selectedTerminalProperty());
            this.selectedEdgeProperty.bind(target.selectedEdgeProperty());
            target.edgeAdditionModeProperty().bind(this.edgeAdditionModeProperty);
            target.restoreProperties();
            target.edgeLengthProperty().addListener(this.edgeLengthListener);
            if (null != this.graphWeightStringProperty) this.graphWeightStringProperty.set("Вес: " + target.getPage().getGraph().getWeight());
            target.getPage().getGraph().weightProperty().addListener(this.graphWeightListener);
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
            target.getPage().getGraph().weightProperty().removeListener(this.graphWeightListener);
        }
    }

    public Tab addNewResultPage(String name) {
        try {
            ResultGraphPage page = new ResultGraphPage(new EuclideanGraph());
            if (name != null && !name.isEmpty()) page.nameProperty().set(name);
            this.project.addPage(page);
            return this.addNewGraphPage(page);
        } catch (IllegalComponentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tab addNewGraphPage(String name) {
        try {
            GraphPage page = new GraphPage(new EuclideanGraph());
            if (name != null && !name.isEmpty()) page.nameProperty().set(name);
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
        Tab tab = new Tab(page.nameProperty().get());
        tab.setOnClosed(event -> this._closeTab(tab));
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
        this.projectTreeViewController.addItem(page);
        return tab;
    }

    public void closeTab(Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (null != handler) handler.handle(null);
    }

    private boolean _closeTab(Tab tab) {
        this.projectTreeViewController.closeItem(this.tabs.get(tab).getPage());
        this.tabs.remove(tab);
        return this.getTabs().remove(tab);
    }

    public boolean removeTab(Tab tab) {
        this.projectTreeViewController.removeItem(this.tabs.get(tab).getPage());
        return this.getTabs().remove(tab) & this.project.removePage(this.tabs.remove(tab).getPage());
    }

    public boolean renameCurrentPage(String name) {
        if (name != null && !name.isEmpty()) {
            this.currentTab.setText(name);
            this.currentPageController.getValue().getPage().nameProperty().set(name);
            return true;
        }
        return false;
    }

    public Map<String, String> getResults() {
        Map result = null;
        if (currentPageController.getValue().getPage() instanceof ResultGraphPage)
            result = ((ResultGraphPage) currentPageController.getValue().getPage()).getProperties();
        return result;
    }

    public boolean importORLTask(File file) {
        try {
            Map<Integer, GraphPage> pages =  ORLibraryAccess.getTasks(file);
            pages.forEach((id, page) -> {
                this.addNewGraphPage(page);
                this.project.addPage(page);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean importORLResult(File file) {
        try {
            Map<Integer, ResultGraphPage> pages = ORLibraryAccess.getResults(file);
            pages.forEach((id, page) -> {
                this.addNewGraphPage(page);
                this.project.addPage(page);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getHeh(File file) {
        try {
            ORLibraryAccess.getHehs(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startAnal(AlgorithmType type, int count) {
        if (null != project.getFile()) {
            for (int i = 0; i < count; i++) {
                tabs.forEach((tab, controller) -> {
                    String result = controller.startAnal(type);
                    try {
                        FileUtils.append(
                                new File(project.getFile().getPath() + "/" + this.project.nameProperty().get() + type.shortName() + ".reslog"),
                                result
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void savePageAsReport() {
        if (null != project.getFile() && null != currentPageController.getValue()) {
            String result = currentPageController.getValue().saveAsReport();
            try {
                FileUtils.append(
                        new File(project.getFile().getPath() + "/" + this.project.nameProperty().get() + "reports.reslog"),
                        result
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void setIsResultPagePropertyFollower(BooleanProperty property) {
        this.isntResultPage = property;
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.isntResultPage.set(!(tabs.get(newValue).getPage() instanceof ResultGraphPage)));
    }

    public void setGraphWeightPropertyFollower(StringProperty property) {
        this.graphWeightStringProperty = property;
        if (null != this.currentPageController) this.graphWeightStringProperty.set("Вес: " + this.currentPageController.getValue().getPage().getGraph().getWeight());
    }

    public void setEdgeLengthPropertyFollower(StringProperty property) {
        this.edgeLengthStringProperty = property;
    }
}