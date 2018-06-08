package appi.ci;

import appi.ci.implementations.SimpleProject;
import appi.ci.interfaces.Project;
import core.exceptions.IllegalComponentException;
import core.implementations.GraphMultiPageScheme;
import core.implementations.GraphPage;
import core.implementations.euclidean.EuclideanGraph;
import core.interfaces.STBPage;
import dai.ProjectDataAccess;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.json.simple.parser.ParseException;
import utils.DialogUtils;
import utils.FileChooserUtils;
import utils.iou.JsonUtils;

import java.io.File;
import java.io.IOException;

public enum ProjectController {
    SIMPLE_PROJECT;

    private static Property<Project> project;
    private static BooleanProperty hasProject;

    static {
        project = new SimpleObjectProperty<>();
        hasProject = new SimpleBooleanProperty();
        project.addListener((observable, oldValue, newValue) -> hasProject.set(newValue != null));
    }

    public static Project getNewProject(ProjectController type) {
        ProjectController.closeProject();
        String name = DialogUtils.showNameDialog("");
        if (null == name || name.isEmpty()) return null;
        switch (type) {
            case SIMPLE_PROJECT:
                GraphMultiPageScheme scheme = new GraphMultiPageScheme();
                try {
                    scheme.addPage(new GraphPage(new EuclideanGraph()));
                    ((STBPage) scheme.getPages().get(0)).nameProperty().set("page #1");
                    scheme.setCurrentPage((GraphPage) scheme.getPages().get(0));
                } catch (IllegalComponentException e) {
                    e.printStackTrace();
                }
                project.setValue(new SimpleProject(scheme, name));
                break;
            default:
                break;
        }
        return project.getValue();
    };

    public static boolean saveProject() {
        if (project.getValue() == null) return false;
        File directory = FileChooserUtils.getProjectDirectoryChooser(project.getValue().getFile()).showDialog(null);
        if (directory != null) {
            try {
                // TODO: ask user if project already exist
                JsonUtils.writeJsonToFile(
                        ProjectDataAccess.SIMPLE.toJson(project.getValue()),
                        new File(directory.getPath() + "/" + project.getValue().nameProperty().get() + ".stb")
                );
                project.getValue().setFile(directory);
                return true;
            } catch (IOException e) {
                // TODO: show some information to user
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Project openProject() {
        ProjectController.closeProject();
        File projectFile = FileChooserUtils.getProjectFileChooser(null).showOpenDialog(null);
        // TODO: read configuration and do the needful
        if (projectFile != null) {
            try {
                // TODO: check project type
                project.setValue(ProjectDataAccess.SIMPLE.fromJson(JsonUtils.readJsonFromFile(projectFile)));
                project.getValue().setFile(new File(projectFile.getParentFile().getPath()));
                return project.getValue();
            } catch (IOException e) {
                // TODO: show some information to user
                e.printStackTrace();
            } catch (ParseException e) {
                // TODO: show some information to user
                e.printStackTrace();
            } catch (IllegalComponentException e) {
                // TODO: show some information to user
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean closeProject() {
        // TODO: ask user to save project if it has changes
        project.setValue(null);
        return true;
    }

    public static void renameProject(String name) {
        if (name != null && !name.isEmpty()) project.getValue().nameProperty().set(name);
    }

    public static File getDirectory() {
        return project.getValue().getFile();
    }

    public static String getProjectName() {
        return project.getValue().nameProperty().get();
    }

    public static BooleanProperty hasProject() {
        return hasProject;
    }
}
