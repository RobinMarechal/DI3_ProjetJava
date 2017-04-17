package models;

import lib.json.Jsonable;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private static HashMap<LocalDate, Integer> totalChecksInPerDay = new HashMap<>();

    /**
     * The number of checks out per day
     */
    private static HashMap<LocalDate, Integer> totalChecksOutPerDay = new HashMap<>();


    /**
     * The time (HH:MM) when this employee arrived at work this day
     */
    private LocalTime arrivedAt;

    /**
     * The time (HH:MM) when this employee left work this day
     */
    private LocalTime leftAt;

    /**
     * The date of the working day
     */
    private LocalDate date;

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
    CheckInOut (Employee employee, LocalDate date)
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
    CheckInOut (Employee employee, LocalDateTime dateTime)
    {
        this.employee = employee;
        this.date = LocalDate.from(dateTime);

        check(dateTime);
    }

    /**
     * Rounds the check time to the nearest quarter <br/>
     * Ex: 8h07 => 8h00; 8h08 => 8h15
     *
     * @param time The time to round
     * @return the rounded time
     */
    private LocalTime roundTimeToNearestQuarter (LocalTime time)
    {
        LocalTime lt = LocalTime.from(time).truncatedTo(ChronoUnit.MINUTES);

        int minutes = lt.getMinute();
        int minutesInQuarter = minutes % 15;
        int minutesToAdd = -minutesInQuarter;
        if (minutesInQuarter > 7)
        {
            minutesToAdd += 15;
        }

        lt = lt.plusMinutes(minutesToAdd);
        return lt;
    }

    /**
     * Retrieve the total number of checks in.
     *
     * @return the number of checks in
     */
    public static int getTotalChecksIn ()
    {
        int count = 0;
        for (Map.Entry<LocalDate, Integer> entry : totalChecksInPerDay.entrySet())
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
        for (Map.Entry<LocalDate, Integer> entry : totalChecksOutPerDay.entrySet())
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
    public static int getTotalChecksInAt (LocalDate date)
    {
        return totalChecksInPerDay.containsKey(date) ? totalChecksInPerDay.get(date) : 0;
    }

    /**
     * Retrieve the number of checks out at a specific date
     *
     * @param date The date
     * @return the number of checks out at the date
     */
    public static int getTotalChecksOutAt (LocalDate date)
    {
        return totalChecksOutPerDay.containsKey(date) ? totalChecksOutPerDay.get(date) : 0;
    }

    /**
     * Retrieve the number of checks (in + out) at a specific date
     *
     * @param date The date
     * @return the number of checks (in + out) at the date
     */
    public static int getTotalChecksAt (LocalDate date)
    {
        return getTotalChecksInAt(date) + getTotalChecksOutAt(date);
    }

    /**
     * Retrieve the check-in time of the employee (rounded to the nearest quarter)
     *
     * @return The check-in time of the employee(rounded to the nearest quarter)
     */
    public LocalTime getArrivedAt ()
    {
        return arrivedAt;
    }

    /**
     * Modify the check-in time of the employee (will be rounded to the nearest quarter)
     *
     * @param arrivedAt the time of the check-in
     */
    public void setArrivedAt (LocalTime arrivedAt)
    {
        checkIn(arrivedAt);
    }

    /**
     * Retrieve the check-out time of the employee (rounded to the nearest quarter)
     *
     * @return The check-out time of the employee
     */
    public LocalTime getLeftAt ()
    {
        return leftAt;
    }

    /**
     * Modify the check-out time of the employee (will be rounded to the nearest quarter)
     *
     * @param leftAt the time of the check-out
     */
    public void setLeftAt (LocalTime leftAt)
    {
        checkOut(leftAt);
    }

    /**
     * Retrieve the checks' date
     *
     * @return the checks' date
     */
    public LocalDate getDate ()
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
    void check (LocalDateTime dateTime)
    {
        LocalTime time = LocalTime.from(dateTime);

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
    private void checkIn (LocalTime time)
    {
        arrivedAt = roundTimeToNearestQuarter(time);

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
    private void checkOut (LocalTime time)
    {
        leftAt = roundTimeToNearestQuarter(time);

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

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String arrivedAtStr = arrivedAt == null ? null : arrivedAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        String leftAtStr = leftAt == null ? null : leftAt.format(DateTimeFormatter.ofPattern("HH:mm"));

        checkObject.put("date", dateStr);
        checkObject.put("arrivedAt", arrivedAtStr);
        checkObject.put("leftAt", leftAtStr);

        return checkObject;
    }
}
