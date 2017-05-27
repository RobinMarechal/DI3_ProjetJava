package application.lib.views;

import javafx.scene.layout.Pane;

/**
 * Created by Robin on 25/04/2017.
 */
public abstract class BaseViewController
{
    protected Pane pane = new Pane();

    public Pane getPane ()
    {
        return pane;
    }
}
