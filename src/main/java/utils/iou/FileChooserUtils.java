package utils.iou;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public final class FileChooserUtils {

    private static final FileChooser FILE_CHOOSER = new FileChooser();
    private static final DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();
    private static final FileChooser.ExtensionFilter STB_FILTER = new FileChooser.ExtensionFilter("STB-Project files (*.stb)", "*.stb");
    private static final FileChooser.ExtensionFilter JSON_FILTER = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");

    public static DirectoryChooser getProjectDirectoryChooser(File initialDirectory) {
        DIRECTORY_CHOOSER.setInitialDirectory(initialDirectory);
        DIRECTORY_CHOOSER.setTitle("Choose project directory");
        return DIRECTORY_CHOOSER;
    }

    public static FileChooser getProjectFileChooser(File initialDirectory) {
        FILE_CHOOSER.setInitialDirectory(initialDirectory);
        FILE_CHOOSER.setTitle("Chooser project file");
        FILE_CHOOSER.getExtensionFilters().clear();
        FILE_CHOOSER.getExtensionFilters().add(STB_FILTER);
        FILE_CHOOSER.getExtensionFilters().add(JSON_FILTER);
        return FILE_CHOOSER;
    }
}
