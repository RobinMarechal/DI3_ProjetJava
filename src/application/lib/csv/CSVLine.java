package application.lib.csv;

import java.util.ArrayList;

/**
 * Created by Robin on 04/06/2017.
 */
public class CSVLine extends ArrayList<String> implements CharSequence
{
    public static final String SEPARATOR = ",";

    public CSVLine ()
    {
    }

    public CSVLine (String line)
    {
        super();
        add(line);
    }

    public void add (Object... objects)
    {
        for (Object obj : objects)
        {
            super.add(obj.toString());
        }
    }

    @Override
    public String toString ()
    {
        String line = "";

        if (!isEmpty())
        {
            line += get(0);
            for (int i = 1; i < size(); i++)
            {
                line += SEPARATOR + get(i);
            }
        }

        return line;
    }

    public int getNbOfElements()
    {
        return size();
    }

    @Override
    public int length ()
    {
        return toString().length();
    }

    @Override
    public char charAt (int index)
    {
        return toString().charAt(index);
    }

    @Override
    public CharSequence subSequence (int start, int end)
    {
        return toString().subSequence(start, end);
    }

    public static CSVLine fromString (String line)
    {
        String[] split = line.split(SEPARATOR);
        CSVLine  csvLine = new CSVLine();

        for (String value : split)
        {
            csvLine.add(value.trim());
        }

        return csvLine;
    }
}
