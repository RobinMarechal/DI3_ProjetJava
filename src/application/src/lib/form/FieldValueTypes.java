package lib.form;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Robin on 23/05/2017. <br>
 * Form.Field value type. <br>
 * the type allow the {@link lib.form.validator.Validator} to validated (or not) a field with a regular expression
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
    UNDEFINED("^.*$");

    /** The regexp */
    private final String regexp;

    /**
     * Default constructor
     * @param regexp the regular expression
     */
    FieldValueTypes (@NotNull String regexp)
    {
        this.regexp = regexp;
    }

    /**
     * Get the associate regular expression
     * @return the associate regular expression
     */
    public String getRegexp ()
    {
        return regexp;
    }

    /**
     * Get the String associated to the enum value
     * @return the String associated to the enum value
     */
    @Override
    public String toString ()
    {
        return super.toString();
    }
}
