package models;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.HashMap;

/**
 * Created by Robin on 27/03/2017.
 */
public class Employee extends Person {
    private static int NEXT_ID = 1;

    private int id;
    private LocalTime startingHour;
    private LocalTime endingHour;

    // Relations
    private StandardDepartment department;
    private HashMap<LocalDate, CheckInOut> checksInOut = new HashMap<>();

    public Employee(String firstName, String lastName)
    {
        super(firstName, lastName);
        this.id = Employee.NEXT_ID;
        Employee.NEXT_ID++;
        Company.getCompany().addEmployee(this);
    }

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

    public LocalTime getStartingHour() {
        return startingHour;
    }

    public Employee setStartingHour(LocalTime startingHour) {
        this.startingHour = startingHour;
        return this;
    }

    public LocalTime getEndingHour() {
        return endingHour;
    }

    public Employee setEndingHour(LocalTime endingHour) {
        this.endingHour = endingHour;
        return this;
    }

    public Employee setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() { return this.id; }

    public boolean arrivedLateAt(LocalDate date)
    {
        return false;
    }

    public boolean leftEarlierAt(LocalDate date) { return false; }

    public static int getNextId() {
        return NEXT_ID;
    }

    public VirtualDepartment getDepartment() {
        return department;
    }

    public Employee setDepartment(StandardDepartment department) {
        if(department == this.department)
        {
            return this;
        }

        if(this.department != null)
        {
            this.department.removeEmployee(this);
        }

        if(department != null)
        {
            department.addEmployee(this);
        }

        this.department = department;
        return this;
    }

    public Employee fire()
    {
        Company.getCompany().removeEmployee(this);

        if(this.department != null)
        {
            this.department.removeEmployee(this);
        }

        return this;
    }

    public CheckInOut getCheckInOutAt(LocalDate date)
    {
        return checksInOut.get(date);
    }

    // DO CHECKS at some day, arrived/left at some hour
    public Employee doCheck(LocalDate date, LocalTime time)
    {
        // If there already is a CheckInOut instance associate with this Employee...
        if(checksInOut.containsKey(date))
        {
            // We simply modify it
            checksInOut.get(date).check(time);
        }
        else
        {
            // otherwise we create one
            checksInOut.put(date, new CheckInOut(this, date, time));
        }
        return this;
    }

    @Override
    public String toString()
    {
        return "Employee n°" + id + " : " + super.toString();
    }

}
