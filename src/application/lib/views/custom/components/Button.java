package application.lib.views.custom.components;


import javafx.scene.Node;

/**
 * Created by Robin on 25/04/2017.
 */
public class Button extends javafx.scene.control.Button
{
    /**
     * Creates a button with an empty string for its label.
     */
    public Button ()
    {
    }

    /**
     * Creates a button with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public Button (String text)
    {
        super(text);
    }

    /**
     * Creates a button with the specified text and icon for its label.
     *
     * @param text    A text string for its label.
     * @param graphic the icon for its label.
     */
    public Button (String text, Node graphic)
    {
        super(text, graphic);
    }
}
