package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lib.json.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.stream.Collectors;

/**
 * Created by Robin on 27/03/2017.
 */
public class StandardDepartment extends VirtualDepartment implements Jsonable
{
    protected static final String JSON_KEY_ID = "id";
    protected static final String JSON_KEY_MANAGER = "manager";

    /** The ID of the next instance */
    private static int NEXT_ID = 1;

    /**  The ID of the instance */
    private IntegerProperty id = new SimpleIntegerProperty(this, "id", -1);

    /** A list of all employees working in this department  */
    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    /** The manager of the department */
    private ObjectProperty<Manager> manager = new SimpleObjectProperty<>(this, "manager");

    /**
     * 3 parameters constructor (with manager)
     *
     * @param name           The department's name
     * @param activitySector the department's activitySector
     * @param manager        the department's manager
     */
    public StandardDepartment (String name, String activitySector, Manager manager)
    {
        this(name, activitySector);
        setManager(manager);
    }

    /**
     * 3 parameters constructor (with id)
     *
     * @param name           The department's name
     * @param activitySector the department's activitySector
     * @param id             the id to give to this department
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

        this.id.setValue(id);
        Company.getCompany().addStandardDepartment(this);
    }

    /**
     * 2 parameters constructor
     *
     * @param name           The department's name
     * @param activitySector the department's activitySector
     */
    public StandardDepartment (String name, String activitySector)
    {
        super(name, activitySector);
        id.setValue(StandardDepartment.NEXT_ID);
        StandardDepartment.NEXT_ID++;
        Company.getCompany().addStandardDepartment(this);
    }

    /**
     * Retrieve the department's ID
     *
     * @return the department's ID
     */
    public int getId ()
    {
        return id.getValue();
    }

    /**
     * The ID of the next created instance of {@link StandardDepartment}
     *
     * @return the ID of the next instance
     */
    public static int getNextId ()
    {
        return StandardDepartment.NEXT_ID;
    }

    static void setNextId(int nextId)
    {
        NEXT_ID = nextId;
    }

    /**
     * Retrieve the number of employees working in this department
     *
     * @return the number of employees working in this department
     */
    public int getNbEmployees ()
    {
        return employees.size();
    }

    /**
     * Adds a manager to this department as an employee
     *
     * @param manager the employee to add
     * @return this
     * @throws Exception if the manager is managing another department
     */
    public StandardDepartment addEmployee (Manager manager) throws Exception
    {
        if (manager != null)
        {
            // If the manager is managing another department, throw and exception
            if (manager.getManagedDepartment() != null && manager.getManagedDepartment() != this)
            {
                throw new Exception("Impossible to add a manager as an employee if he is the manager of another department.");
            }

            addEmployee((Employee) manager);
        }

        return this;
    }

    /**
     * Adds an employee to this department
     *
     * @param employee the employee to add
     * @return this
     */
    public StandardDepartment addEmployee (Employee employee)
    {
        if (employee != null && !employees.contains(employee))
        {
            // We add this employee the list
            this.employees.add(employee);

            // The former employee's department does not contain him anymore
            StandardDepartment oldDep = (StandardDepartment) employee.getDepartment();
            if (oldDep != null && oldDep != this)
            {
                oldDep.removeEmployee(employee);
            }

            // This employee is now owned by this department
            employee.setDepartment(this);
        }

        return this;
    }

    /**
     * Adds an employee to this department
     *
     * @param employees the employees to add
     * @return this
     */
    public StandardDepartment addAllEmployees (Employee... employees)
    {
        for (Employee e : employees)
        {
            addEmployee(e);
        }

        return this;
    }

    /**
     * Removes an employee from this department
     *
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
     *
     * @param manager the new manager
     * @return this
     */
    public StandardDepartment setManager (Manager manager)
    {
        // The old manager is no longer the manager of any department
        Manager oldManager = this.manager.getValue();
        if (oldManager != null)
        {
            oldManager.setManagedDepartment(null);
        }

        // The new manager is now the manager of this department
        if (manager != null)
        {
            // The new manager is no longer the manager of his old department
            StandardDepartment oldDepartment = manager.getManagedDepartment();

            if (oldDepartment != null)
            {
                oldDepartment.setManager(null);
            }

            // He becomes the manager of this department
            manager.setManagedDepartment(this);
        }

        this.manager.setValue(manager);

        // Finally, he joins the department as an employee.
        this.addEmployee((Employee) manager);

        return this;
    }

    /**
     * Retrieve the manager of the department
     *
     * @return the manager of the department
     */
    public Manager getManager ()
    {
        return manager.getValue();
    }

    public IntegerProperty idProperty ()
    {
        return id;
    }

    public ObservableList<Employee> getEmployees ()
    {
        return employees;
    }

    public void setEmployees (ObservableList<Employee> employees)
    {
        this.employees = employees;
    }

    public ObjectProperty<Manager> managerProperty ()
    {
        return manager;
    }

    /**
     * Removes the department from the company
     */
    public void remove ()
    {
        // The employees of this department no longer have a department
        for (Employee e : employees)
        {
            e.setDepartment(null);
        }

        // The manager is no longer the manager of this department
        if (this.manager != null)
        {
            this.manager.getValue().setManagedDepartment(null);
        }

        // Then We clear the list
        employees.clear();

        // Finally we remove this department from the company
        Company.getCompany().removeStandardDepartment(this);
    }

    /**
     * Creates a String representing the department and all his employees
     *
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
     *
     * @return
     */
    @Override
    public String toString ()
    {
//        return "StandardDepartment : " + getName() + ", activity sector : " + getActivitySector() + ", managed by : " + manager;
        return getName();
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

        json.put(JSON_KEY_ID, id.getValue());
        json.put(JSON_KEY_MANAGER, manager.getValue() == null ? null : manager.getValue().getId());
        employeesArray.addAll(employees.stream().map(Employee::getId).collect(Collectors.toList()));

        json.put(JSON_KEY_EMPLOYEES, employeesArray);

        return json;
    }

    /**
     * Get the list of the employees working in this department
     *
     * @return the list of the employees working int this department
     */
    public ObservableList<Employee> getEmployeesList ()
    {
        return employees;
    }

    public static void loadFromJson (JSONObject json) throws Exception
    {
        String name = json.get(JSON_KEY_NAME).toString();
        String sector = json.get(JSON_KEY_ACTIVITY_SECTOR).toString();
        int id = Integer.parseInt(json.get(JSON_KEY_ID).toString());
        int managerId = Integer.parseInt(json.get(JSON_KEY_MANAGER).toString());
        JSONArray employeesJson = (JSONArray) json.get(JSON_KEY_EMPLOYEES);

        StandardDepartment dep = new StandardDepartment(name, sector, id);

        Company company = Company.getCompany();
        Manager manager = company.getManagementDepartment().getManager(managerId);
        dep.setManager(manager);

        for (Object obj : employeesJson)
        {
            int       empId   = Integer.parseInt(obj.toString());
            Employee  e       = company.getEmployee(empId);
            dep.addEmployee(e);
        }
    }
}
