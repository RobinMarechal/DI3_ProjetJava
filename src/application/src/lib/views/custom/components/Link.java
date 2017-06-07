package lib.views.custom.components;

import lib.util.Closure;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Robin on 10/05/2017.
 * This class is a custom javafx {@link Label} allowing to simulate hyperlinks
 */
public class Link extends Label
{
    /** The closure to call on click */
    private Closure closure;

    /** The associated css class */
    private final static String CSS_CLASS_NAME = "link";

    /**
     * Closure parameter constructor <br>
     * The closure will be automatically called on click
     *
     * @param closure the closure to execute on click
     */
    public Link (@NotNull Closure closure)
    {
        this(closure, true);
    }

    /**
     * 2 parameters constructor <br>
     *
     * @param closure the closure to execute on click
     * @param automaticCall true if you want to call the closure automatically, false otherwise. This can be useful if you want to do
     *                      some stuff before triggering the link
     */
    public Link (@NotNull Closure closure, boolean automaticCall)
    {
        this.closure = closure;
        getStyleClass().add(CSS_CLASS_NAME);

        if (automaticCall)
        {
            prepareClickEvent();
        }
    }

    /**
     * Prepare the click event
     */
    private void prepareClickEvent ()
    {
        setOnMouseClicked(event -> trigger());
    }

    /**
     * Add a tooltip to the Label
     * @param text the tooltip text
     */
    public void setTooltipValue (String text)
    {
        setTooltip(new Tooltip(text));
    }

    /**
     * Remove the tooltip
     */
    public void removeTooltip ()
    {
        setTooltip(null);
    }

    /**
     * Execute the closure
     */
    public void trigger ()
    {
        try
        {
            closure.call();
        }
        catch (Exception e)
        {
            System.err.println("Error while triggering link...");
            e.printStackTrace();
        }
    }
}
