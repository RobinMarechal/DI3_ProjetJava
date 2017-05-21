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
public class ManagementDepartmentTest
{
    @Test
    public void getManagementDepartment() throws Exception
    {
        assertNotNull(ManagementDepartment.getManagementDepartment());
        assertEquals(ManagementDepartment.getManagementDepartment(), Company.getCompany().getManagementDepartment());
    }

    @Test
    public void getManager() throws Exception
    {
        assertNull(ManagementDepartment.getManagementDepartment().getManager(-1));
        assertNull(ManagementDepartment.getManagementDepartment().getManager(100));
    }

    @Test
    public void addManager() throws Exception
    {
        ManagementDepartment.getManagementDepartment().addManager(null);
    }

    @Test
    public void removeManager() throws Exception
    {
        ManagementDepartment.getManagementDepartment().removeManager(null);
    }

    @Test
    public void saving ()
    {
//        ManagementDepartment.getManagementDepartment().save();

        String filepath = "data\\files\\management_department.json";

        File f = new File(filepath);

        try
        {
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            assertTrue(obj.get("name").equals(ManagementDepartment.getManagementDepartment().getName()));
            assertTrue(obj.get("activitySector").equals(ManagementDepartment.getManagementDepartment().getActivitySector()));
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