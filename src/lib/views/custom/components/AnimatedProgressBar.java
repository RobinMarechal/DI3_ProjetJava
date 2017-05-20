package lib.views.custom.components;

import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import lib.views.custom.effects.Animated;

/**
 * Created by Robin on 20/05/2017.
 */
public class AnimatedProgressBar extends ProgressBar implements Animated
{
    /** animation duration, in milliseconds */
    private int duration = 300;

    public AnimatedProgressBar ()
    {
        prepareAnimation();
    }

    public AnimatedProgressBar (double progress)
    {
        super(progress);
        prepareAnimation();
    }

    @Override
    public void prepareAnimation ()
    {
        progressProperty().addListener((observable, oldValue, newValue) ->
        {
            new Thread(() -> Platform.runLater(() ->
            {
                new Transition()
                {
                    {
                        setCycleDuration(Duration.millis(duration));
                    }

                    @Override
                    protected void interpolate (double frac)
                    {
                        final double progression = newValue.doubleValue() - oldValue.doubleValue();
                        setProgress(oldValue.doubleValue() + progression * frac);
                    }
                };
            }));
        });
    }

    public int getDuration ()
    {
        return duration;
    }

    public void setDuration (int duration)
    {
        this.duration = duration;
    }
}
