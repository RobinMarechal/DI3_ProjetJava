package emulator;

import emulator.tcp.Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Robin on 23/05/2017.
 */
public class ClientMain extends Application
{
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 8080;

    public static void main (String[] args)
    {
        launch(args);
    }

    @Override
    public void start (Stage window) throws Exception
    {
        Client.start(window, HOSTNAME, PORT);
    }
}
