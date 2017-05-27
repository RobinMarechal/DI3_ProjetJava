package application.lib.views;

/**
 * Created by Robin on 25/04/2017.
 */
public enum Tabs
{
    COMPANY(0),
    EMPLOYEES(1),
    STANDARD_DEPARTMENTS(2)
    ;

    private final int value;

    private Tabs(final int value)
    {
        this.value = value;
    }

    public String toString ()
    {
        return (name().charAt(0) + name().substring(1).toLowerCase()).replace("_", " ");
    }

    public String getControllerClassName()
    {
        String str = "";
        String name[] = toString().split("_");

        for (String s : name)
        {
            str += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        
        return toString() + "Controller";
    }
}
