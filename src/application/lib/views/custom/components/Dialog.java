package application.lib.views.custom.components;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

/**
 * Created by Robin on 21/05/2017.
 */
public class Dialog
{
    protected Stage stage = new Stage();

    public Dialog ()
    {
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        setDialogShortcut();
    }

    public void setDialogShortcut ()
    {
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event ->
        {
            if (event.getCode() == KeyCode.ESCAPE)
            {
                stage.close();
            }
        });

        stage.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
            {
                stage.close();
            }
        });
    }

    public void close ()
    {
        stage.close();
    }

    protected void setContent (Pane content)
    {
        stage.setScene(new Scene(content));
        stage.show();
    }


}

