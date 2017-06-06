package application;

import application.lib.views.Template;
import application.models.Company;
import application.network.Server;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Robin on 27/03/2017.
 */
public class ApplicationMain extends Application
{
    private static final int TIMEOUT = 200;

    public static void main (String args[])
    {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param window the primary stage for this application, onto which
     *               the application scene can be set. The primary stage will be embedded in
     *               the browser if the application was launched as an applet.
     *               Applications may create other stages, if needed, but they will not be
     *               primary stages and will not be embedded in the browser.
     */
    @Override
    public void start (Stage window) throws Exception
    {
        // Starting the server
        JSONObject json       = null;
        File       configFile = new File(new File(".").getCanonicalPath() + "/data/config/network.json");
        if (configFile.exists())
        {
            FileReader reader = new FileReader(configFile);
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(reader);
        }
        new Thread(new Server(json)).start();


        // Desierialization
        Company.getCompany().deserialize();


        // Creation of the window
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
