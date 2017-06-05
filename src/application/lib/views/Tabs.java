package application.lib.views;

/**
 * Created by Robin on 25/04/2017.<br/>
 * A list of applications left tabs
 */
public enum Tabs
{
    /** Company tabs */
    COMPANY,
    /** Employees tabs */
    EMPLOYEES,
    /** Standard departments tabs */
    STANDARD_DEPARTMENTS,
    ;

    /**
     * Get the name of the section from the enum field name
     * @return the name of the section
     */
    public String toString ()
    {
        return (name().charAt(0) + name().substring(1).toLowerCase()).replace("_", " ");
    }
}
