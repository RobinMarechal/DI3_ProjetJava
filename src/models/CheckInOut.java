package models;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Robin on 27/03/2017.
 */
public class CheckInOut {
    private static HashMap<LocalDate, Integer> totalChecksInPerDay = new HashMap<>();
    private static HashMap<LocalDate, Integer> totalChecksOutPerDay = new HashMap<>();

    private LocalTime arrivedAt;
    private LocalTime leftAt;
    private LocalDate date;
    private Employee employee;

    public CheckInOut(Employee employee, LocalDate date)
    {
        this.employee = employee;
        this.date = date;
    }

    public CheckInOut(Employee employee, LocalDate date, LocalTime time)
    {
        this.employee = employee;
        this.date = date;
        this.arrivedAt = time;

        checkIn(time);
    }

    public CheckInOut(Employee employee)
    {
        this.employee = employee;
    }

    public static int getTotalChecksIn()
    {
        int count = 0;
        for(Map.Entry<LocalDate, Integer> entry : totalChecksInPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    public static int getTotalChecksOut()
    {
        int count = 0;
        for(Map.Entry<LocalDate, Integer> entry : totalChecksOutPerDay.entrySet())
        {
            count += entry.getValue();
        }
        return count;
    }

    public static int getTotalChecks()
    {
        return getTotalChecksIn() + getTotalChecksOut();
    }

    public static int getTotalChecksInAt(LocalDate date)
    {
        return totalChecksInPerDay.get(date);
    }

    public static int getTotalChecksOutAt(LocalDate date)
    {
        return totalChecksOutPerDay.get(date);
    }

    public static int getTotalChecksAt(LocalDate date)
    {
        return totalChecksOutPerDay.get(date) + totalChecksInPerDay.get(date);
    }

    public LocalTime getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(LocalTime arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public LocalTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalTime leftAt) {
        this.leftAt = leftAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void check(LocalTime time)
    {
        if(arrivedAt == null)
        {
            checkIn(time);
        }
        else
        {
            checkOut(time);
        }
    }

    private void checkIn(LocalTime time)
    {
        arrivedAt = time;

        // If there already was a checkIn this day, increment the counter
        // otherwise, create the HashMap entry of this date
        if(totalChecksInPerDay.containsKey(date))
        {
            totalChecksInPerDay.put(date, totalChecksInPerDay.get(date) + 1);
        }
        else
        {
            totalChecksInPerDay.put(date, 1);
        }
    }

    private void checkOut(LocalTime time)
    {
        leftAt = time;

        if(totalChecksOutPerDay.containsKey(date))
        {
            totalChecksOutPerDay.put(date, totalChecksOutPerDay.get(date) + 1);
        }
        else
        {
            totalChecksOutPerDay.put(date, 1);
        }
    }

    @Override
    public String toString() {
        String str = employee.toString();
        if(arrivedAt != null)
        {
            str += " arrived at " + arrivedAt;
        }
        if(leftAt != null)
        {
            if(arrivedAt != null)
            {
                str += " and";
            }

            str += " left at " + leftAt;
        }

        str += " on the " + date;

        return str;
    }
}
