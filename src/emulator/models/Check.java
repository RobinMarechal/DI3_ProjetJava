package emulator.models;

import fr.etu.univtours.marechal.SimpleDateTime;

/**
 * Created by Robin on 28/05/2017.
 */
public class Check
{
    private SimpleDateTime dateTime;
    private int employeeId;

    public Check (SimpleDateTime dateTime, int employeeId)
    {
        this.dateTime = dateTime;
        this.employeeId = employeeId;
    }

    public SimpleDateTime getDateTime ()
    {
        return dateTime;
    }

    public int getEmployeeId ()
    {
        return employeeId;
    }
}
