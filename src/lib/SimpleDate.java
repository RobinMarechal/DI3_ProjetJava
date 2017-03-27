package lib;

import java.time.Instant;
import java.util.Date;

/**
 * Created by Robin on 27/03/2017.
 */
public class SimpleDate
{
    private int year;
    private int month;
    private int day;

    public SimpleDate()
    {
        this(new Date());
    }

    public SimpleDate(java.util.Date date)
    {
        this(date.toInstant());
    }

    public SimpleDate(Instant instant)
    {
        // [0] => "2017"; [1] => "03"; [2] => "26"
        String tmp[] = instant.toString().substring(0, 10).split("-");
        this.year = Integer.parseInt(tmp[0]);
        this.month = Integer.parseInt(tmp[1]);
        this.day = Integer.parseInt(tmp[2]);
    }

    @Override
    public String toString() {
        return this.year + "/" + (this.month < 10 ? "0" : "") + this.month + "/" + (this.day < 10 ? "0" : "") + this.day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDate date = (SimpleDate) o;

        if (year != date.year) return false;
        if (month != date.month) return false;
        return day == date.day;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 366 * result + month;
        result = 31 * result + day;
        return result;
    }
}
