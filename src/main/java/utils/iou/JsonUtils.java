package utils.iou;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public final class JsonUtils {

    private static final JSONParser PARSER = new JSONParser();
   
    private JsonUtils() {}

    public static void writeJsonToFile(JSONObject json, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }

    public static JSONObject readJsonFromFile(File file) throws IOException, ParseException {
        FileReader fileReader = new FileReader(file);
        JSONObject result = (JSONObject) PARSER.parse(fileReader);
        fileReader.close();
        return result;
    }
}
