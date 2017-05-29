package application.models;

import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.lib.exceptions.ModelException;
import application.lib.exceptions.codes.EmployeeCodes;
import application.lib.json.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import static application.models.StandardDepartment.JSON_KEY_MANAGER;

/**
 * Created by Robin on 27/03/2017.
 */
public class Employee extends Person implements Jsonable
{
    protected static final String JSON_KEY_ID = "id";
    protected static final String JSON_KEY_CHECKS = "checks";
    protected static final String JSON_KEY_STARTING_HOUR = "startingHour";
    protected static final String JSON_KEY_ENDING_HOUR = "endingHour";

    public static final SimpleTime DEFAULT_STARTING_HOUR = SimpleTime.of(8, 0);
    public static final SimpleTime DEFAULT_ENDING_HOUR = SimpleTime.of(17, 0);

    /** Next employee ID */
    private static int NEXT_ID = 1;


    /** Employee's ID */
    private IntegerProperty id = new SimpleIntegerProperty(this, "id", -1);

    /** The time when the employee must arrive at work every morning */
    private ObjectProperty<SimpleTime> startingHour = new SimpleObjectProperty<>(this, "startingHour", DEFAULT_STARTING_HOUR);

    /** The time when the employee must leave every day */
    private ObjectProperty<SimpleTime> endingHour = new SimpleObjectProperty<>(this, "endingHour", DEFAULT_ENDING_HOUR);

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
        Company.getCompany().addEmployee(this);
        Employee.updateNextId();
    }

    /**
     * 2 parameters constructor
     *
     * @param firstName the first-name of the employee
     * @param lastName  the last-name of the employee
     * @param id        the ID to give to this employee
     * @throws Exception if there already is an employee with this ID
     */
    public Employee (String firstName, String lastName, int id)
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
     * For example, if he came at 8:15 on one day while he's supposed to fzbfiylzebiuzbflmezjf at 8:00, <code>overtime = -15</code>.
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

    public static void updateNextId()
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

    public ObservableList<CheckInOut> getChecksInOut ()
    {
        return checksInOut;
    }

    void setChecksInOut (ObservableList<CheckInOut> checks)
    {
        // In order to notify view of the change
        checksInOut.clear();
        checksInOut.addAll(checks);
    }

    /**
     * Makes the employee perform a check in or out.
     *
     * @param dateTime The datetime of the check
     * @return this
     */
    public Employee doCheck (SimpleDateTime dateTime)
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

    public static Employee loadFromJson (JSONObject json)
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
}
