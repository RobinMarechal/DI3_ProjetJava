package application.lib.views.custom.effects.transitions;

import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.util.Duration;
import application.lib.views.custom.components.SlidingLabel;

/**
 * Created by Robin on 05/05/2017.<br/>
 * This class is a slide animation performed on a {@link SlidingLabel}
 */
public class LabelSlider extends Transition
{
    /** The additional top padding */
    private int additionalPaddingTop = 0;
    /** The additional right padding */
    private int additionalPaddingRight = 0;
    /** The additional bottom padding */
    private int additionalPaddingBottom = 0;
    /** The additional left padding */
    private int additionalPaddingLeft = 0;

    /** The initial top padding */
    private int initialPaddingTop = 0;
    /** The initial right padding */
    private int initialPaddingRight = 0;
    /** The initial bottom padding */
    private int initialPaddingBottom = 0;
    /** The initial left padding */
    private int initialPaddingLeft = 0;


    /** The animated label */
    private SlidingLabel label;

    /** The animation duration */
    private int duration = 150;

    /**
     * 2 parameters constructor
     *
     * @param label    the label to animate
     * @param duration the animation duration
     */
    public LabelSlider (SlidingLabel label, int duration)
    {
        this(label);
        setDuration(duration);
    }

    /**
     * 1 parameter constructor
     *
     * @param label the label to animate
     */
    public LabelSlider (SlidingLabel label)
    {
        super();
        this.label = label;
        setDuration(duration);

    }

    /**
     * Get the animated label
     *
     * @return the animated label
     */
    public SlidingLabel getSlidingLabel ()
    {
        return label;
    }

    /**
     * Set the animated label
     *
     * @param label the animated label
     */
    public void setSlidingLabel (SlidingLabel label)
    {
        this.label = label;
    }

    /**
     * Get the animation duration
     *
     * @return the animation duration
     */
    public int getDuration ()
    {
        return duration;
    }

    /**
     * Set the animation duration
     *
     * @param duration the animation duration
     */
    public void setDuration (int duration)
    {
        this.duration = duration;
        setCycleDuration(Duration.millis(duration));
    }

    /**
     * Set the initial paddings
     *
     * @param top    the initial top padding
     * @param right  the initial right padding
     * @param bottom the initial bottom padding
     * @param left   the initial left padding
     */
    public void setInitialPadding (int top, int right, int bottom, int left)
    {
        initialPaddingTop = top;
        initialPaddingRight = right;
        initialPaddingBottom = bottom;
        initialPaddingLeft = left;
    }

    /**
     * Set the additional paddings
     *
     * @param top    the additional top padding
     * @param right  the additional right padding
     * @param bottom the additional bottom padding
     * @param left   the additional left padding
     */
    public void setAdditionalPadding (int top, int right, int bottom, int left)
    {
        additionalPaddingTop = top;
        additionalPaddingRight = right;
        additionalPaddingBottom = bottom;
        additionalPaddingLeft = left;
    }

    /**
     * Get the inital top padding
     *
     * @return the inital top padding
     */
    public int getInitialPaddingTop ()
    {
        return initialPaddingTop;
    }

    /**
     * Set the inital top padding
     *
     * @param initialPaddingTop the inital top padding
     */
    public void setInitialPaddingTop (int initialPaddingTop)
    {
        this.initialPaddingTop = initialPaddingTop;
    }

    /**
     * Get the inital right padding
     *
     * @return the inital right padding
     */
    public int getInitialPaddingRight ()
    {
        return initialPaddingRight;
    }

    /**
     * Set the inital right padding
     *
     * @param initialPaddingRight the inital right padding
     */
    public void setInitialPaddingRight (int initialPaddingRight)
    {
        this.initialPaddingRight = initialPaddingRight;
    }

    /**
     * Get the inital bottom padding
     *
     * @return the inital bottom padding
     */
    public int getInitialPaddingBottom ()
    {
        return initialPaddingBottom;
    }

    /**
     * Set the inital bottom padding
     *
     * @param initialPaddingBottom the inital bottom padding
     */
    public void setInitialPaddingBottom (int initialPaddingBottom)
    {
        this.initialPaddingBottom = initialPaddingBottom;
    }

    /**
     * Get the inital left padding
     *
     * @return the inital left padding
     */
    public int getInitialPaddingLeft ()
    {
        return initialPaddingLeft;
    }

    /**
     * Set the inital left padding
     *
     * @param initialPaddingLeft the inital left padding
     */
    public void setInitialPaddingLeft (int initialPaddingLeft)
    {
        this.initialPaddingLeft = initialPaddingLeft;
    }


    /**
     * Get the additional top padding
     *
     * @return the additional top padding
     */
    public int getAdditionalPaddingTop ()
    {
        return additionalPaddingTop;
    }

    /**
     * Set the additional top padding
     *
     * @param additionalPaddingTop the additional top padding
     */
    public void setAdditionalPaddingTop (int additionalPaddingTop)
    {
        this.additionalPaddingTop = additionalPaddingTop;
    }

    /**
     * Get the additional right padding
     *
     * @return the additional right padding
     */
    public int getAdditionalPaddingRight ()
    {
        return additionalPaddingRight;
    }

    /**
     * Set the additional right padding
     *
     * @param additionalPaddingRight the additional right padding
     */
    public void setAdditionalPaddingRight (int additionalPaddingRight)
    {
        this.additionalPaddingRight = additionalPaddingRight;
    }

    /**
     * Get the additional bottom padding
     *
     * @return the additional bottom padding
     */
    public int getAdditionalPaddingBottom ()
    {
        return additionalPaddingBottom;
    }

    /**
     * Set the additional bottom padding
     *
     * @param additionalPaddingBottom the additional bottom padding
     */
    public void setAdditionalPaddingBottom (int additionalPaddingBottom)
    {
        this.additionalPaddingBottom = additionalPaddingBottom;
    }

    /**
     * Get the additional left padding
     *
     * @return the additional left padding
     */
    public int getAdditionalPaddingLeft ()
    {
        return additionalPaddingLeft;
    }

    /**
     * Set the additional left padding
     *
     * @param additionalPaddingLeft the additional left padding
     */
    public void setAdditionalPaddingLeft (int additionalPaddingLeft)
    {
        this.additionalPaddingLeft = additionalPaddingLeft;
    }


    /**
     * Generate the reverse animation
     *
     * @return the reverse animation
     */
    public LabelSlider generateBackSlider ()
    {
        LabelSlider slider = new LabelSlider(label, duration);

        slider.initialPaddingTop = initialPaddingTop + additionalPaddingTop;
        slider.initialPaddingRight = initialPaddingRight + additionalPaddingRight;
        slider.initialPaddingBottom = initialPaddingBottom + additionalPaddingBottom;
        slider.initialPaddingLeft = initialPaddingLeft + additionalPaddingLeft;

        slider.additionalPaddingTop = -1 * additionalPaddingTop;
        slider.additionalPaddingRight = -1 * additionalPaddingRight;
        slider.additionalPaddingBottom = -1 * additionalPaddingBottom;
        slider.additionalPaddingLeft = -1 * additionalPaddingLeft;

        return slider;
    }


    /**
     * The method {@code interpolate()} has to be provided by implementations of
     * {@code Transition}. While a {@code Transition} is running, this method is
     * called in every frame.
     * <p>
     * The parameter defines the current position with the animation. At the
     * start, the fraction will be {@code 0.0} and at the end it will be
     * {@code 1.0}. How the parameter increases, depends on the
     * {@link #interpolatorProperty() interpolator}, e.g. if the
     * {@code interpolator} is {@link javafx.animation.Interpolator#LINEAR}, the fraction will
     * increase linear.
     * <p>
     * This method must not be called by the user directly.
     *
     * @param frac The relative position
     */
    @Override
    protected void interpolate (double frac)
    {
        double top    = initialPaddingTop + additionalPaddingTop * frac;
        double right  = initialPaddingRight + additionalPaddingRight * frac;
        double bottom = initialPaddingBottom + additionalPaddingBottom * frac;
        double left   = initialPaddingLeft + additionalPaddingLeft * frac;

        label.setInitialPadding(new Insets(top, right, bottom, left));
    }
}
