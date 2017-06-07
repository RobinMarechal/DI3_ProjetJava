package models;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lib.json.Jsonable;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.stream.Collectors;

/**
 * Created by Robin on 27/03/2017.<br>
 * This class is a singleton representin the company's management department. <br>
 * It contains only managers and is managed by the boss of the company
 */
public class ManagementDepartment extends VirtualDepartment<Manager> implements Jsonable
{
    /** Instance of the ManagementDepartment singleton class */
    private static ManagementDepartment managementDepartmentInstance = new ManagementDepartment();


    /**
     * Retrieve the instance of the ManagementDepartment singleton class
     *
     * @return the instance of the ManagementDepartment singleton class
     */
    public static ManagementDepartment getManagementDepartment ()
    {
        return managementDepartmentInstance;
    }

    /**
     * Default constructor
     */
    private ManagementDepartment ()
    {
        super("Management Department", "Management");
    }

    /**
     * Retrieve the number of managers in the Company
     *
     * @return the number of managers in the company
     */
    public int getNbManagers ()
    {
        return employees.size();
    }

    /**
     * Retrieve the manager with a specific ID
     *
     * @param id the id of the manager
     * @return The {@link Manager} instance with this id OR null if not found
     */
    public Manager getManager (int id)
    {
        for (Manager m : employees)
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
        return this.employees;
    }

    /**
     * Get a list containing all the managers that don't manage any department.
     *
     * @return a list containing all the managers that don't manage any department.
     */
    public ObservableList<Manager> getManagersThatDontManage ()
    {
        final ObservableList<Manager> list = employees.stream()
                                                      .filter(manager -> manager.getManagedDepartment() == null)
                                                      .collect(Collectors.toCollection(FXCollections::observableArrayList));

        getEmployees().addListener(new ListChangeListener<Manager>()
        {
            @Override
            public void onChanged (Change<? extends Manager> c)
            {
                while (c.next())
                {
                    list.clear();
                    list.addAll(employees.stream()
                                         .filter(manager -> manager.getManagedDepartment() == null)
                                         .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                }
            }
        });

        return list;
    }

    /**
     * Adds a manager to the Company.
     *
     * @param manager the {@link Manager} instance to add
     * @return this
     */
    public ManagementDepartment addManager (Manager manager)
    {
        if (manager != null && !employees.contains(manager))
        {
            //            this.employees.add(manager);
            int     id    = manager.getId();
            boolean added = false;
            for (int i = 0; i < employees.size() && !added; i++)
            {
                if (id < employees.get(i).getId())
                {
                    employees.add(i, manager);
                    added = true;
                }
            }

            if (!added)
            {
                employees.add(manager);
            }
        }

        return this;
    }

    /**
     * Create a {@link Manager} instance from an {@link Employee} one. <br>
     * The newly created manager contains the exact same data as the employee one, including the ID.<br>
     * The Employee instance is deleted.
     * @param employee the employee promote
     * @return the newly created manager
     */
    public Manager addEmployeeAsManager (@NotNull Employee employee)
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
            employees.remove(manager);

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
        for (Manager m : employees)
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

    /**
     * Load the management department data from a JSON object.
     * @param json the JSON object containg the management department information
     */
    public static void loadFromJson (@NotNull JSONObject json)
    {
        ManagementDepartment obj = getManagementDepartment();
        obj.setName(json.get(JSON_KEY_NAME).toString());
        obj.setActivitySector(json.get(JSON_KEY_ACTIVITY_SECTOR).toString());
    }
}
