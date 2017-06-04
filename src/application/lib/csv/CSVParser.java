package application.lib.csv;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Robin on 04/06/2017.
 */
public class CSVParser implements Iterator<CSVLine>
{
    public static final char HEADER_INDICATOR = '#';
    public static final char SECTION_SEPARATOR = '-';

    private ArrayList<CSVLine> lines = new ArrayList<>();

    private int iteratorPosition = 0;

    public ArrayList<CSVLine> getLines ()
    {
        return lines;
    }

    public CSVParser addLine (CSVLine line)
    {
        lines.add(line);
        return this;
    }

    public CSVParser addLine (String line)
    {
        addLine(CSVLine.fromString(line));
        return this;
    }

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

    public void saveToFile (@NotNull String filePath)
    {
        if (!lines.isEmpty())
        {
            try
            {
                File f = new File(filePath);
                saveToFile(f);
            }
            catch (IOException e)
            {
                System.out.println("csv save failed");
                e.printStackTrace();
            }
        }
    }

    public static CSVParser loadFromFile (@NotNull String filePath)
    {
        try
        {
            File f = new File(filePath);
            return loadFromFile(f);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

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

    @Override
    public boolean hasNext ()
    {
        return iteratorPosition < lines.size();
    }

    @Override
    public CSVLine next ()
    {
        return hasNext() ? lines.get(iteratorPosition++) : null;
    }

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
