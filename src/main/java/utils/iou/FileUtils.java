package utils.iou;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileUtils {

    private FileUtils() {}

    public static void append(File file, String line) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(line + "\n");
        fileWriter.close();
    }
}
