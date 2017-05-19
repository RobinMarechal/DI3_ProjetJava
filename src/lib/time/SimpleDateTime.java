package lib.time;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Created by Robin on 24/04/2017.
 */
public class SimpleDateTime implements Serializable, Comparable<SimpleDateTime>
{
    public static final SimpleDateTime NOW = SimpleDateTime.fromLocalDateTime(LocalDateTime.now());

    private LocalDateTime localDateTime;

    private SimpleDateTime (LocalDateTime localDateTime)
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

    public SimpleDateTime plusMinutes (int minutes)
    {
        LocalDateTime ldt = toLocalDateTime();
        ldt = ldt.plusMinutes(minutes);
        SimpleDateTime result = SimpleDateTime.fromLocalDateTime(ldt);
        return result;
    }

    public SimpleDateTime plusHours (int hours)
    {
        LocalDateTime ldt = toLocalDateTime();
        ldt = ldt.plusHours(hours);
        SimpleDateTime result = SimpleDateTime.fromLocalDateTime(ldt);
        return result;
    }

    public SimpleDateTime plusDays (int days)
    {
        LocalDateTime ldt = toLocalDateTime();
        ldt = ldt.plusDays(days);
        SimpleDateTime result = SimpleDateTime.fromLocalDateTime(ldt);
        return result;
    }

    public SimpleDateTime plusMonths (int months)
    {
        LocalDateTime ldt = toLocalDateTime();
        ldt = ldt.plusMonths(months);
        SimpleDateTime result = SimpleDateTime.fromLocalDateTime(ldt);
        return result;
    }

    public SimpleDateTime plusYears (int years)
    {
        LocalDateTime ldt = toLocalDateTime();
        ldt = ldt.plusYears(years);
        SimpleDateTime result = SimpleDateTime.fromLocalDateTime(ldt);
        return result;
    }

    public SimpleDateTime minusMinutes (int minutes)
    {
        return this.plusMinutes(-minutes);
    }

    public SimpleDateTime minusHours (int hours)
    {
        return this.plusHours(-hours);
    }

    public SimpleDateTime minusDays (int days)
    {
        return this.plusDays(-days);
    }

    public SimpleDateTime minusMonths (int months)
    {
        return this.plusMonths(-months);
    }

    public SimpleDateTime minusYears (int years)
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

    public SimpleTime toSimpleTime ()
    {
        return SimpleTime.fromSimpleDateTime(this);
    }

    public SimpleDate toSimpleDate ()
    {
        return SimpleDate.fromSimpleDateTime(this);
    }


    /**
     * Calculates the minutes of differences between to SimpleTime <br />
     * Example : {"8:00"}.until({"8:15"}) => -15 <br/>
     * Example : {"8:30"}.until({"8:00"}) => 30 <br/>
     *
     * @param dateTime the datetime to compare
     * @return >0 if the object if after the param, else <0 if the param is after the object, else 0 if both are equals
     */
    public long until (SimpleDateTime dateTime, ChronoUnit chronoUnit)
    {
        return localDateTime.until(dateTime.localDateTime, chronoUnit);
    }

    /**
     * Compares two times <br />
     * Example : {"8:00"}.until({"8:15"}) => -1 <br/>
     * Example : {"8:30"}.until({"8:00"}) => 1 <br/>
     * Example : {"8:30"}.until({"8:30"}) => 0 <br/>
     *
     * @param dateTime
     * @return 1 if the object if after the param, else -1 if the param is after the object, else 0 if both are equals
     */
    @Override
    public int compareTo(SimpleDateTime dateTime) throws NullPointerException
    {
        if(dateTime == null)
            throw new NullPointerException();

        return localDateTime.compareTo(dateTime.localDateTime);
    }

    // Statics

    @Contract ("_, _, _ -> !null")
    public static SimpleDateTime fromDateAndTime (SimpleDate date, SimpleTime time)
    {
        return new SimpleDateTime(LocalDateTime.of(date.toLocalDate(), time.toLocalTime()));
    }

    @Contract ("_, _, _ -> !null")
    public static SimpleDateTime fromSimpleDate (SimpleDate date)
    {
        return fromLocalDate(date.toLocalDate());
    }

    @Contract ("_, _, _ -> !null")
    public static SimpleDateTime fromSimpleTime (SimpleTime time)
    {
        return fromLocalTime(time.toLocalTime());
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime fromLocalTime (LocalTime localTime)
    {
        return new SimpleDateTime(LocalDateTime.of(0, 1, 1, localTime.getHour(), localTime.getMinute()));
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime fromLocalDate (LocalDate localDate)
    {
        return new SimpleDateTime(LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0));
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime fromLocalDateTime (LocalDateTime localDateTime)
    {
        return new SimpleDateTime(localDateTime);
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime of (int years, int months, int days, int hours, int minutes, int seconds)
    {
        return fromDateAndTime(SimpleDate.of(years, months, days), SimpleTime.of(hours, minutes, seconds));
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime of (int years, int months, int days, int hours, int minutes)
    {
        return of(years, months, days, hours, minutes, 0);
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime of (int years, int months, int days, int hours)
    {
        return of(years, months, days, hours, 0, 0);
    }

    @Contract ("_ -> !null")
    public static SimpleDateTime of (int years, int months, int days)
    {
        return of(years, months, days, 0, 0, 0);
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

        SimpleDateTime dateTime = (SimpleDateTime) o;

        if (localDateTime != null ? !localDateTime.equals(dateTime.localDateTime) : dateTime.localDateTime != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode ()
    {
        if (localDateTime == null)
        {
            return 0;
        }

        return localDateTime.hashCode();
    }

    @Override
    public String toString ()
    {
        return toSimpleDate() + " " + toSimpleTime();
    }
}
