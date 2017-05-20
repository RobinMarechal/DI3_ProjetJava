package lib.views.custom.components;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lib.views.Template;
import lib.views.custom.effects.Animated;
import lib.views.custom.effects.transitions.LabelSlider;

/**
 * Created by Robin on 05/05/2017.
 */
public class SlidingLabel extends Label implements Animated
{
    private int animationDuration = 120;
    private int paddingTop = 10;
    private int paddingRight = 0;
    private int paddingBottom = 10;
    private int paddingLeft = 20;

    private int additionnalPaddingTop = 0;
    private int additionnalPaddingRight = 0;
    private int additionnalPaddingBottom = 0;
    private int additionnalPaddingLeft = 0;

    private LabelSlider slider;
    private LabelSlider sliderBack;
    private EventHandler<MouseEvent> slideEvent;

    public SlidingLabel ()
    {
    }

    public SlidingLabel (String text)
    {
        super(text);
    }

    public SlidingLabel (String text, Node graphic)
    {
        super(text, graphic);
    }


    public int getAnimationDuration ()
    {
        return animationDuration;
    }

    public void setAnimationDuration (int animationDuration)
    {
        this.animationDuration = animationDuration;
    }

    public int getPaddingTop ()
    {
        return paddingTop;
    }

    public void setPaddingTop (int paddingTop)
    {
        this.paddingTop = paddingTop;
        reload();
    }

    public int getPaddingRight ()
    {
        return paddingRight;
    }

    public void setPaddingRight (int paddingRight)
    {
        this.paddingRight = paddingRight;
        reload();
    }

    public int getPaddingBottom ()
    {
        return paddingBottom;
    }

    public void setPaddingBottom (int paddingBottom)
    {
        this.paddingBottom = paddingBottom;
        reload();
    }

    public int getPaddingLeft ()
    {
        return paddingLeft;
    }

    public void setPaddingLeft (int paddingLeft)
    {
        this.paddingLeft = paddingLeft;
        reload();
    }

    public int getAdditionnalPaddingTop ()
    {
        return additionnalPaddingTop;
    }

    public void setAdditionnalPaddingTop (int additionnalPaddingTop)
    {
        this.additionnalPaddingTop = additionnalPaddingTop;
    }

    public int getAdditionnalPaddingRight ()
    {
        return additionnalPaddingRight;
    }

    public void setAdditionnalPaddingRight (int additionnalPaddingRight)
    {
        this.additionnalPaddingRight = additionnalPaddingRight;
    }

    public int getAdditionnalPaddingBottom ()
    {
        return additionnalPaddingBottom;
    }

    public void setAdditionnalPaddingBottom (int additionnalPaddingBottom)
    {
        this.additionnalPaddingBottom = additionnalPaddingBottom;
    }

    public int getAdditionnalPaddingLeft ()
    {
        return additionnalPaddingLeft;
    }

    public void setAdditionnalPaddingLeft (int additionnalPaddingLeft)
    {
        this.additionnalPaddingLeft = additionnalPaddingLeft;
    }

    public LabelSlider getSlider ()
    {
        return slider;
    }

    public void setSlider (LabelSlider slider)
    {
        this.slider = slider;
    }

    public EventHandler<MouseEvent> getSlideEvent ()
    {
        return slideEvent;
    }

    public void setSlideEvent (EventHandler<MouseEvent> slideEvent)
    {
        this.slideEvent = slideEvent;
    }

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

    @Override
    public void prepareAnimation ()
    {
        slider = new LabelSlider(this, animationDuration);
        slider.setBasePadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
        slider.setAdditionalPadding(additionnalPaddingTop, additionnalPaddingRight, additionnalPaddingBottom, additionnalPaddingLeft);

        sliderBack = slider.generateBackSlider();

        prepareEventHandler();
    }

    private void reload ()
    {
        setPadding(new Insets(paddingTop, paddingRight, paddingBottom, paddingLeft));
    }

    public void setAdditionalPadding (Insets insets)
    {
        additionnalPaddingTop = (int) insets.getTop();
        additionnalPaddingRight = (int) insets.getRight();
        additionnalPaddingBottom = (int) insets.getBottom();
        additionnalPaddingLeft = (int) insets.getLeft();
    }

    public void setBasePadding (Insets insets)
    {
        setPadding(insets);
        paddingTop = (int) insets.getTop();
        paddingRight = (int) insets.getRight();
        paddingBottom = (int) insets.getBottom();
        paddingLeft = (int) insets.getLeft();
    }
}
