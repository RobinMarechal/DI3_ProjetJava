package lib.time;

import org.jetbrains.annotations.Contract;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Created by Robin on 24/04/2017.
 */
public class Date
{
    private DateTime dateTime;

    private Date (LocalDate localDate)
    {
        this.dateTime = DateTime.fromLocalDate(localDate);
    }

    private Date(Date date)
    {
        this.dateTime = date.dateTime;
    }

    public LocalDateTime toLocalDateTime ()
    {
        return dateTime.toLocalDateTime();
    }

    public LocalDate toLocalDate ()
    {
        return LocalDate.from(dateTime.toLocalDateTime());
    }

    public DateTime toDateTime ()
    {
        return dateTime;
    }

    public Date plusDays (int days)
    {
        dateTime = dateTime.plusDays(days);
        return this;
    }

    public Date plusMonths (int months)
    {
        dateTime = dateTime.plusMonths(months);
        return this;
    }

    public Date plusYears (int years)
    {
        dateTime = dateTime.plusYears(years);
        return this;
    }

    public Date minusDays (int days)
    {
        return this.plusDays(-days);
    }

    public Date minusMonths (int months)
    {
        return this.plusMonths(-months);
    }

    public Date minusYears (int years)
    {
        return this.plusYears(-years);
    }

    public int getDayOfYear ()
    {
        return dateTime.getDayOfYear();
    }

    public int getDayOfMonth ()
    {
        return dateTime.getDayOfMonth();
    }

    public DayOfWeek getDayOfWeek ()
    {
        return dateTime.getDayOfWeek();
    }

    public Month getMonth ()
    {
        return dateTime.getMonth();
    }

    public int getMonthValue ()
    {
        return dateTime.getMonthValue();
    }

    public int getYear ()
    {
        return dateTime.getYear();
    }

    // Statics

    @Contract ("_, _, _ -> !null")
    public static Date of (int years, int month, int days)
    {
        return new Date(LocalDate.of(years, month, days));
    }

    @Contract ("_ -> !null")
    public static Date fromLocalDate (LocalDate localDate)
    {
        return new Date(localDate);
    }

    @Contract ("_ -> !null")
    public static Date fromLocalDateTime (LocalDateTime localDateTime)
    {
        return fromLocalDate(localDateTime.toLocalDate());
    }

    @Contract ("_ -> !null")
    public static Date fromDateTime (DateTime dateTime)
    {
        return of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
    }

    @Override
    public boolean equals (Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Date date = (Date) o;

        return dateTime != null ? dateTime.equals(date.dateTime) : date.dateTime == null;

    }

    @Override
    public int hashCode ()
    {
        if(dateTime == null)
        {
            return 0;
        }


        return dateTime.toLocalDate().hashCode();
    }

    @Override
    public String toString ()
    {
        String year = "" + getYear();
        String month = "" + getMonthValue();
        String day = "" + getDayOfMonth();

        if (month.length() < 2)
        {
            month = "0" + month;
        }

        if (day.length() < 2)
        {
            day = "0" + day;
        }

        return year + "-" + month + "-" + day;
    }
}
