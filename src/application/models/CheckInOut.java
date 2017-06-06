package application.models;

import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import application.lib.json.Jsonable;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static fr.etu.univtours.marechal.SimpleDate.fromSimpleDateTime;

/**
 * Created by Robin on 27/03/2017. <br/>
 * This class represents a check-in AND a check-out for a day, for an employee (including managers). <br/>
 * An instance of CheckInOut represents both check-in and check-out.
 */
public class CheckInOut implements Jsonable
{
    /** JSON key for check data */
    protected static final String JSON_KEY_DATE = "date";
    /** JSON key for check in time */
    protected static final String JSON_KEY_ARRIVED_AT = "arrivedAt";
    /** JSON key for check out time */
    protected static final String JSON_KEY_LEFT_AT = "leftAt";

    /** The time (HH:MM) when this employee arrived at work this day (rounded to nearest quarter) */
    private ObjectProperty<SimpleTime> arrivedAt = new SimpleObjectProperty<>(this, "arrivedAt");

    /** The time (HH:MM) when this employee left work this day (rounded to nearest quarter) */
    private ObjectProperty<SimpleTime> leftAt = new SimpleObjectProperty<>(this, "leftAt");

    /** The date of the working day  */
    private ObjectProperty<SimpleDate> date = new SimpleObjectProperty<>(this, "date");

    /** The employee who checked-in/out. */
    private final Employee employee;


    /**
     * Constructor with date <br/>
     * Sets-up the object.
     *
     * @param employee The associated employee
     * @param date     The date of work
     */
    CheckInOut (Employee employee, SimpleDate date)
    {
        this.employee = employee;
        this.date.setValue(date);
    }

    /**
     * Constructor with SimpleDateTime <br/>
     * Sets-up the object and register the check-in
     *
     * @param employee The associated employee
     * @param dateTime The arrival of the employee
     */
    CheckInOut (Employee employee, SimpleDateTime dateTime)
    {
        this.employee = employee;
        this.date.setValue(fromSimpleDateTime(dateTime));

        check(dateTime);
    }

    /**
     * Retrieve the check-in time of the employee (rounded to the nearest quarter)
     *
     * @return The check-in time of the employee(rounded to the nearest quarter)
     */
    public SimpleTime getArrivedAt ()
    {
        return arrivedAt.getValue();
    }

    public ObjectProperty<SimpleTime> arrivedAtProperty ()
    {
        return arrivedAt;
    }

    /**
     * Modify the check-in time of the employee (will be rounded to the nearest quarter)
     *
     * @param arrivedAt the time of the check-in
     */
    public void setArrivedAt (SimpleTime arrivedAt)
    {
        checkIn(arrivedAt);
    }

    /**
     * Retrieve the check-out time of the employee (rounded to the nearest quarter)
     *
     * @return The check-out time of the employee
     */
    public SimpleTime getLeftAt ()
    {
        return leftAt.getValue();
    }

    public ObjectProperty<SimpleTime> leftAtProperty ()
    {
        return leftAt;
    }

    /**
     * Modify the check-out time of the employee (will be rounded to the nearest quarter)
     *
     * @param leftAt the time of the check-out
     */
    public void setLeftAt (SimpleTime leftAt)
    {
        checkOut(leftAt);
    }

    /**
     * Retrieve the checks' date
     *
     * @return the checks' date
     */
    public SimpleDate getDate ()
    {
        return date.getValue();
    }

    /**
     * Get the date property which can be used for bindings
     * @return the date property
     */
    public ObjectProperty<SimpleDate> dateProperty ()
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
    void check (SimpleDateTime dateTime)
    {
        SimpleTime time = SimpleTime.fromSimpleDateTime(dateTime);

        if (arrivedAt.getValue() == null)
        {
            checkIn(time);
        }
        else if (leftAt.getValue() == null)
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
    private void checkIn (SimpleTime time)
    {
        arrivedAt.setValue(time);

        Company.getCompany().incrementChecksInAt(date.getValue());
    }

    /**
     * Registers the check-out <br/>
     * Modify it if it already exists (which can only happens when setters are used)
     *
     * @param time the check-out time.
     */
    private void checkOut (SimpleTime time)
    {
        leftAt.setValue(time);

        Company.getCompany().incrementChecksOutAt(date.getValue());
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
        if (arrivedAt.getValue() != null)
        {
            str += " arrived at " + arrivedAt.getValue();
        }
        if (leftAt.getValue() != null)
        {
            if (arrivedAt.getValue() != null)
            {
                str += " and";
            }

            str += " left at " + leftAt.getValue();
        }

        str += " on the " + date.getValue();

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

        String dateStr = date.getValue().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String arrivedAtStr = arrivedAt.getValue() == null ? null : arrivedAt.getValue()
                                                                             .toLocalDateTime()
                                                                             .format(DateTimeFormatter.ofPattern
                                                                                     ("HH:mm"));
        String leftAtStr = leftAt.getValue() == null ? null : leftAt.getValue()
                                                                    .toLocalTime()
                                                                    .format(DateTimeFormatter.ofPattern("HH:mm"));

        checkObject.put(JSON_KEY_DATE, dateStr);
        checkObject.put(JSON_KEY_ARRIVED_AT, arrivedAtStr);
        checkObject.put(JSON_KEY_LEFT_AT, leftAtStr);

        return checkObject;
    }

    /**
     * Load a {@link CheckInOut} instance from a json object
     * @param employee the employee who has this check
     * @param obj the json object containing the information
     * @return the new created CheckInOut instance
     */
    public static CheckInOut loadFromJson (Employee employee, JSONObject obj)
    {
        LocalDate localDate = LocalDate.parse(obj.get(JSON_KEY_DATE)
                                                 .toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        LocalTime localIn = LocalTime.parse(obj.get(JSON_KEY_ARRIVED_AT)
                                               .toString(), DateTimeFormatter.ofPattern("HH:mm"));

        LocalTime localOut = null;
        Object    leftAt   = obj.get(JSON_KEY_LEFT_AT);
        if (leftAt != null) // otherwise, the employee simply didn't check out
        {
            localOut = LocalTime.parse(leftAt.toString(), DateTimeFormatter.ofPattern("HH:mm"));

        }

        CheckInOut c = new CheckInOut(employee, SimpleDate.fromLocalDate(localDate));
        c.checkIn(SimpleTime.fromLocalTime(localIn));
        if (localOut != null)
        {
            c.checkOut(SimpleTime.fromLocalTime(localOut));
        }

        return c;
    }
}
