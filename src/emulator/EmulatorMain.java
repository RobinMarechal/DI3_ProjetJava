package emulator;

import emulator.network.Client;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Robin on 23/05/2017.
 */
public class EmulatorMain extends Application
{
    public static void main (String[] args)
    {
        launch(args);
    }

    @Override
    public void start (Stage window) throws Exception
    {
        JSONObject json = null;
        File configFile = new File(new File(".").getCanonicalPath() + "/data/config/network.json");
        if(configFile.exists())
        {
            FileReader reader = new FileReader(configFile);
            JSONParser parser = new JSONParser();
            json   = (JSONObject) parser.parse(reader);
            reader.close();
        }

        String serializationDataPath = new File(".").getCanonicalPath() + "/data/serialized/emulator.ser";

        Client.start(window, json, serializationDataPath);
    }
}
