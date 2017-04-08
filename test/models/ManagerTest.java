package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        m1.becomesManagerOf(dep);
        assertFalse(m1.isManagerOf(null));
        assertTrue(m1.isManagerOf(dep));
    }

    @Test
    public void manages() throws Exception {
        try{
            m1.manages(null);
        }catch(NullPointerException e)
        {
            fail("The function shouldn't do anything in case of null parameter.");
        }
    }

    @Test
    public void becomesManagerOf() throws Exception {
        try{
            m2.becomesManagerOf(null);
        }catch(NullPointerException e)
        {
            fail("The function shouldn't do anything in case of null parameter.");
        }
    }

    @Test
    public void doesNotManageAnymore() throws Exception {
        try{
            m2.doesNotManageAnymore(null);
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
}