package lib.time;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Robin on 24/04/2017.
 */
public class SimpleTime implements Serializable, Comparable<SimpleTime>
{
    public static final SimpleTime NOW = SimpleTime.fromSimpleDateTime(SimpleDateTime.NOW);

    private SimpleDateTime dateTime;

    private SimpleTime (LocalTime localTime)
    {
        LocalTime lt = localTime.truncatedTo(ChronoUnit.MINUTES);
        this.dateTime = SimpleDateTime.fromLocalTime(lt);
    }

    public SimpleTime plusMinutes (int minutes)
    {
        SimpleDateTime sdt = dateTime.plusMinutes(minutes);
        return sdt.toSimpleTime();
    }

    public SimpleTime plusHours (int hours)
    {
        SimpleDateTime sdt = dateTime.plusHours(hours);
        return sdt.toSimpleTime();
    }

    public SimpleTime minusMinutes (int minutes)
    {
        return this.plusMinutes(-minutes);
    }

    public SimpleTime minusHours (int hours)
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

    public SimpleDateTime toSimpleDateTime ()
    {
        return dateTime;
    }

    /**
     * Calculates the minutes of differences between to SimpleTime <br />
     * Example : {"8:00"}.until({"8:15"}) => -15 <br/>
     * Example : {"8:30"}.until({"8:00"}) => 30 <br/>
     *
     * @param time the time to compare
     * @return the separating both SimpleTime
     */
    public long diff (SimpleTime time)
    {
        return (this.toLocalTime().toSecondOfDay() - time.toLocalTime().toSecondOfDay()) / 60;
    }

    /**
     * Compares two times <br />
     * Example : {"8:00"}.until({"8:15"}) => -1 <br/>
     * Example : {"8:30"}.until({"8:00"}) => 1 <br/>
     * Example : {"8:30"}.until({"8:30"}) => 0 <br/>
     *
     * @param time
     * @return 1 if the object if after the param, else -1 if the param is after the object, else 0 if both are equals
     */
    @Override
    public int compareTo(SimpleTime time) throws NullPointerException
    {
        if(time == null)
            throw new NullPointerException();

        long diff = diff(time);
        return (int) (diff == 0 ? 0 : diff / Math.abs(diff));
    }

    // Statics

    @Contract ("_, _, _ -> !null")
    public static SimpleTime of (int hours, int minutes, int seconds)
    {
        return new SimpleTime(LocalTime.of(hours, minutes, seconds));
    }

    @Contract ("_, _ -> !null")
    public static SimpleTime of (int hours, int minutes)
    {
        return SimpleTime.of(hours, minutes, 0);
    }

    @Contract ("_ -> !null")
    public static SimpleTime fromLocalTime (LocalTime localTime)
    {
        return new SimpleTime(localTime);
    }

    @Contract ("_ -> !null")
    public static SimpleTime fromLocalDateTime (LocalDateTime localDateTime)
    {
        return fromLocalTime(localDateTime.toLocalTime());
    }

    @Contract ("_ -> !null")
    public static SimpleTime fromSimpleDateTime (SimpleDateTime dateTime)
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

        SimpleTime time = (SimpleTime) o;

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
