package models;

import lib.json.JsonSaver;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class ManagementDepartment extends VirtualDepartment implements JsonSaver, Jsonable
{
    /**
     * Instance of the ManagementDepartment singleton class
     */
    private static ManagementDepartment managementDepartmentInstance = new ManagementDepartment();


    /**
     * A list of all Company's managers
     */
    private ArrayList<Manager> managers = new ArrayList<>();


    /**
     * Retrieve the instance of the ManagementDepartment singleton class
     * @return
     */
    public static ManagementDepartment getManagementDepartment() {
        return managementDepartmentInstance;
    }

    /**
     * Basic constructor
     */
    private ManagementDepartment()
    {
    	super("Management Department", "Management");
    }

    /**
     * Retrieve the number of managers in the Company
     * @return
     */
    public int getNbManagers()
    {
        return managers.size();
    }

    /**
     * Retrieve the manager with a specific ID
     * @param id the id of the manager
     * @return The {@link Manager} instance with this id OR null if not found
     */
    public Manager getManager(int id)
    {
        for(Manager m : managers)
        {
            if(m.getId() == id)
            {
                return m;
            }
        }

        return null;
    }

    /**
     * Adds a manager to the Company.
     * @param manager the {@link Manager} instance to add
     * @return this
     */
    public ManagementDepartment addManager(Manager manager)
    {
        if(manager != null && !managers.contains(manager))
        {
            this.managers.add(manager);
        }

        return this;
    }

    /**
     * Removes a manager from the Company
     * @param manager the {@link Manager} instance to remove
     * @return this
     */
    public ManagementDepartment removeManager(Manager manager)
    {
        if(manager != null && managers.contains(manager))
        {
            managers.remove(manager);
            manager.setManagedDepartment(null);
        }

        return this;
    }

    /**
     * Creates a string with all managers
     * @return The string representing the management department with all managers
     */
    public String toStringWithManagers()
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
     * @return The string representing the management department
     */
    @Override
    public String toString() {
        return getName() + " : activity sector : " + getActivitySector() + ", managed by the " + Boss.getBoss();
    }

    /**
     * Save the data of a class instance into a json file
     */
    @Override
    public void save ()
    {
        String path = "data\\files";
        String filename = "management_department.json";
        saveToFile(path, filename, toJson());
    }
}
