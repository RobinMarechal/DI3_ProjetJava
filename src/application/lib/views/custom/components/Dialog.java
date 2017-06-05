package application.lib.views.custom.components;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

/**
 * Created by Robin on 21/05/2017. <br/>
 * This class represent a dialog box
 */
public abstract class Dialog extends javafx.stage.Stage
{
    /**
     * Default constructor
     */
    public Dialog ()
    {
        initStyle(StageStyle.UTILITY);
        setResizable(false);
        prepareDialogEventListeners();
    }

    /**
     * Prepare dialog events listener like close on espace pressed, or close on focus loss
     */
    public void prepareDialogEventListeners ()
    {
        addEventFilter(KeyEvent.KEY_RELEASED, event ->
        {
            if (event.getCode() == KeyCode.ESCAPE)
            {
                close();
            }
        });

        focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
            {
                close();
            }
        });
    }

    /**
     * set the content and show the dialog
     * @param content the inside pane
     */
    protected void setContent (Pane content)
    {
        setScene(new Scene(content));
        show();
    }


}

