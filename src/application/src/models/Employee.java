package models;

import lib.csv.CSVLine;
import lib.csv.CSVParser;
import lib.csv.interfaces.CSVBuilder;
import lib.exceptions.ModelException;
import lib.exceptions.codes.EmployeeCodes;
import lib.json.Jsonable;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import static models.StandardDepartment.JSON_KEY_MANAGER;

/**
 * Created by Robin on 27/03/2017.<br>
 * This class represents an employee of the company. <br>
 */
public class Employee extends Person implements Jsonable, CSVBuilder
{
    /** JSON key for ID value */
    protected static final String JSON_KEY_ID = "id";
    /** JSON key for {@link CheckInOut} array */
    protected static final String JSON_KEY_CHECKS = "checks";
    /** JSON key for starting hour value */
    protected static final String JSON_KEY_STARTING_HOUR = "startingHour";
    /** JSON key for ending hour value */
    protected static final String JSON_KEY_ENDING_HOUR = "endingHour";

    /** Default employee's starting hour = 9:00 */
    public static final SimpleTime DEFAULT_STARTING_HOUR = SimpleTime.of(9, 0);
    /** Default employee's ending hour = 17:00 */
    public static final SimpleTime DEFAULT_ENDING_HOUR = SimpleTime.of(17, 0);

    /** Next employee ID. <br> This value is always greater than the ID of every employees */
    private static int NEXT_ID = 1;


    /** Employee's ID. <br> Each employee has a unique ID */
    private IntegerProperty id = new SimpleIntegerProperty(this, "id", -1);

    /** The time when the employee must arrive at work every morning */
    private ObjectProperty<SimpleTime> startingHour = new SimpleObjectProperty<>(this, "startingHour", DEFAULT_STARTING_HOUR);

    /** The time when the employee must leave every day */
    private ObjectProperty<SimpleTime> endingHour = new SimpleObjectProperty<>(this, "endingHour", DEFAULT_ENDING_HOUR);

    /** The working overtime in hours (can be < 0) */
    private DoubleProperty overtime = new SimpleDoubleProperty(this, "overtime", 0);

    /** The department where this employee is working */
    private ObjectProperty<StandardDepartment> department = new SimpleObjectProperty<>(this, "department", null);

    /** A list of all {@link CheckInOut} instances. */
    private ObservableList<CheckInOut> checksInOut = FXCollections.observableArrayList();


    /**
     * 2 parameters constructor
     *
     * @param firstName the first-name of the employee
     * @param lastName  the last-name of the employee
     */
    public Employee (@NotNull String firstName, @NotNull String lastName)
    {
        super(firstName, lastName);
        this.id.setValue(Employee.NEXT_ID);
        Company.getCompany().addEmployee(this);
        Employee.updateNextId();
    }

    /**
     * 2 parameters constructor
     *
     * @param firstName the first-name of the employee
     * @param lastName  the last-name of the employee
     * @param id        the ID to give to this employee
     */
    public Employee (@NotNull String firstName, @NotNull String lastName, int id)
    {
        super(firstName, lastName);

        if (Company.getCompany().getEmployee(id) != null)
        {
            System.err.print("An employee with the id " + id + " already exists... ");
            id = NEXT_ID;
            System.err.println("The employee was created with id " + id + " instead.");
        }


        this.id.setValue(id);
        Company.getCompany().addEmployee(this);
        if (id >= Employee.NEXT_ID)
        {
            Employee.updateNextId();
        }
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

    /**
     * Get the ending hour property which can used for bindings
     *
     * @return the ending hour property
     */
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
    public Employee setEndingHour (@NotNull SimpleTime endingHour)
    {
        this.endingHour.setValue(endingHour);
        return this;
    }


    /**
     * Get the employee's starting hour value
     *
     * @return the employee's starting hour value
     */
    public SimpleTime getStartingHour ()
    {
        return startingHour.getValue();
    }

    /**
     * Get the starting hour property which can used for bindings
     *
     * @return the starting hour property
     */
    public ObjectProperty<SimpleTime> startingHourProperty ()
    {
        return startingHour;
    }

    /**
     * Set the employee's starting hour
     *
     * @param startingHour the employee's starting hour
     */
    public void setStartingHour (@NotNull SimpleTime startingHour)
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

    /**
     * Get the check in time property at a specific date
     *
     * @param date the date of check
     * @return the check in time property
     */
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

    /**
     * Get the check out time property at a specific date
     *
     * @param date the date of check
     * @return the check out time property
     */
    public ObjectProperty<SimpleTime> leavingTimePropertyAt (SimpleDate date)
    {
        CheckInOut o = getCheckInOutAt(date);
        return o == null ? null : o.leftAtProperty();
    }

    /**
     * Get the id property which can be used for bindings
     *
     * @return id property
     */
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
     * Get the total overtime of this employee <br>
     * For example, if he came at 8:15 on one day while he's supposed to fzbfiylzebiuzbflmezjf at 8:00, <code>overtime = -15</code>.
     *
     * @return
     */
    public double getOvertime ()
    {
        return overtime.getValue();
    }

    /**
     * Get the overtime (in hours) property which can used for bindings
     *
     * @return the overtime (in hours) property
     */
    public DoubleProperty overtimeProperty ()
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
        if (checkInTime == null)
        {
            throw new ModelException(EmployeeCodes.NO_CHECK_IN_THIS_DATE);
        }
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
        if (checkOutTime == null)
        {
            throw new ModelException(EmployeeCodes.NO_CHECK_OUT_THIS_DATE);
        }
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

    /**
     * Update the NEXT_ID value in order to have a unique ID for each employees
     */
    private static void updateNextId ()
    {
        final ObservableList<Employee> employees = Company.getCompany().getEmployeesList();
        NEXT_ID = employees.get(employees.size() - 1).getId() + 1;
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
     * Modify the department of the employee <br>
     * <b>WARNING: this method is unsafe and should only be used by StandardDepartment's methods.</b>
     *
     * @param department the new department
     * @return this
     */
    Employee setDepartment (StandardDepartment department)
    {
        this.department.setValue(department);
        return this;
    }

    /**
     * Get the department object property which can used for bindings
     *
     * @return the department object property
     */
    public ObjectProperty<StandardDepartment> departmentProperty ()
    {
        return department;
    }

    /**
     * Fire an employee
     *
     * @return this
     */
    public Employee fire () throws Exception
    {
        Company.getCompany().removeEmployee(this);

        checksInOut.stream().filter(check -> check.getArrivedAt() != null).forEach(check ->
        {
            Company.getCompany().decrementChecksInAt(check.getDate());
            if (check.getLeftAt() != null)
            {
                Company.getCompany().decrementChecksOutAt(check.getDate());
            }
        });

        return this;
    }

    /**
     * Make an employee becoming an employee. <br>
     * A new instance is created with the same attribute values (including ID)
     *
     * @return The employee as a new manager
     */
    public Manager upgradeToManager ()
    {
        return Company.getCompany().getManagementDepartment().addEmployeeAsManager(this);
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

    /**
     * Get the list of checks
     *
     * @return the list of checks
     */
    public ObservableList<CheckInOut> getChecksInOut ()
    {
        return checksInOut;
    }

    /**
     * Modify the list of checks in out
     *
     * @param checks the new list of checks in out
     */
    void setChecksInOut (@NotNull ObservableList<CheckInOut> checks)
    {
        // In order to notify view of the change
        checksInOut.clear();
        checksInOut.addAll(checks);
    }

    /**
     * Makes the employee perform a check in or out at a date <br>
     * If the employees already had checked in (if there's already {@link CheckInOut} instance in the list
     * with the same date), a check out is performed. Otherwise, a check in is performed.
     *
     * @param dateTime The date and time of the check
     * @return this
     */
    public Employee doCheck (@NotNull SimpleDateTime dateTime)
    {
        SimpleDate date = SimpleDate.fromSimpleDateTime(dateTime);
        SimpleTime time = SimpleTime.fromSimpleDateTime(dateTime);

        CheckInOut check = getCheckInOutAt(date);
        int        index = checksInOut.indexOf(check);

        // If there already is a CheckInOut instance associate with this Employee...
        if (check != null) // Check in
        {
            if (check.getLeftAt() == null)
            {
                check.check(dateTime);
                checksInOut.set(index, check);
                //            checksInOut.

                // We update the additional working time
                double tmp = overtime.getValue();
                tmp -= ((double) endingHour.getValue().diff(time)) / 60.0;
                overtime.setValue(tmp);
            }
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
        //        return "Employee n°" + id.getValue() + " : " + super.toString();
        return id.getValue() + " - " + super.toString();
    }


    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json        = super.toJson();
        JSONArray  checksArray = new JSONArray();

        String startingHourStr = startingHour.getValue() == null ? null : startingHour.getValue()
                                                                                      .toLocalTime()
                                                                                      .format(DateTimeFormatter.ofPattern("HH:mm"));
        String endingHourStr = endingHour.getValue() == null ? null : endingHour.getValue()
                                                                                .toLocalTime()
                                                                                .format(DateTimeFormatter.ofPattern("HH:mm"));

        json.put(JSON_KEY_ID, id.getValue());
        json.put(JSON_KEY_STARTING_HOUR, startingHourStr);
        json.put(JSON_KEY_ENDING_HOUR, endingHourStr);
        if (this instanceof Manager)
        {
            json.put(JSON_KEY_MANAGER, true);
        }

        checksArray.addAll(checksInOut.stream().map(CheckInOut::toJson).collect(Collectors.toList()));

        json.put(JSON_KEY_CHECKS, checksArray);

        return json;
    }

    /**
     * Load an employees based on a JSON object. <br>
     *
     * @param json the json object containing the employee's data
     * @return the created employee
     */
    public static Employee loadFromJson (@NotNull JSONObject json)
    {
        String fName = json.get(JSON_KEY_FIRSTNAME).toString();
        String lName = json.get(JSON_KEY_LASTNAME).toString();
        int    id    = Integer.parseInt(json.get(JSON_KEY_ID).toString());

        Employee employee;
        if (json.get(JSON_KEY_MANAGER) != null)
        {
            employee = new Manager(fName, lName, id);
        }
        else
        {
            employee = new Employee(fName, lName, id);
        }

        JSONArray checks = (JSONArray) json.get(JSON_KEY_CHECKS);
        for (Object obj : checks)
        {
            CheckInOut check = CheckInOut.loadFromJson(employee, (JSONObject) obj);
            employee.checksInOut.add(check);
        }

        return employee;
    }

    /**
     * Build {@link CSVLine} instances containg all the employee's data, including checks,
     * and add them to the {@link CSVParser} passed as a parameter
     *
     * @param parser the parser to fill
     */
    @Override
    public void buildCSV (@NotNull CSVParser parser)
    {
        CSVLine line = new CSVLine();
        line.add(getId(), getFirstName(), getLastName(), this instanceof Manager, getStartingHour(), getEndingHour(), getOvertime());
        parser.addLine(line);

        for (CheckInOut check : checksInOut)
        {
            CSVLine checkLine = new CSVLine();
            checkLine.add(check.getDate());
            checkLine.add(check.getArrivedAt());
            if (check.getLeftAt() != null)
            {
                checkLine.add(check.getLeftAt());
            }

            parser.addLine(checkLine);
        }
    }
}
