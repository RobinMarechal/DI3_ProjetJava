package models;

/**
 * Created by Robin on 27/03/2017.
 */
public class Manager extends Employee
{
//    private ManagementDepartment managementDepartment = Company.getCompany().getManagementDepartment();
    private StandardDepartment managedDepartment;

    public Manager(String firstName, String lastName)
    {
        super(firstName, lastName);
        Company.getCompany().getManagementDepartment().addManager(this);
    }

    public Manager(String firstName, String lastName, int id) throws Exception
    {
        super(firstName, lastName, id);
    }

    public boolean isManagerOf(StandardDepartment department)
    {
        return this.managedDepartment == department;
    }

    public StandardDepartment getManagedDepartment()
    {
        return managedDepartment;
    }

    public Manager manages(StandardDepartment department)
    {
        if(this.managedDepartment != department)
        {
            department.setManager(this);
        }

        if(department == null && this.managedDepartment != null)
        {
            doesNotManageAnymore(this.managedDepartment);
        }

        this.managedDepartment = department;


        return this;
    }

    public Manager becomesManagerOf(StandardDepartment department)
    {
        manages(department);
        return this;
    }

    public Manager doesNotManageAnymore(StandardDepartment department)
    {
        if(department == null || department == managedDepartment)
        {
            return this;
        }

        department.setManager(null);
        managedDepartment = null;
        return this;
    }

    @Override
    public Manager fire()
    {
        super.fire();
        Company.getCompany().getManagementDepartment().removeManager(this);
        return this;
    }

    @Override
    public String toString() {
        String name = "no department";
        if(managedDepartment != null)
        {
            name = managedDepartment.getName();
        }

        return super.toString() + " (Manager of "+ name +")";
    }
}
