package lib.views.custom.components;

import javafx.stage.StageStyle;

/**
 * Created by Robin on 01/05/2017.
 */
public class Stage extends javafx.stage.Stage
{
    /**
     * Creates a new instance of decorated {@code Stage}.
     *
     * @throws IllegalStateException if this constructor is called on a thread
     *                               other than the JavaFX Application Thread.
     */
    public Stage ()
    {
    }

    /**
     * Creates a new instance of {@code Stage}.
     *
     * @param style The style of the {@code Stage}
     * @throws IllegalStateException if this constructor is called on a thread
     *                               other than the JavaFX Application Thread.
     */
    public Stage (StageStyle style)
    {
        super(style);
    }
}