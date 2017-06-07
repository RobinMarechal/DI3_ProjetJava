import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Robin on 23/05/2017. <br>
 * Main class of the emulator
 */
public class EmulatorMain extends Application
{
    public static void main (String[] args)
    {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
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
        Emulator.launch(window);
    }
}
