package appi.ci;

import appi.ci.implementations.SimpleProject;
import appi.ci.interfaces.Project;
import core.exceptions.IllegalComponentException;
import core.exceptions.IllegalLocationException;
import core.implementations.GraphPage;
import core.implementations.GraphSinglePageScheme;
import core.implementations.euclidean.EuclideanGraph;
import core.interfaces.STBGraph;
import dai.PageDataAccess;
import org.json.simple.parser.ParseException;
import utils.iou.FileChooserUtils;
import utils.iou.JsonUtils;

import java.io.File;
import java.io.IOException;

public enum ProjectController {
    SIMPLE_PROJECT;

    private static Project project;

    public static Project getNewProject(ProjectController type) {
        ProjectController.closeProject();
        switch (type) {
            case SIMPLE_PROJECT:
                try { // TODO: remove try
                    project = new SimpleProject(new GraphSinglePageScheme(new GraphPage(new EuclideanGraph())));
                } catch (IllegalComponentException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return project;
    };

    public static boolean saveProject() {
        if (project == null) return false;
        // TODO: ownerWindow = MainWindow ?
        File directory = FileChooserUtils.getProjectDirectoryChooser(project.getFile()).showDialog(null);
        if (directory != null) {
            try {
                // TODO: ask user if project already exist
                JsonUtils.writeJsonToFile(
                        PageDataAccess.EUCLIDEAN.toJson((STBGraph) project.getCurrentPage().getAllComponents().get(0)),
                        new File(directory.getPath() + "/" + project.hashCode() + ".stb")
                );
                project.setFile(directory);
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
        // TODO: ownerWindow = MainWindow ?
        File projectFile = FileChooserUtils.getProjectFileChooser(null).showOpenDialog(null);
        // TODO: read configuration and do the needful
        if (projectFile != null) {
            try {
                project = new SimpleProject(new GraphSinglePageScheme(new GraphPage(PageDataAccess.EUCLIDEAN.fromJson(JsonUtils.readJsonFromFile(projectFile)))));
                project.setFile(new File(projectFile.getParentFile().getPath()));
                return project;
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
        project = null;
        return true;
    }

    public static boolean hasProject() {
        return project != null;
    }
}
