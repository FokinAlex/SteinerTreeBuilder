package utils.iou;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public final class FileChooserUtils {

    private static final FileChooser PROJECT_FILE_CHOOSER = new FileChooser();
    private static final FileChooser ORL_FILE_CHOOSER = new FileChooser();
    private static final DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();
    private static final FileChooser.ExtensionFilter STB_FILTER = new FileChooser.ExtensionFilter("STB-Project files (*.stb)", "*.stb");
    private static final FileChooser.ExtensionFilter JSON_FILTER = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
    private static final FileChooser.ExtensionFilter TXT_FILTER = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");

    static {
        PROJECT_FILE_CHOOSER.getExtensionFilters().addAll(STB_FILTER, JSON_FILTER);
        PROJECT_FILE_CHOOSER.setTitle("Chooser project file");
        ORL_FILE_CHOOSER.getExtensionFilters().add(TXT_FILTER);
        ORL_FILE_CHOOSER.setTitle("Choose OR-Library file");
        DIRECTORY_CHOOSER.setTitle("Choose project directory");
    }

    public static DirectoryChooser getProjectDirectoryChooser(File initialDirectory) {
        DIRECTORY_CHOOSER.setInitialDirectory(initialDirectory);
        return DIRECTORY_CHOOSER;
    }

    public static FileChooser getProjectFileChooser(File initialDirectory) {
        PROJECT_FILE_CHOOSER.setInitialDirectory(initialDirectory);
        return PROJECT_FILE_CHOOSER;
    }

    public static FileChooser getORLibraryFileChooser(File initialDirectory) {
        ORL_FILE_CHOOSER.setInitialDirectory(initialDirectory);
        return ORL_FILE_CHOOSER;
    }
}
