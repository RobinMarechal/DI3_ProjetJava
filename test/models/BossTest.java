package models;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Robin on 08/04/2017.
 */
public class BossTest {

    @Test
    public void basics() throws Exception
    {
        assertNotNull(Boss.getBoss());
        assertEquals(Boss.getBoss(), Company.getCompany().getBoss());
    }

    @Test
    public void saving ()
    {
        Boss.getBoss().save();

        String filepath = "data\\files\\boss.json";

        File f = new File(filepath);

        try
        {
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            assertTrue(obj.get("firstName").equals(Boss.getBoss().getFirstName()));
            assertTrue(obj.get("lastName").equals(Boss.getBoss().getLastName()));
        }
        catch (FileNotFoundException e)
        {
            fail("The file '" + filepath + "' sould exist...");
        }
        catch (ParseException | IOException e)
        {
            fail("Error when parsing the file...");
        }
    }

}