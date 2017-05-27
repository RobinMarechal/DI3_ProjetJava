package application.lib.views.custom.components;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Created by Robin on 01/05/2017.
 */
public class Table extends TableView
{
    /**
     * Creates a default TableView control with no content.
     * <p>
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     */
    public Table ()
    {
    }

    /**
     * Creates a TableView with the content provided in the items ObservableList.
     * This also sets up an observer such that any changes to the items list
     * will be immediately reflected in the TableView itself.
     * <p>
     * <p>Refer to the {@link TableView} class documentation for details on the
     * default state of other properties.
     *
     * @param items The items to insert into the TableView, and the list to watch
     *              for changes (to automatically show in the TableView).
     */
    public Table (ObservableList items)
    {
        super(items);
    }
}
