package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Robin on 08/04/2017.
 */
public class StandardDepartmentTest {

    private StandardDepartment dep1, dep2;
    private int nextId;

    @Before
    public void setUp() throws Exception {
        nextId = StandardDepartment.getNextId();
        dep1 = new StandardDepartment("", "");
        dep2 = new StandardDepartment("", "");
    }

    @Test
    public void getId() throws Exception
    {
        assertEquals(0 + nextId, dep1.getId());
        assertEquals(1 + nextId, dep2.getId());

        StandardDepartment dep3 = null, dep4 = null;

        try{
            dep3 = new StandardDepartment("", "", 20);
            assertEquals(20, dep3.getId());
        }catch(Exception e)
        {
            fail("StandardDepartment with id 5 should have been created successfully.");
        }

        try{
            dep4 = new StandardDepartment("", "", 20);
            fail("StandardDepartment with id 5 shouldn't have been created : a StandardDepartment with id 5 already exists.");
        }catch(Exception e)
        {

        }
    }

    @Test
    public void addEmployee() throws Exception {
        dep1.addEmployee(null);
    }

    @Test
    public void removeEmployee() throws Exception {
        dep1.removeEmployee(null);
    }

    @Test
    public void setManager() throws Exception {
        dep1.setManager(null);
    }

    @Test
    public void getManager() throws Exception {
        assertNull(dep1.getManager());
    }

    @Test
    public void remove() throws Exception {
        int nbDeps = Company.getCompany().getNbStandardDepartments();

        dep1.remove();
        Company.getCompany().removeStandardDepartment(dep2);

        assertEquals(Company.getCompany().getNbStandardDepartments(), nbDeps - 2);
    }

}