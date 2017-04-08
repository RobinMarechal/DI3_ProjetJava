package models;

import org.junit.Before;
import org.junit.Test;

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

}