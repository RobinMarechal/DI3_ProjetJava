package lib.views.custom.components;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by Robin on 21/05/2017.
 */
public class Dialog
{
    public void setDialogCloseShortcut (javafx.stage.Stage stage)
    {
        if (stage != null)
        {
            stage.addEventFilter(KeyEvent.KEY_RELEASED, event ->
            {
                if (event.getCode() == KeyCode.ESCAPE)
                {
                    stage.close();
                }
            });

            stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(!newValue)
                {
                    stage.close();
                }
            });
        }
    }
}

