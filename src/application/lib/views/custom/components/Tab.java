package application.lib.views.custom.components;

import javafx.scene.Node;

/**
 * Created by Robin on 03/05/2017.
 */
public class Tab extends javafx.scene.control.Tab
{
    /**
     * Creates a tab with no title.
     */
    public Tab ()
    {
    }

    /**
     * Creates a tab with a text title.
     *
     * @param text The title of the tab.
     */
    public Tab (String text)
    {
        super(text);
    }

    /**
     * Creates a tab with a text title and the specified content node.
     *
     * @param text    The title of the tab.
     * @param content The content of the tab.
     * @since JavaFX 8u40
     */
    public Tab (String text, Node content)
    {
        super(text, content);
    }
}
