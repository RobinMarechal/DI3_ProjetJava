package application.models;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Robin on 07/04/2017.
 */
public class EmployeeTest
{

    private Employee e1, e2, e3, e4, e5;
    private int nextId;
    private int nbEmployees;

    @Before
    public void setUp () throws Exception
    {
        nextId = Employee.getNextId();
        nbEmployees = Company.getCompany().getNbEmployees();
        e1 = new Employee("abc", "def");
        e2 = new Employee("emp", "loyer");
        e3 = new Employee("test", "test");
    }

    @Test
    public void basics () throws Exception
    {
        assertEquals(nextId + 2, e3.getId());
        assertEquals(nextId + 3, Employee.getNextId());

        assertEquals(nbEmployees + 3, Company.getCompany().getNbEmployees());
        assertEquals(e1, Company.getCompany().getEmployee(e1.getId()));

        Company.getCompany().fire(e2);

        assertEquals(nbEmployees + 2, Company.getCompany().getNbEmployees());
        assertNull(Company.getCompany().getEmployee(2));

        try
        {
            e4 = new Employee("test2", "test2", 2);
        }
        catch (Exception e)
        {
            fail("Exception shouldn't have been thrown : Employee with id 2 should have been created succesfuly.");
        }

        try
        {
            e5 = new Employee("aze", "aze", 2);
            fail("Exception should have been thrown : Employee with id 2 already exists.");
        }
        catch (Exception e)
        {
        }
    }

    @Test
    public void saving ()
    {
//        e1.save();

        String filepath = "data\\files\\employees\\" + e1.getId() + ".json";

        File f = new File(filepath);

        try
        {
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            assertEquals(e1.getId(), Integer.parseInt((obj.get("id").toString())));
            assertTrue(obj.get("firstName").equals(e1.getFirstName()));
            assertEquals(Boolean.FALSE, obj.get("manager"));
            assertNull(obj.get("sartingHour"));
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