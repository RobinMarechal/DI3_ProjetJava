package application.lib.csv.interfaces;

import application.lib.csv.CSVParser;

/**
 * Created by Robin on 04/06/2017. <br/>
 * This interface tells that a class can store data to CSV format
 */
public interface CSVBuilder
{
    /**
     * Add {@link application.lib.csv.CSVLine} objects to a {@link CSVParser}.
     *
     * @param parser the parser to fill
     */
    void buildCSV (CSVParser parser);
}
