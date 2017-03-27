package lib;

import java.time.Instant;
import java.util.Date;

/**
 * Created by Robin on 27/03/2017.
 */
public class SimpleTime
{
    private int hours;
    private int minutes;
    private int seconds;

    public SimpleTime()
    {
        this(new Date());
    }

    public SimpleTime(java.util.Date date)
    {
        this(date.toInstant());
    }

    public SimpleTime(Instant instant)
    {
        // [0] => "15"; [1] => "23"; [2] => "32"
        String tmp[] = instant.toString().substring(12, 19).split(":");
        this.hours = Integer.parseInt(tmp[0]);
        this.minutes = Integer.parseInt(tmp[1]);
        this.seconds = Integer.parseInt(tmp[2]);
    }

    @Override
    public String toString() {
        return (this.hours < 10 ? "0" : "") + this.hours + ":" + (this.minutes < 10 ? "0" : "") + this.minutes + ":" + (this.seconds < 10 ? "0" : "") + this.seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTime time = (SimpleTime) o;

        if (hours != time.hours) return false;
        if (minutes != time.minutes) return false;
        return seconds == time.seconds;
    }

    @Override
    public int hashCode() {
        int result = hours;
        result = 24 * result + minutes;
        result = 60 * result + seconds;
        return result;
    }
}
