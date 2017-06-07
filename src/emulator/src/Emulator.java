import com.sun.istack.internal.NotNull;
import javafx.stage.Stage;
import network.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Robin on 07/06/2017. <br>
 * Emulator application launcher
 */
public class Emulator
{
    /**
     * Launch the emulator program
     *
     * @param window the main window
     * @throws Exception
     */
    public static void launch (@NotNull Stage window) throws Exception
    {
        JSONObject json = null;
        try
        {
            File configFile = new File(new File(".").getCanonicalPath() + "/data/config/clientConfig.json");
            if (configFile.exists())
            {
                FileReader reader = new FileReader(configFile);
                JSONParser parser = new JSONParser();
                json = (JSONObject) parser.parse(reader);
                reader.close();
            }
        }
        catch (Exception e)
        {
            json = null;
        }

        String serializationDataPath = new File(".").getCanonicalPath() + "/data/serialized/emulator.sez";

        Client.start(window, json, serializationDataPath);
    }
}
