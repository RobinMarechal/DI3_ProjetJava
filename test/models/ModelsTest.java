package models;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Robin on 09/04/2017.
 */
public class ModelsTest
{

    private static int baseNbEmployees, baseNbDepartments, baseNbManagers;
    private static Employee e1, e2, e3, e4, e5;
    private static StandardDepartment dep1, dep2, dep3, dep4;
    private static Manager m1, m2, m3, m4;


    @BeforeClass
    public static void setUp () throws Exception
    {
        baseNbEmployees = Company.getCompany().getNbEmployees();
        baseNbDepartments = Company.getCompany().getNbStandardDepartments();
        baseNbManagers = ManagementDepartment.getManagementDepartment().getNbManagers();

        e1 = Company.createEmployee("e1", "a");
        e2 = Company.createEmployee("e2", "b");
        e3 = new Employee("e3", "c");
        e4 = new Employee("e4", "d");
        e5 = new Employee("e5", "e");

        m1 = Company.createManager("m1", "a");
        m2 = Company.createManager("m2", "b");
        m3 = new Manager("m3", "c");
        m4 = new Manager("m4", "d");

        dep1 = Company.createStandardDepartment("dep1", "a");
        dep2 = Company.createStandardDepartment("dep2", "b", m2);
        dep3 = new StandardDepartment("dep3", "c", m3);
        dep4 = new StandardDepartment("dep4", "d");

        dep1.setManager(m1);
        dep1.addEmployee(e1);
        dep1.addEmployee(e2);
        dep1.addEmployee(e3);

        dep2.addEmployee(e4);

        dep3.addEmployee(e5);

        dep4.setManager(m4);
        /*
            dep1 => manager = m1 ; employees = [m1, e1, e2, e3],
            dep2 => manager = m2 ; employees = [m2, e4],
            dep3 => manager = m3 ; employees = [m3, e5],
            dep4 => manager = m4 ; employees = [m4]
         */
    }

    @Test
    public void run() throws Exception
    {
        basics();
        changingDepartment();
    }

    private void basics () throws Exception
    {
        assertEquals(baseNbEmployees + 5 + 4, Company.getCompany().getNbEmployees());
        assertEquals(baseNbDepartments + 4, Company.getCompany().getNbStandardDepartments());
        assertEquals(baseNbManagers + 4, ManagementDepartment.getManagementDepartment().getNbManagers());

        assertEquals(3 + 1, dep1.getNbEmployees());
        assertEquals(1 + 1, dep2.getNbEmployees());
        assertEquals(1 + 1, dep3.getNbEmployees());
        assertEquals(0 + 1, dep4.getNbEmployees());

        assertEquals(m1, dep1.getManager());
        assertEquals(m2, dep2.getManager());
        assertEquals(m3, dep3.getManager());
        assertEquals(m4, dep4.getManager());

        assertEquals(dep1, m1.getDepartment());
    }

    private void changingDepartment () throws Exception
    {
        dep1.addEmployee(e5);

        assertEquals(dep1, e5.getDepartment());
        assertEquals(5, dep1.getNbEmployees());
        assertEquals(1, dep3.getNbEmployees()); // 1 because of the manager

        try
        {
            dep1.addEmployee(m3);
            fail("Manager shouldn't be added successfuly as an employee: he's the manager of another department.");
        }
        catch (Exception e)
        {
        }

        assertEquals(m1, dep1.getManager());
        assertNotNull(dep3.getManager());
        assertEquals(dep3, m3.getDepartment());
        assertNotNull(m3.getManagedDepartment());

        dep2.setManager(m1);

        assertNull(dep1.getManager());
        assertEquals(4, dep1.getNbEmployees());
        assertEquals(3, dep2.getNbEmployees());
        assertEquals(dep2, m1.getDepartment());
        assertEquals(dep2, m1.getManagedDepartment());

        dep1.setManager(m3);

        assertEquals(m3, dep1.getManager());
        assertEquals(5, dep1.getNbEmployees());

        /*
            dep1 => manager = m3 ; employees = [e1, e2, e3, e5, m3],
            dep2 => manager = m1 ; employees = [m2, m1, e4],
            dep3 => manager = null ; employees = [m3],
            dep4 => manager = m4 ; employees = [m4]
         */
    }


    @Test
    public void saves ()
    {
        String paths[] = {
                "data\\files\\employees\\",
                "data\\files\\departments\\",
                "data\\files\\"
        };

        clearDirectories();

        Company.getCompany().saveAll();

        File dirEmployees = new File(paths[0]);
        File dirDepartments = new File(paths[1]);
        File dirFiles = new File(paths[2]);

        assertEquals(5 + 4 + baseNbEmployees, dirEmployees.list().length); // 5 employees + 4 managers
        assertEquals(4 + baseNbDepartments, dirDepartments.list().length);
        assertEquals(3 + 2, dirFiles.list().length); // 3 files, 2 folders
    }

    private static void clearDirectories()
    {
        String paths[] = {
                "data\\files\\employees\\",
                "data\\files\\departments\\",
                "data\\files\\"
        };

        for (String path : paths)
        {
            File dir = new File(path);
            File files[] = dir.listFiles();
            for (File f : files)
            {
                if (!f.isDirectory())
                {
                    f.delete();
                }
            }
        }
    }

    private static void removing() throws Exception
    {
        int nbE = Company.getCompany().getNbEmployees();
        int nbM = ManagementDepartment.getManagementDepartment().getNbManagers();
        int nbD = Company.getCompany().getNbStandardDepartments();

        e1.fire();
        m3.fire();

        assertEquals(3, dep1.getNbEmployees());
        assertEquals(nbE - 2, Company.getCompany().getNbEmployees());
        assertNull(dep1.getManager());

        dep1.remove();

        assertNull(e2.getDepartment());

        dep2.remove();
        dep3.remove();
        dep4.remove();

        e2.fire();
        e3.fire();
        e4.fire();
        e5.fire();

        m1.fire();
        m2.fire();
        m4.fire();

        assertEquals(baseNbManagers, ManagementDepartment.getManagementDepartment().getNbManagers());
        assertEquals(baseNbEmployees, Company.getCompany().getNbEmployees());
        assertEquals(baseNbDepartments, Company.getCompany().getNbStandardDepartments());
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        clearDirectories();
        removing();
    }
}