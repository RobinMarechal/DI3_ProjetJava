package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class VirtualDepartment implements Jsonable, Serializable
{
    /** The name of the department  */
    private StringProperty name = new SimpleStringProperty(this, "name", "");

    /** The activity sector of the department */
    private StringProperty activitySector = new SimpleStringProperty(this, "activitySector", "");

    /**
     * 2 parameters constructor
     * @param name The name of the department
     * @param activitySector The activity sector of the department
     */
    public VirtualDepartment(String name, String activitySector) {
        this.name.setValue(name);
        this.activitySector.setValue(activitySector);
    }

    /**
     * Retrieve the name of the department
     * @return the name of the department
     */
    public String getName() {

        return name.getValueSafe();
    }

    /**
     * Modifies the name of the department
     * @param name the new name
     * @return this
     */
    public VirtualDepartment setName(String name) {
        this.name.setValue(name);
        return this;
    }

    /**
     * Retrieve the activity sector of the department
     * @return the activity sector of the department
     */
    public String getActivitySector() {
        return activitySector.getValueSafe();
    }


    /**
     * Modifies the activity sector of the department
     * @param activitySector the new activity sector
     * @return this
     */
    public VirtualDepartment setActivitySector(String activitySector) {
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

    /**
     * Creates a String representing the department
     * @return
     */
    @Override
    public String toString() {
        return "VirtualDepartment{" +
                "name='" + name.getValueSafe() + '\'' +
                ", activitySector='" + activitySector.getValueSafe() + '\'' +
                '}';


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

        json.put("name", this.name.getValueSafe());
        json.put("activitySector", this.activitySector.getValueSafe());

        return json;
    }
}
