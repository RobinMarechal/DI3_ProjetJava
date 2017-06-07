package lib.views;

import javafx.scene.layout.Pane;

/**
 * Created by Robin on 25/04/2017.<br>
 * This class is the parent of every ViewController, which are the intermediate between a controller and a view <br>
 * Their role is to build a view by passing/binding data to UI components <br>
 * The view's global layout is made by fxml file
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
