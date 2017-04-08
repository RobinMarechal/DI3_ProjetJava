package models;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class ManagementDepartment extends VirtualDepartment
{
    // Singleton
    private static ManagementDepartment managementDepartmentInstance = new ManagementDepartment();

    // Relations
    private ArrayList<Manager> managers = new ArrayList<>();

    public static ManagementDepartment getManagementDepartment() {
        return managementDepartmentInstance;
    }

    public ManagementDepartment()
    {
    	super("Management Department", "Management");
    }

    public int getNbManagers()
    {
        return managers.size();
    }

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

    public ManagementDepartment addManager(Manager manager)
    {
        if(manager != null && !managers.contains(manager))
        {
            this.managers.add(manager);
        }

        return this;
    }

    public ManagementDepartment removeManager(Manager manager)
    {
        if(manager != null && managers.contains(manager))
        {
            managers.remove(manager);
            manager.becomesManagerOf(null);
        }

        return this;
    }

    public String toStringWithManagers()
    {
        String str = "List of managers : \n ";
        for (Manager m : managers)
        {
            str += "\t - " + m + "\n";
        }

        return str;
    }

    @Override
    public String toString() {
        return getName() + " : activity sector : " + getActivitySector() + ", managed by the " + Boss.getBoss();
    }
}
