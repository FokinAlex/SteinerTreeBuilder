package appi.ci;

import appi.ci.interfaces.Project;
import core.interfaces.STBPage;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class ProjectTreeViewController {

    private TreeView treeView;
    private Map<STBPage, Pair<TreeItem, Boolean>> items;
    private Property<STBPage> clickedPage = new SimpleObjectProperty<>();

    public ProjectTreeViewController(TreeView treeView, Project project) {
        this.treeView = treeView;
        this.items = new HashMap<>();

        this.treeView.setRoot(null);

        treeView.setOnMouseClicked(event -> {
            if ((event.getClickCount() > 1) && this.treeView.getSelectionModel().getSelectedItem() != null) {
                this.items.entrySet().forEach(entry -> {
                    if (entry.getValue().getKey().equals(this.treeView.getSelectionModel().getSelectedItem()) && !entry.getValue().getValue()) {
                        this.clickedPage.setValue(entry.getKey());
                        this.items.put(entry.getKey(), new Pair<>(entry.getValue().getKey(), true));
                        this.clickedPage.setValue(null);
                }});
        }});

        TreeItem projectItem = new TreeItem();
        projectItem.valueProperty().bind(project.nameProperty());
        projectItem.setExpanded(true);
        this.treeView.setRoot(projectItem);
    }

    public void addItem(STBPage page) {
        if (!this.items.containsKey(page)) {
            TreeItem item = new TreeItem();
            item.valueProperty().bind(page.nameProperty());
            this.treeView.getRoot().getChildren().add(item);
            this.items.put(page, new Pair<>(item, true));
        }
    }

    public void closeItem(STBPage page) {
        if (this.items.get(page).getValue()) this.items.put(page, new Pair<>(this.items.get(page).getKey(), false));
    }

    public void removeItem(STBPage page) {
        this.treeView.getRoot().getChildren().remove((items.remove(page)).getKey());
    }

    public Property<STBPage> getClickedPageProperty() {
        return this.clickedPage;
    }
}
