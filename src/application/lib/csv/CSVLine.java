package application.lib.csv;

import java.util.ArrayList;

/**
 * Created by Robin on 04/06/2017. <br/>
 * An object of this class represents a CSV line
 */
public class CSVLine extends ArrayList<String> implements CharSequence
{
    /** Data separator (by convention, it's a comma) */
    public static final String SEPARATOR = ",";

    /** Default Constructor */
    public CSVLine ()
    {
    }

    /**
     * Add values to the CSV line <br/>
     * toString() method is performed on each parameter
     *
     * @param objects the values to add to the line
     */
    public void add (Object... objects)
    {
        for (Object obj : objects)
        {
            super.add(obj.toString());
        }
    }

    /**
     * Create the CSV line represented (each value separated by a SEPARATOR)
     *
     * @return the string created
     */
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

    /**
     * Get the number of values contained in the line
     *
     * @return the number of values contained in the line
     */
    public int getNbOfElements ()
    {
        return size();
    }

    /**
     * Get the length of the line, in String format
     *
     * @return the length of the line, in String format
     */
    @Override
    public int length ()
    {
        return toString().length();
    }

    /**
     * Get a character at a specific position of the line, in String format
     *
     * @param index the index of the character
     * @return a character at a specific position of the line, in String format
     */
    @Override
    public char charAt (int index)
    {
        return toString().charAt(index);
    }

    /**
     * Get a subsequence of the line, in String format
     *
     * @param start the start index of the sub sequence (included)
     * @param end   the end index of the sub sequence (excluded)
     * @return the sub sequence
     */
    @Override
    public CharSequence subSequence (int start, int end)
    {
        return toString().subSequence(start, end);
    }

    /**
     * Create a {@link CSVLine} object from a String
     * @param line the line to parse
     * @return the {@link CSVLine} instance that contains the parsed data
     */
    public static CSVLine fromString (String line)
    {
        String[] split   = line.split(SEPARATOR);
        CSVLine  csvLine = new CSVLine();

        for (String value : split)
        {
            csvLine.add(value.trim());
        }

        return csvLine;
    }
}
