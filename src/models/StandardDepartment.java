package models;

import lib.json.JsonSaver;
import lib.json.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class StandardDepartment extends VirtualDepartment implements Jsonable, JsonSaver
{
    /**
     * The ID of the next instance
     */
    private static int NEXT_ID = 1;

    /**
     * The ID of the instance
     */
    private int id;

    /**
     * A list of all employees working in this department
     */
    private ArrayList<Employee> employees = new ArrayList<>();

    /**
     * The manager of the department
     */
    private Manager manager;

    /**
     * 3 parameters constructor (with manager)
     * @param name The department's name
     * @param activitySector the department's activitySector
     * @param manager the department's manager
     */
    public StandardDepartment (String name, String activitySector, Manager manager)
    {
        this(name, activitySector);
        setManager(manager);
    }

     /**
     * 3 parameters constructor (with id)
     * @param name The department's name
     * @param activitySector the department's activitySector
     * @param id the id to give to this department
     * @throws Exception if there already is a department with this ID
     */
    public StandardDepartment (String name, String activitySector, int id) throws Exception
    {
        super(name, activitySector);

        if (Company.getCompany().getStandardDepartment(id) != null)
        {
            throw new Exception("There already is an employee with id " + id + ".");
        }

        if (id > StandardDepartment.NEXT_ID)
        {
            StandardDepartment.NEXT_ID = id + 1;
        }

        this.id = id;
        Company.getCompany().addStandardDepartment(this);
    }

    /**
     * 2 parameters constructor
     * @param name The department's name
     * @param activitySector the department's activitySector
     */
    public StandardDepartment (String name, String activitySector)
    {
        super(name, activitySector);
        id = StandardDepartment.NEXT_ID;
        StandardDepartment.NEXT_ID++;
        Company.getCompany().addStandardDepartment(this);
    }

    /**
     * Retrieve the department's ID
     * @return the department's ID
     */
    public int getId ()
    {
        return id;
    }

    /**
     * The ID of the next created instance of {@link StandardDepartment}
     * @return the ID of the next instance
     */
    public static int getNextId ()
    {
        return StandardDepartment.NEXT_ID;
    }

    /**
     * Retrieve the number of employees working in this department
     * @return the number of employees working in this department
     */
    public int getNbEmployees ()
    {
        return employees.size();
    }

    /**
     * Adds a manager to this department as an employee
     * @param manager the employee to add
     * @return this
     */
    public StandardDepartment addEmployee(Manager manager)
    {
        if(manager != null && !employees.contains(manager))
        {
            manager.setManagedDepartment(null);
        }

        return this.addEmployee((Employee) manager);
    }

    /**
     * Adds an employee to this department
     * @param employee the employee to add
     * @return this
     */
    public StandardDepartment addEmployee (Employee employee)
    {
        if (employee != null && !employees.contains(employee))
        {
            this.employees.add(employee);
            employee.setDepartment(this);
        }

        return this;
    }

    /**
     * Removes an employee from this department
     * @param employee the employee to remove
     * @return this
     */
    public StandardDepartment removeEmployee (Employee employee)
    {
        if (employee != null && employees.contains(employee))
        {
            this.employees.remove(employee);
            employee.setDepartment(null);
        }

        return this;
    }

    /**
     * Sets the manager of this department
     * @param manager the new manager
     * @return this
     */
    public StandardDepartment setManager (Manager manager)
    {
        if(this.manager != null)
        {
            this.manager.setManagedDepartment(null);
            this.manager = null;
        }

        if(manager != null)
        {
            manager.setManagedDepartment(this);
            this.manager = manager;

            if (!employees.contains(manager))
            {
                employees.add(manager);
                manager.setDepartment(this);
            }
        }

        return this;
    }

    /**
     * Retrieve the manager of the department
     * @return the manager of the department
     */
    public Manager getManager ()
    {
        return manager;
    }

    /**
     * Removes the department from the company
     */
    public void remove ()
    {
        for(Employee e : employees)
        {
            e.setDepartmentToNull();
        }

        Company.getCompany().removeStandardDepartment(this);
    }

    /**
     * Creates a String representing the department and all his employees
     * @return
     */
    public String toStringWithEmployees ()
    {
        String str;
        int size = employees.size();
        if (employees.size() == 0)
        {
            str = "There is no employee in the department " + getName() + ".  "; // 2 spaces because there is a substring(0, length-2) later.
        }
        else if (employees.size() == 1)
        {
            str = "There 1 employee in the department " + getName() + " :" + System.lineSeparator();
        }
        else
        {
            str = "There are " + size + " employees in the department " + getName() + " :" + System.lineSeparator();
        }

        for (Employee e : employees)
        {
            str += "\t - " + e + "," + System.lineSeparator();
        }

        str = str.substring(0, str.length() - 2);

        return str;
    }


    /**
     * Creates a String representing the department
     * @return
     */
    @Override
    public String toString ()
    {
        return "StandardDepartment : " + getName() + ", activity sector : " + getActivitySector() + ", managed by : " + manager;
    }

    /**
     * Save the data of a class instance into a json file
     */
    @Override
    public void save ()
    {
        String path = "data\\files\\departments";
        String filename = id + ".json";
        saveToFile(path, filename, toJson());
    }

    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = super.toJson();
        JSONArray employeesArray = new JSONArray();

        json.put("id", id);
        json.put("manager", manager == null ? null : manager.getId());

        for (Employee e : employees)
        {
            employeesArray.add(e.getId());
        }

        json.put("employees", employeesArray);

        return json;
    }
}
