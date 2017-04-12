package models;

import lib.json.JsonSaver;
import lib.json.Jsonable;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Robin on 27/03/2017.
 */
public class Employee extends Person implements JsonSaver, Jsonable, Serializable
{
    /**
     * Next employee ID
     */
    private static int NEXT_ID = 1;

    /**
     * Employee's ID
     */
    private int id;

    /**
     * The time when the employee must arrive at work every morning
     */
    private LocalTime startingHour;

    /**
     * The time when the employee must leave every day
     */
    private LocalTime endingHour;

    /**
     * The department where this employee is working
     */
    private StandardDepartment department;

    /**
     * A list of al checks per date.
     */
    private HashMap<LocalDate, CheckInOut> checksInOut = new HashMap<>();


    /**
     * 2 parameters constructor
     * @param firstName the first-name of the employee
     * @param lastName the last-name of the employee
     */
    public Employee(String firstName, String lastName)
    {
        super(firstName, lastName);
        this.id = Employee.NEXT_ID;
        Employee.NEXT_ID++;
        Company.getCompany().addEmployee(this);
    }

    /**
     * 2 parameters constructor
     * @param firstName the first-name of the employee
     * @param lastName the last-name of the employee
     * @param id the ID to give to this employee
     * @throws Exception if there already is an employee with this ID
     */
    public Employee(String firstName, String lastName, int id) throws Exception
    {
        super(firstName, lastName);

        if(Company.getCompany().getEmployee(id) != null)
        {
            throw new Exception("There already is an employee with id " + id + ".");
        }

        if(id > Employee.NEXT_ID)
        {
            Employee.NEXT_ID = id + 1;
        }

        this.id = id;
        Company.getCompany().addEmployee(this);
    }

    /**
     * Retrieve the starting hour attribute
     * @return the starting hour
     */
    public LocalTime getStartingHour() {
        return startingHour;
    }

    /**
     * Modify the starting hour
     * @param startingHour the new day starting hour
     * @return this
     */
    public Employee setStartingHour(LocalTime startingHour) {
        this.startingHour = startingHour;
        return this;
    }

    /**
     * Retrieve the ending hour attribute
     * @return the ending hour
     */
    public LocalTime getEndingHour() {
        return endingHour;
    }

    /**
     * Modify the ending hour
     * @param endingHour the new day ending hour
     * @return this
     */
    public Employee setEndingHour(LocalTime endingHour) {
        this.endingHour = endingHour;
        return this;
    }

    /**
     * Retrieve the time when the employee arrived at work at a specific date
     * @param date the date
     * @return the time of arrival at this date
     */
    public LocalTime getArrivingTimeAt(LocalDate date)
    {
        if(!checksInOut.containsKey(date))
            return null;

        return checksInOut.get(date).getArrivedAt();
    }

    /**
     * Retrieve the time when the employee left work at a specific date
     * @param date the date
     * @return the time of leaving at this date
     */
    public LocalTime getLeavingTimeAt(LocalDate date)
    {
        if(!checksInOut.containsKey(date))
            return null;

        return checksInOut.get(date).getLeftAt();
    }

    /**
     * Retrieve the ID of the employee
     * @return the ID of the employee
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * To know if the employee arrived late at a specific date
     * @param date the date
     * @return true: he was late, false otherwise
     */
    public boolean arrivedLateAt(LocalDate date)
    {
        return false;
    }

    /**
     * To know if the employee arrived earlier at a specific date
     * @param date the date
     * @return true: he arrived earlier, false otherwise
     */
    public boolean arrivedEarlierAt(LocalDate date)
    {
        return false;
    }

    /**
     * To know if the employee left earlier at a specific date
     * @param date the date
     * @return true: he left earlier, false otherwise
     */
    public boolean leftEarlierAt(LocalDate date)
    {
        return false;
    }

    /**
     * To know if the employee left late at a specific date
     * @param date the date
     * @return true: he left late, false otherwise
     */
    public boolean leftLateAt(LocalDate date)
    {
        return false;
    }

    /**
     * Retrieve the automatic next employee ID
     * @return the next employee ID
     */
    public static int getNextId() {
        return NEXT_ID;
    }

    /**
     * Retrieve the department where this employee is working
     * @return the department where this employee is working
     */
    public VirtualDepartment getDepartment() {
        return department;
    }

    /**
     * Modify the department of the employee
     * @param department the new department
     * @return this
     * @warning this method should only be used by StandardDepartment's methods.
     */
    @NotNull
    protected Employee setDepartment(StandardDepartment department)
    {
        if(department == this.department)
        {
            return this;
        }

        if(this.department != null)
        {
            this.department.removeEmployee(this);
        }

        this.department = department;

        return this;
    }

    /**
     * Fire an employee
     * @return this
     */
    public Employee fire()
    {
        Company.getCompany().removeEmployee(this);

        if(this.department != null)
        {
            this.department.removeEmployee(this);
        }

        return this;
    }

    /**
     * Retrieve the CheckInOut of a specific date
     * @param date the date of check
     * @return The CheckInOut at this date
     */
    public CheckInOut getCheckInOutAt(LocalDate date)
    {
        return checksInOut.get(date);
    }


    /**
     * Makes the employee perform a check in or out.
     * @param dateTime The datetime of the check
     * @return this
     */
    public Employee doCheck(LocalDateTime dateTime)
    {
        LocalDate date = LocalDate.from(dateTime);
        LocalTime time = LocalTime.from(dateTime);

        // If there already is a CheckInOut instance associate with this Employee...
        if(checksInOut.containsKey(date))
        {
            // We simply modify it
            checksInOut.get(date).check(dateTime);
        }
        else
        {
            // otherwise we create one
            checksInOut.put(date, new CheckInOut(this, dateTime));
        }
        return this;
    }

    /**
     * Creates a String from an Employee's instance
     * @return
     */
    @Override
    public String toString()
    {
        return "Employee n°" + id + " : " + super.toString();
    }

    /**
     * Save an Employee instance into a json file.
     */
    @Override
    public void save ()
    {
        String path = "data\\files\\employees";
        String filename = this.id + ".json";
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
        JSONObject json;
        JSONArray checksArray = new JSONArray();

        String startingHourStr = startingHour == null ? null : startingHour.format(DateTimeFormatter.ofPattern("HH:mm"));
        String endingHourStr = endingHour == null ? null : endingHour.format(DateTimeFormatter.ofPattern("HH:mm"));

        json = super.toJson();

        json.put("id", id);
        json.put("startingHour", startingHourStr);
        json.put("endingHour", endingHourStr);
        json.put("manager", false);

        for(Map.Entry<LocalDate, CheckInOut> entry : checksInOut.entrySet())
        {
            CheckInOut check = entry.getValue();

            checksArray.add(check.toJson());
        }

        json.put("checks", checksArray);

        return json;
    }

    /**
     * Set the department of this employee to null.
     */
    protected void setDepartmentToNull ()
    {
        this.department = null;
    }
}
