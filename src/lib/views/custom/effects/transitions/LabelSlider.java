package lib.views.custom.effects.transitions;

import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.util.Duration;
import lib.views.custom.components.SlidingLabel;

/**
 * Created by Robin on 05/05/2017.
 */
public class LabelSlider extends Transition
{
    // additional
    private int additionalPaddingTop = 0;

    private int additionalPaddingRight = 0;
    private int additionalPaddingBottom = 0;
    private int additionalPaddingLeft = 0;
    // base
    private int basePaddingTop = 0;

    private int basePaddingRight = 0;
    private int basePaddingBottom = 0;
    private int basePaddingLeft = 0;

    private SlidingLabel label;
    private int duration = 150;


    public LabelSlider (SlidingLabel label, int duration)
    {
        this(label);
        setDuration(duration);
    }

    public LabelSlider (SlidingLabel label)
    {
        super();
        this.label = label;
        setDuration(duration);

    }

    public LabelSlider (double targetFramerate, SlidingLabel label)
    {
        super(targetFramerate);
        this.label = label;
        setDuration(duration);
    }

    public SlidingLabel getSlidingLabel ()
    {
        return label;
    }

    public void setSlidingLabel (SlidingLabel label)
    {
        this.label = label;
    }

    public void setBasePadding (int top, int right, int bottom, int left)
    {
        basePaddingTop = top;
        basePaddingRight = right;
        basePaddingBottom = bottom;
        basePaddingLeft = left;
    }

    public void setAdditionalPadding (int top, int right, int bottom, int left)
    {
        additionalPaddingTop = top;
        additionalPaddingRight = right;
        additionalPaddingBottom = bottom;
        additionalPaddingLeft = left;
    }

    public int getBasePaddingTop ()
    {
        return basePaddingTop;
    }

    public void setBasePaddingTop (int basePaddingTop)
    {
        this.basePaddingTop = basePaddingTop;
    }

    public int getBasePaddingRight ()
    {
        return basePaddingRight;
    }

    public void setBasePaddingRight (int basePaddingRight)
    {
        this.basePaddingRight = basePaddingRight;
    }

    public int getBasePaddingBottom ()
    {
        return basePaddingBottom;
    }

    public void setBasePaddingBottom (int basePaddingBottom)
    {
        this.basePaddingBottom = basePaddingBottom;
    }

    public int getBasePaddingLeft ()
    {
        return basePaddingLeft;
    }

    public void setBasePaddingLeft (int basePaddingLeft)
    {
        this.basePaddingLeft = basePaddingLeft;
    }

    public int getAdditionalPaddingTop ()
    {
        return additionalPaddingTop;
    }

    public void setAdditionalPaddingTop (int additionalPaddingTop)
    {
        this.additionalPaddingTop = additionalPaddingTop;
    }

    public int getAdditionalPaddingRight ()
    {
        return additionalPaddingRight;
    }

    public void setAdditionalPaddingRight (int additionalPaddingRight)
    {
        this.additionalPaddingRight = additionalPaddingRight;
    }

    public int getAdditionalPaddingBottom ()
    {
        return additionalPaddingBottom;
    }

    public void setAdditionalPaddingBottom (int additionalPaddingBottom)
    {
        this.additionalPaddingBottom = additionalPaddingBottom;
    }

    public int getAdditionalPaddingLeft ()
    {
        return additionalPaddingLeft;
    }

    public void setAdditionalPaddingLeft (int additionalPaddingLeft)
    {
        this.additionalPaddingLeft = additionalPaddingLeft;
    }

    public LabelSlider generateBackSlider ()
    {
        LabelSlider slider = new LabelSlider(label, duration);

        slider.basePaddingTop = basePaddingTop + additionalPaddingTop;
        slider.basePaddingRight = basePaddingRight + additionalPaddingRight;
        slider.basePaddingBottom = basePaddingBottom + additionalPaddingBottom;
        slider.basePaddingLeft = basePaddingLeft+ additionalPaddingLeft;

        slider.additionalPaddingTop = -1 * additionalPaddingTop;
        slider.additionalPaddingRight = -1 * additionalPaddingRight;
        slider.additionalPaddingBottom = -1 * additionalPaddingBottom;
        slider.additionalPaddingLeft = -1 * additionalPaddingLeft;

        return slider;
    }

    @Override
    protected void interpolate (double frac)
    {
        double top = basePaddingTop + additionalPaddingTop * frac;
        double right = basePaddingRight + additionalPaddingRight * frac;
        double bottom = basePaddingBottom + additionalPaddingBottom * frac;
        double left = basePaddingLeft + additionalPaddingLeft * frac;

        label.setBasePadding(new Insets(top, right, bottom, left));
    }

    public int getDuration ()
    {
        return duration;
    }

    public void setDuration (int duration)
    {
        this.duration = duration;
        setCycleDuration(Duration.millis(duration));
    }
}
