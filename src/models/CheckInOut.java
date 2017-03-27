package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lib.SimpleDate;
import lib.SimpleTime;

/**
 * Created by Robin on 27/03/2017.
 */
public class CheckInOut {
    private static HashMap<SimpleDate, Integer> totalChecksInPerDay = new HashMap<>();
    private static HashMap<SimpleDate, Integer> totalChecksOutPerDay = new HashMap<>();

    private SimpleTime arrivedAt;
    private SimpleTime leftAt;
    private SimpleDate date;
    private Employee employee;

    public CheckInOut(Employee employee, SimpleDate date)
    {
        this.employee = employee;
        this.date = date;
    }

    public CheckInOut(Employee employee, SimpleDate date, SimpleTime time)
    {
        this.employee = employee;
        this.date = date;
    }

    public CheckInOut(Employee employee)
    {
        this.employee = employee;
    }

    public static int getTotalChecksIn()
    {
        int count = 0;
        for(Map.Entry<SimpleDate, Integer> entry : totalChecksInPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    public static int getTotalChecksOut()
    {
        int count = 0;
        for(Map.Entry<SimpleDate, Integer> entry : totalChecksOutPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    public static int getTotalChecks()
    {
        return getTotalChecksIn() + getTotalChecksOut();
    }

    public static int getTotalChecksInAt(Date date)
    {
        return totalChecksInPerDay.get(date);
    }

    public static int getTotalChecksOutAt(Date date)
    {
        return totalChecksOutPerDay.get(date);
    }

    public static int getTotalChecksAt(Date date)
    {
        return totalChecksOutPerDay.get(date) + totalChecksInPerDay.get(date);
    }

    public SimpleTime getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(SimpleTime arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public SimpleTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(SimpleTime leftAt) {
        this.leftAt = leftAt;
    }

    public SimpleDate getDate() {
        return date;
    }

    public void setDate(SimpleDate date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void check(SimpleTime time)
    {
        if(arrivedAt == null)
        {
            arrivedAt = time;
        }
        else
        {
            leftAt = time;
        }
    }
}
