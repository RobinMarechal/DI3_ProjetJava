package lib.csv.interfaces;

import lib.csv.CSVParser;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Robin on 04/06/2017. <br>
 * This interface tells that a class can store data to CSV format
 */
public interface CSVBuilder
{
    /**
     * Add {@link lib.csv.CSVLine} objects to a {@link CSVParser}.
     *
     * @param parser the parser to fill
     */
    void buildCSV (@NotNull CSVParser parser);
}
