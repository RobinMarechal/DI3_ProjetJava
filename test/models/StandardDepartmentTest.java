package models;

import org.json.simple.JSONArray;
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
 * Created by Robin on 08/04/2017.
 */
public class StandardDepartmentTest
{

    private StandardDepartment dep1, dep2;
    private int nextId;

    @Before
    public void setUp () throws Exception
    {
        nextId = StandardDepartment.getNextId();
        dep1 = new StandardDepartment("", "");
        dep2 = new StandardDepartment("", "");
    }

    @Test
    public void getId () throws Exception
    {
        assertEquals(0 + nextId, dep1.getId());
        assertEquals(1 + nextId, dep2.getId());

        StandardDepartment dep3 = null, dep4 = null;

        int id = 80;

        try
        {
            dep3 = new StandardDepartment("", "", id);
            assertEquals(id, dep3.getId());
        }
        catch (Exception e)
        {
            fail("StandardDepartment with id " + id + " should have been created successfully.");
        }

        try
        {
            dep4 = new StandardDepartment("", "", id);
            fail("StandardDepartment with id " + id + " shouldn't have been created : a StandardDepartment with id 5 already exists.");
        }
        catch (Exception e)
        {

        }
    }

    @Test
    public void addEmployee () throws Exception
    {
        dep1.addEmployee(null);
    }

    @Test
    public void removeEmployee () throws Exception
    {
        dep1.removeEmployee(null);
    }

    @Test
    public void setManager () throws Exception
    {
        dep1.setManager(null);
    }

    @Test
    public void getManager () throws Exception
    {
        assertNull(dep1.getManager());
    }

    @Test
    public void remove () throws Exception
    {
        int nbDeps = Company.getCompany().getNbStandardDepartments();

        dep1.remove();
        Company.getCompany().removeStandardDepartment(dep2);

        assertEquals(Company.getCompany().getNbStandardDepartments(), nbDeps - 2);
    }

    @Test
    public void saving ()
    {
//        dep1.save();

        String filepath = "data\\files\\departments\\" + dep1.getId() + ".json";

        File f = new File(filepath);

        try
        {
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            assertEquals(dep1.getId(), Integer.parseInt((obj.get("id").toString())));
            assertTrue(obj.get("name").equals(dep1.getName()));
            assertTrue(obj.get("activitySector").equals(dep1.getActivitySector()));
            assertNull(obj.get("manager"));
            assertEquals(dep1.getNbEmployees(), ((JSONArray) obj.get("employees")).size());
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