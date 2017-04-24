package lib.time;

import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Robin on 24/04/2017.
 */
public class Time
{
    private DateTime dateTime;

    private Time (LocalTime localTime)
    {
        LocalTime lt = localTime.truncatedTo(ChronoUnit.MINUTES);
        this.dateTime = DateTime.fromLocalTime(lt);
    }

    public Time plusMinutes (int minutes)
    {
        dateTime = dateTime.plusMinutes(minutes);
        return this;
    }

    public Time plusHours (int hours)
    {
        dateTime = dateTime.plusHours(hours);
        return this;
    }

    public Time minusMinutes (int minutes)
    {
        return this.plusMinutes(-minutes);
    }

    public Time minusHours (int hours)
    {
        return this.plusHours(-hours);
    }

    public int getMinute ()
    {
        return dateTime.getMinute();
    }

    public int getHour ()
    {
        return dateTime.getHour();
    }

    public int getSecond ()
    {
        return dateTime.getSecond();
    }

    public LocalTime toLocalTime ()
    {
        return dateTime.toLocalTime();
    }

    public LocalDateTime toLocalDateTime ()
    {
        return dateTime.toLocalDateTime();
    }

    public DateTime toDateTime ()
    {
        return dateTime;
    }

    // Statics

    @Contract ("_, _, _ -> !null")
    public static Time of (int hours, int minutes, int seconds)
    {
        return new Time(LocalTime.of(hours, minutes, seconds));
    }

    @Contract ("_, _ -> !null")
    public static Time of (int hours, int minutes)
    {
        return Time.of(hours, minutes, 0);
    }

    @Contract ("_ -> !null")
    public static Time fromLocalTime (LocalTime localTime)
    {
        return new Time(localTime);
    }

    @Contract ("_ -> !null")
    public static Time fromLocalDateTime (LocalDateTime localDateTime)
    {
        return fromLocalTime(localDateTime.toLocalTime());
    }

    @Contract ("_ -> !null")
    public static Time fromDateTime (DateTime dateTime)
    {
        return of(dateTime.getHour(), dateTime.getMinute());
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

        Time time = (Time) o;

        if (dateTime == null && time.dateTime == null)
        {
            return true;
        }

        return (getMinute() == time.getMinute()) && (getHour() == time.getHour());
    }

    @Override
    public int hashCode ()
    {
        if (dateTime == null)
        {
            return 0;
        }

        return dateTime.toLocalTime().hashCode();
    }

    @Override
    public String toString ()
    {
        String hour = getHour() + "";
        String minute = getMinute() + "";

        if (hour.length() < 2)
        {
            hour = "0" + hour;
        }

        if (minute.length() < 2)
        {
            minute = "0" + minute;
        }

        return hour + ":" + minute;
    }
}
