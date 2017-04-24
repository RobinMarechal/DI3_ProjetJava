package models;

import lib.json.Jsonable;
import org.json.simple.JSONObject;

import lib.time.Time;
import lib.time.Date;
import lib.time.DateTime;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robin on 27/03/2017. <br/>
 * This class represents a check-in AND a check-out for a day, for an employee (including managers). <br/>
 * An instance of CheckInOut represents both check-in and check-out.
 */
public class CheckInOut implements Jsonable
{
    /**
     * The number pf checks in per day
     */
    private static HashMap<Date, Integer> totalChecksInPerDay = new HashMap<>();

    /**
     * The number of checks out per day
     */
    private static HashMap<Date, Integer> totalChecksOutPerDay = new HashMap<>();


    /**
     * The time (HH:MM) when this employee arrived at work this day
     */
    private Time arrivedAt;

    /**
     * The time (HH:MM) when this employee left work this day
     */
    private Time leftAt;

    /**
     * The date of the working day
     */
    private Date date;

    /**
     * The employee who checked-in/out.
     */
    private Employee employee;


    /**
     * Constructor with date <br/>
     * Sets-up the object.
     *
     * @param employee The associated employee
     * @param date     The date of work
     */
    CheckInOut (Employee employee, Date date)
    {
        this.employee = employee;
        this.date = date;
    }

    /**
     * Constructor with DateTime <br/>
     * Sets-up the object and register the check-in
     *
     * @param employee The associated employee
     * @param dateTime The arrival of the employee
     */
    CheckInOut (Employee employee, DateTime dateTime)
    {
        this.employee = employee;
        this.date = Date.fromDateTime(dateTime);

        check(dateTime);
    }

    /**
     * Retrieve the total number of checks in.
     *
     * @return the number of checks in
     */
    public static int getTotalChecksIn ()
    {
        int count = 0;
        for (Map.Entry<Date, Integer> entry : totalChecksInPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    /**
     * Retrieve the total number of checks out.
     *
     * @return the number of checks out
     */
    public static int getTotalChecksOut ()
    {
        int count = 0;
        for (Map.Entry<Date, Integer> entry : totalChecksOutPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    /**
     * Retrieve the total number of checks (in and out)
     *
     * @return the number of checks
     */
    public static int getTotalChecks ()
    {
        return getTotalChecksIn() + getTotalChecksOut();
    }

    /**
     * Retrieve the number of checks in at a specific date
     *
     * @param date The date
     * @return the number of checks in at the date
     */
    public static int getTotalChecksInAt (Date date)
    {
        return totalChecksInPerDay.containsKey(date) ? totalChecksInPerDay.get(date) : 0;
    }

    /**
     * Retrieve the number of checks out at a specific date
     *
     * @param date The date
     * @return the number of checks out at the date
     */
    public static int getTotalChecksOutAt (Date date)
    {
        return totalChecksOutPerDay.containsKey(date) ? totalChecksOutPerDay.get(date) : 0;
    }

    /**
     * Retrieve the number of checks (in + out) at a specific date
     *
     * @param date The date
     * @return the number of checks (in + out) at the date
     */
    public static int getTotalChecksAt (Date date)
    {
        return getTotalChecksInAt(date) + getTotalChecksOutAt(date);
    }

    /**
     * Retrieve the check-in time of the employee (rounded to the nearest quarter)
     *
     * @return The check-in time of the employee(rounded to the nearest quarter)
     */
    public Time getArrivedAt ()
    {
        return arrivedAt;
    }

    /**
     * Modify the check-in time of the employee (will be rounded to the nearest quarter)
     *
     * @param arrivedAt the time of the check-in
     */
    public void setArrivedAt (Time arrivedAt)
    {
        checkIn(arrivedAt);
    }

    /**
     * Retrieve the check-out time of the employee (rounded to the nearest quarter)
     *
     * @return The check-out time of the employee
     */
    public Time getLeftAt ()
    {
        return leftAt;
    }

    /**
     * Modify the check-out time of the employee (will be rounded to the nearest quarter)
     *
     * @param leftAt the time of the check-out
     */
    public void setLeftAt (Time leftAt)
    {
        checkOut(leftAt);
    }

    /**
     * Retrieve the checks' date
     *
     * @return the checks' date
     */
    public Date getDate ()
    {
        return date;
    }

    /**
     * Retrieve the employee associated to the check in/out instance
     *
     * @return the employee associated
     */
    public Employee getEmployee ()
    {
        return employee;
    }

    /**
     * Registers a check (in or out) <br/>
     * If the check-in hasn't been registered yet, it registers it. <br/>
     * Otherwise, it registers the check-out. <br/>
     * If both have been registered, it does nothing. <br/>
     * To modify a check (in or out), use the setters.
     *
     * @param dateTime the datetime of the check
     */
    void check (DateTime dateTime)
    {
        Time time = Time.fromDateTime(dateTime);

        if (arrivedAt == null)
        {
            checkIn(time);
        }
        else if (leftAt == null)
        {
            checkOut(time);
        }
    }

    /**
     * Registers the check-in <br/>
     * Modify it if it already exists (which can only happens when setters are used)
     *
     * @param time the check-in time.
     */
    private void checkIn (Time time)
    {
        arrivedAt = time;

        // If there already was a checkIn this day, increment the counter
        // otherwise, create the HashMap entry for this date
        if (totalChecksInPerDay.containsKey(date))
        {
            totalChecksInPerDay.put(date, totalChecksInPerDay.get(date) + 1);
        }
        else
        {
            totalChecksInPerDay.put(date, 1);
        }
    }

    /**
     * Registers the check-out <br/>
     * Modify it if it already exists (which can only happens when setters are used)
     *
     * @param time the check-out time.
     */
    private void checkOut (Time time)
    {
        leftAt = time;

        if (totalChecksOutPerDay.containsKey(date))
        {
            totalChecksOutPerDay.put(date, totalChecksOutPerDay.get(date) + 1);
        }
        else
        {
            totalChecksOutPerDay.put(date, 1);
        }
    }

    /**
     * Create a String representing the CheckInOut instance
     *
     * @return The String created
     */
    @Override
    public String toString ()
    {
        String str = employee.toString();
        if (arrivedAt != null)
        {
            str += " arrived at " + arrivedAt;
        }
        if (leftAt != null)
        {
            if (arrivedAt != null)
            {
                str += " and";
            }

            str += " left at " + leftAt;
        }

        str += " on the " + date;

        return str;
    }

    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject checkObject = new JSONObject();

        String dateStr = date.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String arrivedAtStr = arrivedAt == null ? null : arrivedAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String leftAtStr = leftAt == null ? null : leftAt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        checkObject.put("date", dateStr);
        checkObject.put("arrivedAt", arrivedAtStr);
        checkObject.put("leftAt", leftAtStr);

        return checkObject;
    }
}
