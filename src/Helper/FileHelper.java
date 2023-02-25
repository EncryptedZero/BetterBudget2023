package Helper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHelper {

    // Define the filename
    private static final String FILENAME = "data.json";

    /**
     * Reads the JSON file and returns a JSONObject.
     *
     * @return JSONObject representing the data in the file.
     */
    public static JSONObject readJSONFile() {
        JSONObject jsonObject = null;
        try (FileReader reader = new FileReader(FILENAME)) {
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(reader);
            System.out.println("File Successfully Read.");
        } 
        catch (IOException | ParseException e) {
            System.out.println("File Unsuccessfully Read.");
        }
        return jsonObject;
    }

    /**
     * Writes the data in a JSONObject to the JSON file.
     *
     * @param jsonObject JSONObject containing the data to write.
     */
    public static void writeJSONFile(JSONObject jsonObject) {
        try (FileWriter file = new FileWriter(FILENAME)) {
            file.write(jsonObject.toJSONString());
            System.out.println("File Successfully Wrote.");
        } 
        catch (IOException e) {
            System.out.println("File Unsuccessfully Wrote.");
        }
    }

    // method to check if data.json exists in the current directory
    public static boolean dataFileExists() {
        File file = new File("data.json");
        return file.exists() && !file.isDirectory();
    }
}

