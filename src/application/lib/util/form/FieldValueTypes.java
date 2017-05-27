package application.lib.util.form;

/**
 * Created by Robin on 23/05/2017.
 */
public enum FieldValueTypes
{
    FIRSTNAME("^([A-Z]?[a-z]+)(([ -][A-Z]?[a-z]+))*$"),
    LASTNAME("^([A-Z]?[a-z]+)(([ -][A-Z]?[a-z]+))*$"),
    NAME("^.+$"),
    HOURS("^[0-9]{0,2}$"),
    MINUTES("^[0-9]{0,2}$"),
    DATE("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$"),
    TIME("^[0-9]{1,2}:[0-9]{1,2}$"),
    DATETIME("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}$"),
    UNDEFINED("^*$");

    private final String regexp;

    FieldValueTypes (String value)
    {
        this.regexp = value;
    }

    public String getRegexp ()
    {
        return regexp;
    }

    @Override
    public String toString ()
    {
        return super.toString();
    }
}
