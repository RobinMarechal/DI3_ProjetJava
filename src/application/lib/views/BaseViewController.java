package application.lib.views;

import javafx.scene.layout.Pane;

/**
 * Created by Robin on 25/04/2017.<br/>
 * This class is the parent of every ViewController, which are the intermediate between a controller and a view
 */
public abstract class BaseViewController
{
    /**
     * The pane which is displayed
     */
    protected Pane pane = new Pane();

    /**
     * Get the pane
     * @return the pane
     */
    public Pane getPane ()
    {
        return pane;
    }
}
