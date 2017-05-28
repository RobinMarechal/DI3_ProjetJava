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
        File       configFile = new File("src/config/network.json");
        FileReader reader     = new FileReader(configFile);
        JSONParser parser     = new JSONParser();
        JSONObject json       = (JSONObject) parser.parse(reader);

        Client.start(window, json);
    }
}
