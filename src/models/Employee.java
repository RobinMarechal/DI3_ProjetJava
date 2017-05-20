package models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lib.exceptions.ModelException;
import lib.exceptions.codes.EmployeeCodes;
import lib.json.JsonSaver;
import lib.json.Jsonable;
import lib.time.SimpleDate;
import lib.time.SimpleDateTime;
import lib.time.SimpleTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by Robin on 27/03/2017.
 */
public class Employee extends Person implements JsonSaver, Jsonable
{

    /** Next employee ID */
    private static int NEXT_ID = 1;


    /** Employee's ID */
    private IntegerProperty id = new SimpleIntegerProperty(this, "id", -1);

    /** The time when the employee must arrive at work every morning */
    private ObjectProperty<SimpleTime> startingHour = new SimpleObjectProperty<>(this, "startingHour", SimpleTime.of(8, 0));

    /** The time when the employee must leave every day */
    private ObjectProperty<SimpleTime> endingHour = new SimpleObjectProperty<>(this, "endingHour", SimpleTime.of(17, 0));

    /** The additional time in minutes (can be < 0) */
    private DoubleProperty overtime = new SimpleDoubleProperty(this, "overtime", 0);

    /** The department where this employee is working */
    private ObjectProperty<StandardDepartment> department = new SimpleObjectProperty<>(this, "department", null);

    /** A list of al checks per date. */
    private ObservableList<CheckInOut> checksInOut = FXCollections.observableArrayList();


    /**
     * 2 parameters constructor
     *
     * @param firstName the first-name of the employee
     * @param lastName  the last-name of the employee
     */
    public Employee (String firstName, String lastName)
    {
        super(firstName, lastName);
        this.id.setValue(Employee.NEXT_ID);
        Employee.NEXT_ID++;
        Company.getCompany().addEmployee(this);
    }

    /**
     * 2 parameters constructor
     *
     * @param firstName the first-name of the employee
     * @param lastName  the last-name of the employee
     * @param id        the ID to give to this employee
     * @throws Exception if there already is an employee with this ID
     */
    public Employee (String firstName, String lastName, int id) throws Exception
    {
        super(firstName, lastName);

        if (Company.getCompany().getEmployee(id) != null)
        {
            throw new Exception("There already is an employee with id " + id + ".");
        }

        if (id > Employee.NEXT_ID)
        {
            Employee.NEXT_ID = id + 1;
        }

        this.id.setValue(id);
        Company.getCompany().addEmployee(this);
    }

    /**
     * Retrieve the ending hour attribute
     *
     * @return the ending hour
     */
    public SimpleTime getEndingHour ()
    {
        return endingHour.getValue();
    }

    public ObjectProperty<SimpleTime> endingHourProperty ()
    {
        return endingHour;
    }

    /**
     * Modify the ending hour
     *
     * @param endingHour the new day ending hour
     * @return this
     */
    public Employee setEndingHour (SimpleTime endingHour)
    {
        this.endingHour.setValue(endingHour);
        return this;
    }


    public SimpleTime getStartingHour ()
    {
        return startingHour.getValue();
    }

    public ObjectProperty<SimpleTime> startingHourProperty ()
    {
        return startingHour;
    }

    public void setStartingHour (SimpleTime startingHour)
    {
        this.startingHour.setValue(startingHour);
    }

    /**
     * Retrieve the time when the employee arrived at work at a specific date
     *
     * @param date the date
     * @return the time of arrival at this date
     */
    public SimpleTime getArrivingTimeAt (SimpleDate date)
    {
        CheckInOut o = getCheckInOutAt(date);
        return o == null ? null : o.getArrivedAt();
    }

    public ObjectProperty<SimpleTime> arrivingTimePropertyAt (SimpleDate date)
    {
        CheckInOut o = getCheckInOutAt(date);
        return o == null ? null : o.arrivedAtProperty();
    }

    /**
     * Retrieve the time when the employee left work at a specific date
     *
     * @param date the date
     * @return the time of leaving at this date
     */
    public SimpleTime getLeavingTimeAt (SimpleDate date)
    {
        CheckInOut o = getCheckInOutAt(date);
        return o == null ? null : o.getLeftAt();
    }

    public ObjectProperty<SimpleTime> leavingTimePropertyAt (SimpleDate date)
    {
        CheckInOut o = getCheckInOutAt(date);
        return o == null ? null : o.leftAtProperty();
    }

    public IntegerProperty idProperty ()
    {
        return id;
    }

    /**
     * Retrieve the ID of the employee
     *
     * @return the ID of the employee
     */
    public int getId ()
    {
        return this.id.getValue();
    }

    /**
     * Get the total overtime of this employee <br/>
     * For example, if he came at 8:15 on one day while he's supposed to start at 8:00, <code>overtime = -15</code>.
     *
     * @return
     */
    public double getOvertime ()
    {
        return overtime.getValue();
    }

    public void setOvertime (double overtime)
    {
        this.overtime.setValue(overtime);
    }

    public DoubleProperty overtimeProperty()
    {
        return overtime;
    }

    /**
     * Verify if the employee arrived on time, late or early at work at a specific date
     *
     * @param date the date of work
     * @return 0 : the employee was on time <br> 1 : the employee came early <br> -1 : the employee came late
     */
    public int verifyCheckInTimeAt (SimpleDate date) throws ModelException
    {
        final SimpleTime checkInTime = getArrivingTimeAt(date);
        if(checkInTime == null)
            throw new ModelException(EmployeeCodes.NO_CHECK_IN_THIS_DATE);
        return startingHour.getValue().compareTo(checkInTime);
    }

    /**
     * Verify if the employee left on time, late or early at a specific date
     *
     * @param date the date of work
     * @return 0 : the employee left on time <br> -1 : the employee left early <br> 1 : the employee left late
     */
    public int verifyCheckOutTimeAt (SimpleDate date) throws ModelException
    {
        final SimpleTime checkOutTime = getLeavingTimeAt(date);
        if(checkOutTime == null)
            throw new ModelException(EmployeeCodes.NO_CHECK_OUT_THIS_DATE);
        return -endingHour.getValue().compareTo(checkOutTime);
    }

    /**
     * Retrieve the automatic next employee ID
     *
     * @return the next employee ID
     */
    public static int getNextId ()
    {
        return NEXT_ID;
    }

    static void setNextId (int nextId)
    {
        NEXT_ID = nextId;
    }

    /**
     * Retrieve the department where this employee is working
     *
     * @return the department where this employee is working
     */
    public StandardDepartment getDepartment ()
    {
        return department.getValue();
    }

    /**
     * Modify the department of the employee
     *
     * @param department the new department
     * @return this
     * @warning this method is unsafe and should only be used by StandardDepartment's methods.
     */
    protected Employee setDepartment (StandardDepartment department)
    {
        this.department.setValue(department);
        return this;
    }

    public ObjectProperty<StandardDepartment> departmentProperty ()
    {
        return department;
    }

    /**
     * Fire an employee
     *
     * @return this
     */
    public Employee fire ()
    {
        Company.getCompany().removeEmployee(this);

        return this;
    }

    /**
     * Retrieve the CheckInOut of a specific date
     *
     * @param date the date of check
     * @return The CheckInOut at this date
     */
    public CheckInOut getCheckInOutAt (SimpleDate date)
    {
        final Optional<CheckInOut> streamFound = checksInOut.stream().filter(c -> c.getDate().equals(date)).findFirst();
        if (!streamFound.isPresent())
        {
            return null;
        }

        return streamFound.get();
    }

    public ObservableList<CheckInOut> getChecksInOut ()
    {
        return checksInOut;
    }

    /**
     * Makes the employee perform a check in or out.
     *
     * @param dateTime The datetime of the check
     * @return this
     */
    public synchronized Employee doCheck (SimpleDateTime dateTime)
    {
        SimpleDate date = SimpleDate.fromSimpleDateTime(dateTime);
        SimpleTime time = SimpleTime.fromSimpleDateTime(dateTime);

        CheckInOut check = getCheckInOutAt(date);
        int index = checksInOut.indexOf(check);

        // If there already is a CheckInOut instance associate with this Employee...
        if (check != null) // Check in
        {
            check.check(dateTime);
            checksInOut.set(index, check);
            //            checksInOut.

            // We update the additional working time
            double tmp = overtime.getValue();
            tmp -= ((double) endingHour.getValue().diff(time)) / 60.0;
            overtime.setValue(tmp);
        }
        else // Check out
        {
            // otherwise we create one
            check = new CheckInOut(this, date);
            check.check(dateTime);
            checksInOut.add(check);

            // We update the additional working time
            double tmp = overtime.getValue();
            tmp += ((double) startingHour.getValue().diff(time)) / 60.0;
            overtime.set(tmp);
        }

        return this;
    }

    /**
     * Creates a String from an Employee's instance
     *
     * @return
     */
    @Override
    public String toString ()
    {
        return "Employee n°" + id.getValue() + " : " + super.toString();
    }

    /**
     * Save an Employee instance into a json file.
     */
    @Override
    public void save ()
    {
        String path = "data\\files\\employees";
        String filename = this.id.getValue() + ".json";
        saveToFile(path, filename, toJson());
    }

    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = super.toJson();
        JSONArray checksArray = new JSONArray();

        String startingHourStr = startingHour.getValue() == null ? null : startingHour.getValue()
                                                                                      .toLocalTime()
                                                                                      .format(DateTimeFormatter.ofPattern("HH:mm"));
        String endingHourStr = endingHour.getValue() == null ? null : endingHour.getValue()
                                                                                .toLocalTime()
                                                                                .format(DateTimeFormatter.ofPattern("HH:mm"));

        json.put("id", id.getValue());
        json.put("startingHour", startingHourStr);
        json.put("endingHour", endingHourStr);
        json.put("manager", false);

        for (CheckInOut check : checksInOut)
        {
            checksArray.add(check.toJson());
        }

        json.put("checks", checksArray);

        return json;
    }
}
