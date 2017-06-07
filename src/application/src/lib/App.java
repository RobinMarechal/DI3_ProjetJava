package lib;

import javafx.stage.Stage;
import lib.views.Template;
import models.Company;
import network.Server;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Robin on 07/06/2017.
 */
public class App
{
    public static void deserializeCompany ()
    {
        Company.getCompany().deserialize();
    }

    public static void startServer ()
    {
        JSONObject json = null;
        try
        {
            File configFile = new File(Helper.serverConfigFilePath());
            if (configFile.exists())
            {
                FileReader reader = new FileReader(configFile);
                JSONParser parser = new JSONParser();
                json = (JSONObject) parser.parse(reader);
            }
        }
        catch (Exception e)
        {
            json = null;
        }
        new Thread(new Server(json)).start();
    }

    public static void startMainWindow (@NotNull Stage window)
    {
        window.setTitle("Pointeuse");
        window.setScene(Template.getInstance().getScene());
        window.setResizable(false);
        window.sizeToScene();
        window.show();
        window.setOnCloseRequest(event ->
        {
            Company.getCompany().serialiaze();
            System.exit(1);
        });
    }
}
