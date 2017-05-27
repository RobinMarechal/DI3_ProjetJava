package application.lib.views.custom.components;


import javafx.scene.Node;

/**
 * Created by Robin on 03/05/2017.
 */
public class Label extends javafx.scene.control.Label
{
    /**
     * Creates an empty label
     */
    public Label ()
    {
    }

    /**
     * Creates Label with supplied text.
     *
     * @param text null text is treated as the empty string
     */
    public Label (String text)
    {
        super(text);
    }

    /**
     * Creates a Label with the supplied text and graphic.
     *
     * @param text    null text is treated as the empty string
     * @param graphic a null graphic is acceptable
     */
    public Label (String text, Node graphic)
    {
        super(text, graphic);
    }
}
