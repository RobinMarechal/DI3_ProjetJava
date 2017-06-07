package models;

import javafx.collections.ObservableList;
import models.Company;
import models.Employee;
import models.Manager;
import models.StandardDepartment;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.*;

/**
 * Created by Robin on 08/04/2017.
 */
public class CompanyTest
{
    private Employee e;
    private Manager m;

    @Test
    public void getCompany () throws Exception
    {
        assertNotNull(Company.getCompany());
    }

    @Test
    public void addEmployee () throws Exception
    {
        Company.getCompany().addEmployee(null);
        e = Company.createEmployee("", "");
        assertNotNull(e);

    }

    @Test
    public void fire () throws Exception
    {
        Company.getCompany().fire(null);
        Company.getCompany().fire(e);
        Company.getCompany().fire(m);

        assertNull(e);
        assertNull(m);
    }

    @Test
    public void removeEmployee () throws Exception
    {
        Company.getCompany().removeEmployee(null);
    }

    @Test
    public void addManager () throws Exception
    {
        m = Company.createManager("", "");
        assertNotNull(m);
        assertEquals(Company.getCompany().getManagementDepartment().getNbManagers(), 1);
    }

    @Test
    public void getEmployee () throws Exception
    {
        Employee emp = new Employee("", "");

        assertEquals(emp, Company.getCompany().getEmployee(emp.getId()));

        emp.fire();
    }

    @Test
    public void getNbEmployees () throws Exception
    {
        int n = Company.getCompany().getNbEmployees();

        Employee emp  = new Employee("", "");
        Employee emp2 = new Employee("", "");

        assertEquals(n + 2, Company.getCompany().getNbEmployees());

        emp.fire();
        emp2.fire();
    }

    @Test
    public void addStandardDepartment () throws Exception
    {
        int                n   = Company.getCompany().getNbStandardDepartments();
        StandardDepartment dep = Company.createStandardDepartment("", "");

        assertNotNull(dep);
        assertEquals(1 + n, Company.getCompany().getNbStandardDepartments());

        StandardDepartment dep2 = new StandardDepartment("", "");

        assertNotNull(dep);
        assertEquals(2 + n, Company.getCompany().getNbStandardDepartments());
    }

    @Test
    public void removeStandardDepartment () throws Exception
    {
        int n = Company.getCompany().getNbStandardDepartments();

        StandardDepartment dep = Company.createStandardDepartment("test", "tests");

        assertEquals(n + 1, Company.getCompany().getNbStandardDepartments());

        Company.getCompany().removeStandardDepartment(dep);

        assertEquals(n, Company.getCompany().getNbStandardDepartments());
    }

    @Test
    public void searchEmployee () throws Exception
    {


        Employee e1 = Company.createEmployee("ABCDEF", "ijklmn");
        Employee e2 = Company.createEmployee("azertyuiop", "qsdfghjklm");
        Employee e3 = Company.createEmployee("abcdef", "Wxcvbn");

        List<Employee> employees1 = Company.getCompany().searchEmployee("abcdef", "");
        List<Employee> employees2 = Company.getCompany().searchEmployee("abcdef", "ijklmn");

        assertEquals(Company.getCompany().getNbEmployees(), Company.getCompany().searchEmployee("", "").size());
        assertEquals(0, Company.getCompany().searchEmployee("aaaaaaaaaaaaa", "").size());

        assertEquals(2, employees1.size());
        assertTrue(employees1.contains(e1));
        assertTrue(employees1.contains(e3));
        assertFalse(employees1.contains(e2));

        assertEquals(1, employees2.size());
        assertTrue(employees2.contains(e1));
    }

    @Test
    public void searchStandardDepartment () throws Exception
    {
        StandardDepartment dep1 = Company.createStandardDepartment("aaaaaaaaaaaaaaa", "Activity");
        StandardDepartment dep2 = Company.createStandardDepartment("aaaaaaaaaaaaaaa2", "Sector");
        StandardDepartment dep3 = Company.createStandardDepartment("test", "aaaaaaaaaaaaaaaa");

        ObservableList<StandardDepartment> deps1 = Company.getCompany().searchStandardDepartment("aaaaaaaaaaaaaaa", "");
        ObservableList<StandardDepartment> deps2 = Company.getCompany().searchStandardDepartment("", "aaaaaaaaaaaaaaa");

        assertEquals(Company.getCompany().getNbStandardDepartments(), Company.getCompany()
                                                                             .searchStandardDepartment("", "")
                                                                             .size());
        assertEquals(0, Company.getCompany().searchStandardDepartment("azertyuio", "").size());
        assertEquals(2, deps1.size());
        assertEquals(1, deps2.size());

        assertTrue(deps1.contains(dep1));
        assertTrue(deps1.contains(dep2));

        assertTrue(deps2.contains(dep3));
    }

    @Test
    public void saving ()
    {
//        Company.getCompany().save();
        String filepath = "data\\files\\company.json";

        File f = new File(filepath);

        try
        {
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject obj    = (JSONObject) parser.parse(reader);

            assertTrue(obj.get("name").equals(Company.getCompany().getName()));
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