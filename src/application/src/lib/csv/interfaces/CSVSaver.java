package lib.csv.interfaces;

import lib.csv.CSVParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Robin on 04/06/2017. <br>
 * This interface provides a way to store {@link CSVParser} content into a file
 */
public interface CSVSaver
{
    /**
     * Save the content of a {@link CSVParser} to a file
     *
     * @param file   the destination file
     * @param parser the parser to write into the file
     */
    default void saveCSVToFile (@NotNull File file, @NotNull CSVParser parser)
    {
        try (FileWriter writter = new FileWriter(file))
        {
            writter.write(parser.toString());
            writter.flush();
            System.out.println("The data was saved into file " + file.getAbsolutePath());
        }
        catch (IOException e)
        {
            System.err.println("CSV saving failed...");
        }
    }
}
