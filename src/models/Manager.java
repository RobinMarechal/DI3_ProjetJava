package models;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class Manager extends Employee
{
    private ManagementDepartment managementDepartment = Company.getCompany().getManagementDepartment();
    private ArrayList<StandardDepartment> managedDepartments = new ArrayList<>();

    public Manager() { }

    public Manager(String firstName, String lastName)
    {
        super(firstName, lastName);
    }

    public Manager(String firstName, String lastName, StandardDepartment department)
    {
        this(firstName, lastName);
        this.managedDepartments.add(department);
    }

    public boolean isManagerOf(StandardDepartment department)
    {
        return managedDepartments.contains(department);
    }

    public void manages(StandardDepartment department) {
        if(!isManagerOf(department))
            managedDepartments.add(department);
    }

    public void doesNotManageAnymore(StandardDepartment department)
    {
        if(isManagerOf(department))
            managedDepartments.remove(department);
    }
}
