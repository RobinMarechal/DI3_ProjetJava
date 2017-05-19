package lib.time;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Created by Robin on 24/04/2017.
 */
public class SimpleDate implements Serializable, Comparable<SimpleDate>
{
    public static final SimpleDate TODAY = SimpleDate.fromSimpleDateTime(SimpleDateTime.NOW);

    private SimpleDateTime dateTime;

    private SimpleDate (LocalDate localDate)
    {
        this.dateTime = SimpleDateTime.fromLocalDate(localDate);
    }

    private SimpleDate (SimpleDate date)
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

    public SimpleDateTime toSimpleDateTime ()
    {
        return dateTime;
    }

    public SimpleDate plusDays (int days)
    {
        SimpleDateTime sdt = dateTime.plusDays(days);
        return sdt.toSimpleDate();
    }

    public SimpleDate plusMonths (int months)
    {
        SimpleDateTime sdt = dateTime.plusMonths(months);
        return sdt.toSimpleDate();
    }

    public SimpleDate plusYears (int years)
    {
        SimpleDateTime sdt = dateTime.plusYears(years);
        return sdt.toSimpleDate();
    }

    public SimpleDate minusDays (int days)
    {
        return this.plusDays(-days);
    }

    public SimpleDate minusMonths (int months)
    {
        return this.plusMonths(-months);
    }

    public SimpleDate minusYears (int years)
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

    /**
     * Get the number of days seperating both dates
     * @param date
     * @return the number of days separating both dates. <0 : the object is before the param.
     */
    public long diff(SimpleDate date)
    {
        return (int) (toLocalDate().toEpochDay() - date.toLocalDate().toEpochDay());
    }


    /**
     * Tests if the object date is after the param date
     * @param date
     * @return the number of days separating both dates. -1 : the object is before the param.
     */
    public int compareTo(SimpleDate date) throws NullPointerException
    {
        if(date == null)
            throw new NullPointerException();

        long diff = diff(date);
        return (int) (diff == 0 ? 0 : diff / Math.abs(diff));
    }

    // Statics

    @Contract ("_, _, _ -> !null")
    public static SimpleDate of (int years, int month, int days)
    {
        return new SimpleDate(LocalDate.of(years, month, days));
    }

    @Contract ("_ -> !null")
    public static SimpleDate fromLocalDate (LocalDate localDate)
    {
        return new SimpleDate(localDate);
    }

    @Contract ("_ -> !null")
    public static SimpleDate fromLocalDateTime (LocalDateTime localDateTime)
    {
        return fromLocalDate(localDateTime.toLocalDate());
    }

    @Contract ("_ -> !null")
    public static SimpleDate fromSimpleDateTime (SimpleDateTime dateTime)
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

        SimpleDate date = (SimpleDate) o;

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
