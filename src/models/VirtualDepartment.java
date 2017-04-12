package models;

import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class VirtualDepartment implements Jsonable
{
    /**
     * The name of the department
     */
    private String name;

    /**
     * The activity sector of the department
     */
    private String activitySector;

    /**
     * 2 parameters constructor
     * @param name The name of the department
     * @param activitySector The activity sector of the department
     */
    public VirtualDepartment(String name, String activitySector) {
        this.name = name;
        this.activitySector = activitySector;
    }

    /**
     * Retrieve the name of the department
     * @return the name of the department
     */
    public String getName() {

        return name;
    }

    /**
     * Modifies the name of the department
     * @param name the new name
     * @return this
     */
    public VirtualDepartment setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Retrieve the activity sector of the department
     * @return the activity sector of the department
     */
    public String getActivitySector() {
        return activitySector;
    }


    /**
     * Modifies the activity sector of the department
     * @param activitySector the new activity sector
     * @return this
     */
    public VirtualDepartment setActivitySector(String activitySector) {
        this.activitySector = activitySector;
        return this;
    }

    /**
     * Creates a String representing the department
     * @return
     */
    @Override
    public String toString() {
        return "VirtualDepartment{" +
                "name='" + name + '\'' +
                ", activitySector='" + activitySector + '\'' +
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

        json.put("name", this.name);
        json.put("activitySector", this.activitySector);

        return json;
    }
}
