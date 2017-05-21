package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017.
 */
public class ManagementDepartment extends VirtualDepartment implements Jsonable
{
    protected final static String JSON_KEY_MANAGERS = "managers";

    /**
     * Instance of the ManagementDepartment singleton class
     */
    private static ManagementDepartment managementDepartmentInstance = new ManagementDepartment();


    /** A list of all Company's managers */
    private ObservableList<Manager> managers = FXCollections.observableArrayList();


    /**
     * Retrieve the instance of the ManagementDepartment singleton class
     *
     * @return
     */
    public static ManagementDepartment getManagementDepartment ()
    {
        return managementDepartmentInstance;
    }

    /**
     * Basic constructor
     */
    private ManagementDepartment ()
    {
        super("Management Department", "Management");
    }

    /**
     * Retrieve the number of managers in the Company
     *
     * @return
     */
    public int getNbManagers ()
    {
        return managers.size();
    }

    /**
     * Retrieve the manager with a specific ID
     *
     * @param id the id of the manager
     * @return The {@link Manager} instance with this id OR null if not found
     */
    public Manager getManager (int id)
    {
        for (Manager m : managers)
        {
            if (m.getId() == id)
            {
                return m;
            }
        }

        return null;
    }

    /**
     * Get a list containing all the managers
     *
     * @return a list containing all managers
     */
    public ObservableList<Manager> getManagersList ()
    {
        return this.managers;
    }

    /**
     * Adds a manager to the Company.
     *
     * @param manager the {@link Manager} instance to add
     * @return this
     */
    public ManagementDepartment addManager (Manager manager)
    {
        if (manager != null && !managers.contains(manager))
        {
            this.managers.add(manager);
        }

        return this;
    }

    public Manager addEmployeeAsManager (Employee employee)
    {
        if (employee instanceof Manager)
        {
            return (Manager) employee;
        }

        final String firstName = employee.getFirstName();
        final String lastName  = employee.getLastName();
        final int    id        = employee.getId();

        final StandardDepartment         department  = employee.getDepartment();
        final ObservableList<CheckInOut> checksInOut = employee.getChecksInOut();

        Manager manager = null;

        try
        {
            employee.fire();
            try
            {
                manager = new Manager(firstName, lastName, id);
            }
            catch (Exception e)
            {
                manager = new Manager(firstName, lastName);
            }

            manager.setChecksInOut(checksInOut);

            try
            {
                department.addEmployee(manager);
            }
            catch (Exception e)
            {
                // Should only happen if department is null, in that case we do nothing
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return manager;
    }

    /**
     * Removes a manager from the Company
     *
     * @param manager the {@link Manager} instance to remove
     * @return this
     */
    public ManagementDepartment removeManager (Manager manager)
    {
        if (manager != null)
        {
            managers.remove(manager);

            // The department he managed has no longer a manager...
            StandardDepartment managedDep = manager.getManagedDepartment();
            if (managedDep != null)
            {
                managedDep.setManager(null);
            }

            // The manager does not manage any department
            manager.setManagedDepartment(null);

            // Finally we remove the manager from the company
            Company.getCompany().fire((Employee) manager);
        }

        return this;
    }

    /**
     * Creates a string with all managers
     *
     * @return The string representing the management department with all managers
     */
    public String toStringWithManagers ()
    {
        String str = "List of managers : " + System.lineSeparator();
        for (Manager m : managers)
        {
            str += "\t - " + m + System.lineSeparator();
        }

        return str;
    }

    /**
     * Create a simple String representing the management department
     *
     * @return The string representing the management department
     */
    @Override
    public String toString ()
    {
        return getName() + " : activity sector : " + getActivitySector() + ", managed by the " + Boss.getBoss();
    }

    public void loadFromDeserialization (ManagementDepartment instance)
    {
        this.managementDepartmentInstance = instance;
    }

    public static void loadFromJson (JSONObject json)
    {
        ManagementDepartment obj = getManagementDepartment();
        obj.setName(json.get(JSON_KEY_NAME).toString());
        obj.setActivitySector(json.get(JSON_KEY_ACTIVITY_SECTOR).toString());
    }
}
