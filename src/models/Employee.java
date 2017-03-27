package models;

import lib.SimpleDate;
import lib.SimpleTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Robin on 27/03/2017.
 */
public class Employee extends Person {
    private static int NEXT_ID = 1;

    private int id;
    private SimpleTime startingHour;
    private SimpleTime endingHour;

    // Relations
    private StandardDepartment department;
    private Company company = Company.getCompany();
    private HashMap<SimpleDate, CheckInOut> checksInOut = new HashMap<>();

    public Employee()
    {
        super();
        this.id = this.NEXT_ID;
        this.NEXT_ID++;
    }

    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
        this.id = this.NEXT_ID;
        this.NEXT_ID++;
    }

    public SimpleTime getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(SimpleTime startingHour) {
        this.startingHour = startingHour;
    }

    public SimpleTime getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(SimpleTime endingHour) {
        this.endingHour = endingHour;
    }

    public static int getNextId() {
        return NEXT_ID;
    }

    public boolean wasLateAt(Date date)
    {
        return false;
    }

    public VirtualDepartment getDepartment() {
        return department;
    }

    public void setDepartment(StandardDepartment department) {
        if(this.department != null)
        {
            this.department.removeEmployee(this);
        }
        department.addEmployee(this);
        this.department = department;
    }

    public CheckInOut getCheckInOutAt(SimpleDate date)
    {
        return checksInOut.get(date);
    }

    // DO CHECKS at some day, arrived/left at some hour
    public void checks(SimpleDate date, SimpleTime time)
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
    }

}
