package lib.views.custom.components;

import lib.views.Template;
import lib.views.custom.effects.Animated;
import lib.views.custom.effects.transitions.LabelSlider;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Robin on 05/05/2017.<br>
 * this class is an animated Label which can slides on mouveover
 */
public class SlidingLabel extends javafx.scene.control.Label implements Animated
{
    /** The animation duration in milliseconds */
    private int animationDuration = 120;

    /** The initial top padding */
    private int paddingTop = 10;
    /** The initial right padding */
    private int paddingRight = 0;
    /** The initial bottom padding */
    private int paddingBottom = 10;
    /** The initial left padding */
    private int paddingLeft = 20;

    /** The additional top padding */
    private int additionalPaddingTop = 0;
    /** The additional right padding */
    private int additionalPaddingRight = 0;
    /** The additional bottom padding */
    private int additionalPaddingBottom = 0;
    /** The additional left padding */
    private int additionalPaddingLeft = 0;

    /** The animation */
    private LabelSlider slider;
    /** The reverse animation */
    private LabelSlider sliderBack;

    /**
     * Default constructor
     */
    public SlidingLabel ()
    {
    }

    /**
     * Text constructor
     * @param text the text of the label
     */
    public SlidingLabel (String text)
    {
        super(text);
    }

    /**
     * Get the animation duration
     * @return the animation duration
     */
    public int getAnimationDuration ()
    {
        return animationDuration;
    }

    /**
     * Set the animation duration
     * @param animationDuration the animation duration
     */
    public void setAnimationDuration (int animationDuration)
    {
        this.animationDuration = animationDuration;
    }

    /**
     * Get the inital top padding
     * @return the inital top padding
     */
    public int getPaddingTop ()
    {
        return paddingTop;
    }

    /**
     * Set the inital top padding
     * @param paddingTop the inital top padding
     */
    public void setPaddingTop (int paddingTop)
    {
        this.paddingTop = paddingTop;
        reload();
    }

    /**
     * Get the inital right padding
     * @return the inital right padding
     */
    public int getPaddingRight ()
    {
        return paddingRight;
    }

    /**
     * Set the inital right padding
     * @param paddingRight the inital right padding
     */
    public void setPaddingRight (int paddingRight)
    {
        this.paddingRight = paddingRight;
        reload();
    }

    /**
     * Get the inital bottom padding
     * @return the inital bottom padding
     */
    public int getPaddingBottom ()
    {
        return paddingBottom;
    }

    /**
     * Set the inital bottom padding
     * @param paddingBottom the inital bottom padding
     */
    public void setPaddingBottom (int paddingBottom)
    {
        this.paddingBottom = paddingBottom;
        reload();
    }

    /**
     * Get the inital left padding
     * @return the inital left padding
     */
    public int getPaddingLeft ()
    {
        return paddingLeft;
    }

    /**
     * Set the inital left padding
     * @param paddingLeft the inital left padding
     */
    public void setPaddingLeft (int paddingLeft)
    {
        this.paddingLeft = paddingLeft;
        reload();
    }

    /**
     * Get the additional top padding
     * @return the additional top padding
     */
    public int getAdditionalPaddingTop ()
    {
        return additionalPaddingTop;
    }

    /**
     * Set the additional top padding
     * @param additionalPaddingTop the additional top padding
     */
    public void setAdditionalPaddingTop (int additionalPaddingTop)
    {
        this.additionalPaddingTop = additionalPaddingTop;
    }

    /**
     * Get the additional right padding
     * @return the additional right padding
     */
    public int getAdditionalPaddingRight ()
    {
        return additionalPaddingRight;
    }

    /**
     * Set the additional right padding
     * @param additionalPaddingRight the additional right padding
     */
    public void setAdditionalPaddingRight (int additionalPaddingRight)
    {
        this.additionalPaddingRight = additionalPaddingRight;
    }

    /**
     * Get the additional bottom padding
     * @return the additional bottom padding
     */
    public int getAdditionalPaddingBottom ()
    {
        return additionalPaddingBottom;
    }

    /**
     * Set the additional bottom padding
     * @param additionalPaddingBottom the additional bottom padding
     */
    public void setAdditionalPaddingBottom (int additionalPaddingBottom)
    {
        this.additionalPaddingBottom = additionalPaddingBottom;
    }

    /**
     * Get the additional left padding
     * @return the additional left padding
     */
    public int getAdditionalPaddingLeft ()
    {
        return additionalPaddingLeft;
    }

    /**
     * Set the additional left padding
     * @param additionalPaddingLeft the additional left padding
     */
    public void setAdditionalPaddingLeft (int additionalPaddingLeft)
    {
        this.additionalPaddingLeft = additionalPaddingLeft;
    }

    /**
     * Get the LabelSlider animation object
     * @return the LabelSlider animation object
     */
    public LabelSlider getSlider ()
    {
        return slider;
    }

    /**
     * Set the LabelSlider animation object
     * @param slider the LabelSlider animation object
     */
    public void setSlider (@NotNull LabelSlider slider)
    {
        this.slider = slider;
    }

    /**
     * Prepare events mouseover events
     */
    protected void prepareEventHandler ()
    {
        setOnMouseEntered(event ->
        {
            if (Template.getInstance().getSelectedTab() != this)
            {
                Task<Void> task = new Task<Void>()
                {
                    @Override
                    protected Void call () throws Exception
                    {
                        slider.play();
                        return null;
                    }
                };

                new Thread(task).start();
            }
        });

        setOnMouseExited(event ->
        {
            if (Template.getInstance().getSelectedTab() != this)
            {
                Task<Void> task = new Task<Void>()
                {
                    @Override
                    protected Void call () throws Exception
                    {
                        sliderBack.play();
                        return null;
                    }
                };

                new Thread(task).start();
            }
        });
    }


    /**
     * Prepare the animations
     */
    @Override
    public void prepareAnimation ()
    {
        slider = new LabelSlider(this, animationDuration);
        slider.setInitialPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
        slider.setAdditionalPadding(additionalPaddingTop, additionalPaddingRight, additionalPaddingBottom, additionalPaddingLeft);

        sliderBack = slider.generateBackSlider();

        prepareEventHandler();
    }

    /**
     * Reset the padding at initial values
     */
    private void reload ()
    {
        setPadding(new Insets(paddingTop, paddingRight, paddingBottom, paddingLeft));
    }

    /**
     * Set the additional paddings
     * @param insets the additional padding
     */
    public void setAdditionalPadding (@NotNull Insets insets)
    {
        additionalPaddingTop = (int) insets.getTop();
        additionalPaddingRight = (int) insets.getRight();
        additionalPaddingBottom = (int) insets.getBottom();
        additionalPaddingLeft = (int) insets.getLeft();
    }

    /**
     * Set the initial paddings
     * @param insets the initial padding
     */
    public void setInitialPadding (@NotNull Insets insets)
    {
        setPadding(insets);
        paddingTop = (int) insets.getTop();
        paddingRight = (int) insets.getRight();
        paddingBottom = (int) insets.getBottom();
        paddingLeft = (int) insets.getLeft();
    }
}
