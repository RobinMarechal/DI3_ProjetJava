package models;

import lib.json.JsonSaver;
import lib.json.Jsonable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Robin on 27/03/2017.
 */
public class Manager extends Employee implements Jsonable, JsonSaver
{
    /**
     * The {@link StandardDepartment} instance managed by this manager
     */
    private StandardDepartment managedDepartment;

    /**
     * 2 parameters constructor <br/>
     * Creates an instance of {@link Manager} and gives it a unique ID (based on NEXT_ID static field) <br/>
     * To force a specific ID, use the 3 parameters constructor.
     *
     * @param firstName The first-name of the manager
     * @param lastName The last-name of the manager
     */
    public Manager(String firstName, String lastName)
    {
        super(firstName, lastName);
        Company.getCompany().getManagementDepartment().addManager(this);
    }

    /**
     * 3 parameters constructor <br />
     * Creates an instance of {@link Manager} with a specific unique ID <br/>
     * If you don't know which ID to give to the manager, use the 2 parameters constructor.
     *
     * @param firstName The first-name of the manager
     * @param lastName The last-name of the manager
     * @param id the ID of the manager.
     * @throws Exception if there already is an {@link Manager} or {@link Employee} instance (by inheritance) with the given ID.
     */
    public Manager(String firstName, String lastName, int id) throws Exception
    {
        super(firstName, lastName, id);
        Company.getCompany().getManagementDepartment().addManager(this);
    }

    /**
     * To know whether the current {@link Manager} instance is the manager of the given {@link StandardDepartment} instance.
     * @param department the {@link StandardDepartment} instance you want to test
     * @return True if the manager manages the department, false altherwise.
     */
    public boolean isManagerOf(StandardDepartment department)
    {
        return this.managedDepartment == department;
    }

    /**
     * Retrieve the {@link StandardDepartment} managed by this manager
     * @return the standard department managed or null of the manger doesn't manage any department
     */
    public StandardDepartment getManagedDepartment()
    {
        return managedDepartment;
    }

    /**
     * Fires a manager
     * @return this
     */
    @Override
    public Manager fire()
    {
        super.fire();
        Company.getCompany().getManagementDepartment().removeManager(this);
        return this;
    }

    /**
     * Creates a String representing a manager.
     * @return
     */
    @Override
    public String toString() {
        String name = "no department";
        if(managedDepartment != null)
        {
            name = managedDepartment.getName();
        }

        return super.toString() + " (Manager of "+ name +")";
    }

    /**
     * Create a {@link JSONObject} from a Manager instance.
     * @return the {@link JSONObject}
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = super.toJson();
        json.put("manager", true);

        return json;
    }

    /**
     * Modifies the managed department.
     * @param managedDepartment the new managed department
     * @return this
     * @warnin this method should only be used by Models' class.
     */
    protected Manager setManagedDepartment (StandardDepartment managedDepartment)
    {
        if(this.managedDepartment != null)
        {
            StandardDepartment tmp = this.managedDepartment;
            this.managedDepartment = managedDepartment;
            tmp.setManager(null);
        }

        this.managedDepartment = managedDepartment;

        return this;
    }
}
