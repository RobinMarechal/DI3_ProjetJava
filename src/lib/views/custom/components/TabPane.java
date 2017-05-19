package lib.views.custom.components;

import javafx.scene.control.Tab;

/**
 * Created by Robin on 25/04/2017.
 */
public class TabPane extends javafx.scene.control.TabPane
{
    /**
     * Constructs a new TabPane.
     */
    public TabPane ()
    {
    }

    /**
     * Constructs a new TabPane with the given tabs set to show.
     *
     * @param tabs The {@link Tab tabs} to display inside the TabPane.
     * @since JavaFX 8u40
     */
    public TabPane (Tab... tabs)
    {
        super(tabs);
    }
}
