package models;

/**
 * Created by Robin on 27/03/2017.
 */
public class Boss extends Person
{
    private ManagementDepartment managementDepartment = Company.getCompany().getManagementDepartment();

    public Boss()
    {
        setFirstName("Boss");
        setLastName("of the Company");
    }
}
