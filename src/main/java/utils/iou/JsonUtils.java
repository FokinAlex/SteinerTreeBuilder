package utils.iou;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public final class JsonUtils {

    private static final JSONParser PARSER = new JSONParser();
    private static final JsonUtils JSONUTILS_OBJECT = new JsonUtils();

    private JsonUtils() {}

    public static JsonBuilder initJsonBuilder(){
        return JSONUTILS_OBJECT.new JsonBuilder();
    }

    public class JsonBuilder {

        private JSONObject jsonObject;

        private JsonBuilder() {
            jsonObject = new JSONObject();
        }

        public JsonBuilder put(Object key, Object value) {
            this.jsonObject.put(key, value);
            return this;
        }

        public JSONObject build() {
            return this.jsonObject;
        }
    }

    public static void writeJsonToFile(JSONObject json, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json.toJSONString());
        fileWriter.flush();
    }

    public static JSONObject readJsonFromFile(File file) throws IOException, ParseException {
        FileReader fileReader = new FileReader(file);
        return (JSONObject) PARSER.parse(fileReader);
    }
}
