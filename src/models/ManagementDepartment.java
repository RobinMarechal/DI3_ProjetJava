package models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Robin on 27/03/2017.
 */
public class ManagementDepartment extends VirtualDepartment
{
    // Relations
    private Company company = Company.getCompany();
    private Boss boss = company.getBoss();
    private ArrayList<Manager> managers = new ArrayList<>();

    public ManagementDepartment()
    {
        setName("Management Department");
        setActivitySector("Management");
    }

    public void addManager(Manager manager)
    {
        if(!managers.contains(manager))
            this.managers.add(manager);
    }

    public void removeManager(Manager manager)
    {
        if(managers.contains(manager))
            this.managers.remove(manager);
    }
}
