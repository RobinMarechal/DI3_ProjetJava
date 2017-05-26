package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class VirtualDepartment<EmployeesClass> implements Jsonable
{
    protected final static String JSON_KEY_NAME = "name";
    protected final static String JSON_KEY_ACTIVITY_SECTOR = "activitySector";
    protected final static String JSON_KEY_EMPLOYEES = "employees";

    /** The name of the department */
    private StringProperty name = new SimpleStringProperty(this, "name", "");

    /** The activity sector of the department */
    private StringProperty activitySector = new SimpleStringProperty(this, "activitySector", "");

    /** A sorted list of all department's employees <br/>
     * It's sorted in ascending order of the employees' id */
    protected ObservableList<EmployeesClass> employees = FXCollections.observableArrayList();

    /**
     * 2 parameters constructor
     *
     * @param name           The name of the department
     * @param activitySector The activity sector of the department
     */
    public VirtualDepartment (String name, String activitySector)
    {
        this.name.setValue(name);
        this.activitySector.setValue(activitySector);
    }

    /**
     * Retrieve the name of the department
     *
     * @return the name of the department
     */
    public String getName ()
    {

        return name.getValueSafe();
    }

    /**
     * Modifies the name of the department
     *
     * @param name the new name
     * @return this
     */
    public VirtualDepartment setName (String name)
    {
        this.name.setValue(name);
        return this;
    }

    /**
     * Retrieve the activity sector of the department
     *
     * @return the activity sector of the department
     */
    public String getActivitySector ()
    {
        return activitySector.getValueSafe();
    }


    /**
     * Modifies the activity sector of the department
     *
     * @param activitySector the new activity sector
     * @return this
     */
    public VirtualDepartment setActivitySector (String activitySector)
    {
        this.activitySector.setValue(activitySector);
        return this;
    }

    public StringProperty nameProperty ()
    {
        return name;
    }

    public StringProperty activitySectorProperty ()
    {
        return activitySector;
    }

    public ObservableList<EmployeesClass> getEmployees ()
    {
        return employees;
    }

    void setEmployees (ObservableList<EmployeesClass> employees)
    {
        this.employees = employees;
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
     * Creates a String representing the department and all his employees
     *
     * @return
     */
    public String toStringWithEmployees ()
    {
        String str;
        int    size = employees.size();
        if (employees.size() == 0)
        {
            str = "There is no employee in the department " + getName() + ".  "; // 2 spaces because there is a
            // substring(0, length-2) later.
        }
        else if (employees.size() == 1)
        {
            str = "There 1 employee in the department " + getName() + " :" + System.lineSeparator();
        }
        else
        {
            str = "There are " + size + " employees in the department " + getName() + " :" + System.lineSeparator();
        }

        for (EmployeesClass e : employees)
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
        //        return "StandardDepartment : " + getName() + ", activity sector : " + getActivitySector() + ",
        // managed by : " + manager;
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
        JSONObject json = new JSONObject();

        json.put(JSON_KEY_NAME, this.name.getValueSafe());
        json.put(JSON_KEY_ACTIVITY_SECTOR, this.activitySector.getValueSafe());

        return json;
    }
}
