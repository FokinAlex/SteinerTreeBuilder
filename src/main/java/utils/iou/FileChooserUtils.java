package utils.iou;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public final class FileChooserUtils {

    private static final FileChooser FILE_CHOOSER = new FileChooser();
    private static final DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();
    private static final FileChooser.ExtensionFilter STB_FILTER = new FileChooser.ExtensionFilter("STB-Project files (*.stb)", "*.stb");
    private static final FileChooser.ExtensionFilter JSON_FILTER = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");

    static {
        FILE_CHOOSER.getExtensionFilters().addAll(STB_FILTER, JSON_FILTER);
        FILE_CHOOSER.setTitle("Chooser project file");
        DIRECTORY_CHOOSER.setTitle("Choose project directory");
    }

    public static DirectoryChooser getProjectDirectoryChooser(File initialDirectory) {
        DIRECTORY_CHOOSER.setInitialDirectory(initialDirectory);
        return DIRECTORY_CHOOSER;
    }

    public static FileChooser getProjectFileChooser(File initialDirectory) {
        FILE_CHOOSER.setInitialDirectory(initialDirectory);
        return FILE_CHOOSER;
    }
}
