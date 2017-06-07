package lib.csv;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Robin on 04/06/2017. <br>
 * This class represent a list of {@link CSVLine}
 */
public class CSVParser implements Iterator<CSVLine>
{
    /** Indicate that the line is a header and should not be parsed */
    public static final char HEADER_INDICATOR = '#';

    /** Indicate the end of a section and the beginning of a new one */
    public static final char SECTION_SEPARATOR = '-';

    /** A list of {@link CSVLine} */
    private ArrayList<CSVLine> lines = new ArrayList<>();

    /** The position of the interator */
    private int iteratorPosition = 0;

    /**
     * Get the {@link CSVLine} list
     *
     * @return the {@link CSVLine} list
     */
    public ArrayList<CSVLine> getLines ()
    {
        return lines;
    }

    /**
     * Add a {@link CSVLine} to the list
     *
     * @param line the {@link CSVLine} object to add
     * @return this
     */
    public CSVParser addLine (@NotNull CSVLine line)
    {
        lines.add(line);
        return this;
    }

    /**
     * Add a {@link String} line to the list <br>
     * The String is parsed to a {@link CSVLine} object
     *
     * @param line the line to add
     * @return this
     */
    public CSVParser addLine (@NotNull String line)
    {
        addLine(CSVLine.fromString(line));
        return this;
    }

    /**
     * Save the content of the object to a file
     *
     * @param file the destination file
     * @throws IOException
     */
    public void saveToFile (@NotNull File file) throws IOException
    {
        FileWriter writter = new FileWriter(file);

        writter.write(lines.get(0).toString());
        for (int i = 1; i < lines.size(); i++)
        {
            writter.write(System.lineSeparator() + lines.get(i).toString());
        }

        writter.close();
        System.out.println("CSV data saved in " + file.getAbsolutePath());
    }

    /**
     * Save the content of the object to a file
     *
     * @param filePath the destination filepath
     * @throws IOException
     */
    public void saveToFile (@NotNull String filePath) throws IOException
    {
        if (!lines.isEmpty())
        {
            File f = new File(filePath);
            saveToFile(f);
        }
    }

    /**
     * Load csv data from a file
     *
     * @param filePath the path of the source file
     * @return the generated {@link CSVParser}
     * @throws FileNotFoundException
     */
    public static CSVParser loadFromFile (@NotNull String filePath) throws FileNotFoundException
    {
        File f = new File(filePath);
        return loadFromFile(f);
    }


    /**
     * Load csv data from a file
     *
     * @param file the source file
     * @return the generated {@link CSVParser}
     * @throws FileNotFoundException
     */
    public static CSVParser loadFromFile (@NotNull File file) throws FileNotFoundException
    {
        CSVParser obj = new CSVParser();

        Scanner sc = new Scanner(file);

        while (sc.hasNext())
        {
            String  lineStr = sc.nextLine();
            CSVLine csvLine = CSVLine.fromString(lineStr);
            obj.lines.add(csvLine);
        }

        return obj;
    }

    /**
     * To know if the parser has a next line
     * @return true if there is at least another line, false otherwise
     */
    @Override
    public boolean hasNext ()
    {
        return iteratorPosition < lines.size();
    }

    /**
     * Get the next line of the parser
     * @return the next line of the parser
     */
    @Override
    public CSVLine next ()
    {
        return hasNext() ? lines.get(iteratorPosition++) : null;
    }

    /**
     * Generate a csv String from all {@link CSVLine}s
     * @return a csv String from all {@link CSVLine}s
     */
    @Override
    public String toString ()
    {
        String str = "";
        for (CSVLine line : lines)
        {
            str += line.toString() + System.lineSeparator();
        }

        return str;
    }
}
