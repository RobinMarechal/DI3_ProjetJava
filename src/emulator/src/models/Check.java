package models;

import com.sun.istack.internal.NotNull;
import fr.etu.univtours.marechal.SimpleDateTime;

import java.time.format.DateTimeFormatter;

/**
 * Created by Robin on 28/05/2017. <br>
 * This class represents a Check data that will be formatted and sent to the server
 */
public class Check
{
    private static String checkDataFormat = "{datetime};{id}";
    private static String dateTimeFormat = "yyyy-MM-dd HH:mm";

    /** The datetime of the check */
    private SimpleDateTime dateTime;
    /** The id of the checking employee */
    private int employeeId;

    /**
     * 2 parameters constructor
     *
     * @param dateTime   the datetime of the check
     * @param employeeId the id of the checking employee
     */
    public Check (@NotNull SimpleDateTime dateTime, int employeeId)
    {
        this.dateTime = dateTime;
        this.employeeId = employeeId;
    }

    /**
     * Get the datetime
     *
     * @return the datetime
     */
    public SimpleDateTime getDateTime ()
    {
        return dateTime;
    }

    /**
     * Get the employee's ID
     *
     * @return the employee's ID
     */
    public int getEmployeeId ()
    {
        return employeeId;
    }

    /**
     * Get the check data format
     * @return the check data format
     */
    public static String getCheckDataFormat ()
    {
        return checkDataFormat;
    }

    /**
     * Set the check data format
     * @param checkDataFormat the check data format
     */
    public static void setCheckDataFormat (@NotNull String checkDataFormat)
    {
        Check.checkDataFormat = checkDataFormat;
    }

    /**
     * Get the datetime format
     * @return the datetime format
     */
    public static String getDateTimeFormat ()
    {
        return dateTimeFormat;
    }

    /**
     * Set the datetime format
     * @param dateTimeFormat the datetime format
     */
    public static void setDateTimeFormat (@NotNull String dateTimeFormat)
    {
        Check.dateTimeFormat = dateTimeFormat;
    }

    /**
     * Returns a string representation of the object
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString ()
    {
        String format      = new String(checkDataFormat);
        String strDateTime = getDateTime().toLocalDateTime().format(DateTimeFormatter.ofPattern(dateTimeFormat));
        int    id          = getEmployeeId();

        String toSend = format.replace("{id}", id + "").replace("{datetime}", strDateTime);

        return toSend;
    }
}
