package application.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017. <br/>
 * This template class represents every kinds of departments of the company containing a list of Object (which should be only
 * {@link Employee} or {@link Manager}
 */
public abstract class VirtualDepartment<EmployeesClass> implements Jsonable
{
    /** The JSON key containg the department's name value */
    protected final static String JSON_KEY_NAME = "name";
    /** The JSON key containg the department's activity sector value */
    protected final static String JSON_KEY_ACTIVITY_SECTOR = "activitySector";
    /** The JSON key containg the department's employees array */
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

    /**
     * Get the department's name property
     * @return the department's name property
     */
    public StringProperty nameProperty ()
    {
        return name;
    }

    /**
     * Get the departments activity sector's property
     * @return the departments activity sector's property
     */
    public StringProperty activitySectorProperty ()
    {
        return activitySector;
    }

    /**
     * Get the list of employees
     * @return
     */
    public ObservableList<EmployeesClass> getEmployees ()
    {
        return employees;
    }

    /**
     * Set the list of employees <br/>
     * <b>WARNING: this method is unsafe for the program coherence </b>
     * @param employees the new list of employees
     */
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
     * Creates a String representing the department
     *
     * @return
     */
    @Override
    public String toString ()
    {
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
