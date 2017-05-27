package application.lib.views.custom.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import application.lib.util.Closure;

/**
 * Created by Robin on 10/05/2017.
 */
public class Link extends Label
{
    private Closure closure;
    private Property property = new SimpleStringProperty(this, "property");
    private String cssClassName = "link";

    public Link (Property text, Closure closure)
    {
        this(text, closure, true);
    }

    public Link(Closure closure)
    {
        this("", closure, true);
    }

    public Link (String text, Closure closure, boolean automaticCall)
    {
        super(text);
        this.closure = closure;
        getStyleClass().add(cssClassName);

        if (automaticCall)
        {
            prepareClickEvent();
        }
    }


    public Link (Property property, Closure closure, boolean automaticCall)
    {
        textProperty().bind(property);
        this.closure = closure;
        this.property = property;
        getStyleClass().add(cssClassName);

        if (automaticCall)
        {
            prepareClickEvent();
        }
    }

    public Property property ()
    {
        return property;
    }

    public void setProperty (Property property)
    {
        this.property = property;
    }

    private void prepareClickEvent ()
    {
        setOnMouseClicked(event -> trigger());
    }

    public void setTooltipValue (String str)
    {
        setTooltip(new Tooltip(str));
    }

    public void removeTooltip ()
    {
        setTooltip(null);
    }

    public void trigger ()
    {
        try
        {
            closure.call();
        }
        catch (Exception e)
        {
            System.out.println("Error while triggering link...");
            e.printStackTrace();
        }
    }
}
