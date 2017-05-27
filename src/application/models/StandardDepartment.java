package application.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import application.lib.json.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.stream.Collectors;

/**
 * Created by Robin on 27/03/2017.
 */
public class StandardDepartment extends VirtualDepartment<Employee> implements Jsonable
{
    protected static final String JSON_KEY_ID = "id";
    protected static final String JSON_KEY_MANAGER = "manager";

    /** The ID of the next instance */
    private static int NEXT_ID = 1;

    /** The ID of the instance */
    private IntegerProperty id = new SimpleIntegerProperty(this, "id", -1);

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
    public StandardDepartment (String name, String activitySector, int id)
    {
        super(name, activitySector);

        if (Company.getCompany().getStandardDepartment(id) != null)
        {
            System.err.print("A department with the id " + id + " already exists... ");
            id = NEXT_ID;
            System.err.println("The department was created with id " + id + " instead.");
        }

        this.id.setValue(id);
        Company.getCompany().addStandardDepartment(this);

        if (id >= StandardDepartment.NEXT_ID)
        {
            StandardDepartment.updateNextId();
        }
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
        Company.getCompany().addStandardDepartment(this);
        StandardDepartment.updateNextId();
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

    private static void updateNextId ()
    {
        final ObservableList<StandardDepartment> employees = Company.getCompany().getStandardDepartmentsList();
        NEXT_ID = employees.get(employees.size() - 1).getId() + 1;
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
            //            this.employees.add(employee);
            int     id    = employee.getId();
            boolean added = false;
            for (int i = 0; i < employees.size() && !added; i++)
            {
                if (id < employees.get(i).getId())
                {
                    employees.add(i, employee);
                    added = true;
                }
            }

            if (!added)
            {
                employees.add(employee);
            }

            // The former employee's department does not contain him anymore
            StandardDepartment oldDep = employee.getDepartment();
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
        if (employee != null && employees.contains(employee) && employee != this.manager.getValue())
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
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json           = super.toJson();
        JSONArray  employeesArray = new JSONArray();

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

    public static StandardDepartment loadFromJson (JSONObject json)
    {
        String    name          = json.get(JSON_KEY_NAME).toString();
        String    sector        = json.get(JSON_KEY_ACTIVITY_SECTOR).toString();
        int       id            = Integer.parseInt(json.get(JSON_KEY_ID).toString());
        int       managerId     = Integer.parseInt(json.get(JSON_KEY_MANAGER).toString());
        JSONArray employeesJson = (JSONArray) json.get(JSON_KEY_EMPLOYEES);

        StandardDepartment dep;
        dep = new StandardDepartment(name, sector, id);

        Company company = Company.getCompany();
        Manager manager = company.getManagementDepartment().getManager(managerId);
        dep.setManager(manager);

        for (Object obj : employeesJson)
        {
            int      empId = Integer.parseInt(obj.toString());
            Employee e     = company.getEmployee(empId);
            dep.addEmployee(e);
        }

        return dep;
    }
}
