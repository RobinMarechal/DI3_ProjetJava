package lib.time;

import org.jetbrains.annotations.Contract;

import java.time.*;

/**
 * Created by Robin on 24/04/2017.
 */
public class DateTime
{
    private LocalDateTime localDateTime;

    private DateTime (LocalDateTime localDateTime)
    {
        this.localDateTime = localDateTime;
        roundToNearestQuarter();
    }

    public LocalDate toLocalDate ()
    {
        return localDateTime.toLocalDate();
    }

    public LocalTime toLocalTime ()
    {
        return localDateTime.toLocalTime();
    }


    /**
     * Rounds the time to the nearest quarter <br/>
     * Ex: 8h07 => 8h00; 8h08 => 8h15
     */
    public void roundToNearestQuarter ()
    {
        int minutes = localDateTime.getMinute();
        int minutesInQuarter = minutes % 15;

        if (minutesInQuarter != 0)
        {
            int minutesToAdd = -minutesInQuarter;
            if (minutesInQuarter > 7)
            {
                minutesToAdd += 15;
            }

            localDateTime = localDateTime.plusMinutes(minutesToAdd);
        }
    }

    public DateTime plusMinutes (int minutes)
    {
        localDateTime = localDateTime.plusMinutes(minutes);
        roundToNearestQuarter();
        return this;
    }

    public DateTime plusHours (int hours)
    {
        localDateTime = localDateTime.plusHours(hours);
        roundToNearestQuarter();
        return this;
    }

    public DateTime plusDays (int days)
    {
        localDateTime = localDateTime.plusDays(days);
        roundToNearestQuarter();
        return this;
    }

    public DateTime plusMonths (int months)
    {
        localDateTime = localDateTime.plusMonths(months);
        roundToNearestQuarter();
        return this;
    }

    public DateTime plusYears (int years)
    {
        localDateTime = localDateTime.plusYears(years);
        roundToNearestQuarter();
        return this;
    }

    public DateTime minusMinutes (int minutes)
    {
        return this.plusMinutes(-minutes);
    }

    public DateTime minusHours (int hours)
    {
        return this.plusHours(-hours);
    }

    public DateTime minusDays (int days)
    {
        return this.plusDays(-days);
    }

    public DateTime minusMonths (int months)
    {
        return this.plusMonths(-months);
    }

    public DateTime minusYears (int years)
    {
        return this.plusYears(-years);
    }

    public int getMinute ()
    {
        return localDateTime.getMinute();
    }

    public int getHour ()
    {
        return localDateTime.getHour();
    }

    public int getSecond ()
    {
        return localDateTime.getSecond();
    }

    public int getDayOfYear ()
    {
        return localDateTime.getDayOfYear();
    }

    public int getDayOfMonth ()
    {
        return localDateTime.getDayOfMonth();
    }

    public DayOfWeek getDayOfWeek ()
    {
        return localDateTime.getDayOfWeek();
    }

    public Month getMonth ()
    {
        return localDateTime.getMonth();
    }

    public int getMonthValue ()
    {
        return localDateTime.getMonthValue();
    }

    public int getYear ()
    {
        return localDateTime.getYear();
    }

    public LocalDateTime toLocalDateTime ()
    {
        return localDateTime;
    }

    public Time toTime ()
    {
        return Time.fromDateTime(this);
    }

    public Date toDate()
    {
        return Date.fromDateTime(this);
    }

    // Statics

    @Contract ("_, _, _ -> !null")
    public static DateTime fromDateAndTime (Date date, Time time)
    {
        return new DateTime(LocalDateTime.of(date.toLocalDate(), time.toLocalTime()));
    }

    @Contract ("_, _, _ -> !null")
    public static DateTime fromDate (Date date)
    {
        return fromLocalDate(date.toLocalDate());
    }

    @Contract ("_, _, _ -> !null")
    public static DateTime fromTime (Time time)
    {
        return fromLocalTime(time.toLocalTime());
    }

    @Contract ("_ -> !null")
    public static DateTime fromLocalTime (LocalTime localTime)
    {
        return new DateTime(LocalDateTime.of(0, 1, 1, localTime.getHour(), localTime.getMinute()));
    }

    @Contract ("_ -> !null")
    public static DateTime fromLocalDate (LocalDate localDate)
    {
        return new DateTime(LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0));
    }

    @Contract ("_ -> !null")
    public static DateTime fromLocalDateTime (LocalDateTime localDateTime)
    {
        return new DateTime(localDateTime);
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

        DateTime dateTime = (DateTime) o;

        if (localDateTime != null ? !localDateTime.equals(dateTime.localDateTime) : dateTime.localDateTime != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode ()
    {
        if(localDateTime == null)
        {
            return 0;
        }

        return localDateTime.hashCode();
    }

    @Override
    public String toString ()
    {
        return toDate() + " " + toTime();
    }
}
