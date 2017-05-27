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
public class ManagerTest {

    private Manager m1, m2;

    @Before
    public void setUp() throws Exception
    {
        m1 = new Manager("Man", "Ager");
        m2 = new Manager("test", "test");
    }

    @Test
    public void isManagerOf() throws Exception {
        assertTrue(m1.isManagerOf(null));
        StandardDepartment dep = new StandardDepartment("", "");
        m1.setManagedDepartment(dep);
        assertFalse(m1.isManagerOf(null));
        assertTrue(m1.isManagerOf(dep));
    }

    @Test
    public void setManagedDepartment() throws Exception {
        try{
            m1.setManagedDepartment(null);
        }catch(NullPointerException e)
        {
            fail("The function shouldn't do anything in case of null parameter.");
        }
    }

    @Test
    public void fire() throws Exception {
        int nbManagers = Company.getCompany().getManagementDepartment().getNbManagers();
        int nbEmployees = Company.getCompany().getNbEmployees();
        Company.getCompany().fire(m1);
        assertEquals(Company.getCompany().getManagementDepartment().getNbManagers(), nbManagers - 1);
        assertEquals(Company.getCompany().getNbEmployees(), nbEmployees - 1);
    }

    @Test
    public void saving ()
    {
//        m1.save();

        String filepath = "data\\files\\employees\\" + m1.getId() + ".json";

        File f = new File(filepath);

        try
        {
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            assertEquals(m1.getId(), Integer.parseInt((obj.get("id").toString())));
            assertTrue(obj.get("firstName").equals(m1.getFirstName()));
            assertEquals(Boolean.TRUE, obj.get("manager"));
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