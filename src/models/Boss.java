package models;

/**
 * Created by Robin on 27/03/2017.
 */
public class Boss extends Person
{
    private static Boss bossInstance = new Boss();

//    private ManagementDepartment managementDepartment = ManagementDepartment.getManagementDepartment();

    public Boss()
    {
        setFirstName("Boss");
        setLastName("of the Company");
    }


    public static Boss getBoss()
    {
        return bossInstance;
    }

    @Override
    public String toString() {
        return "Boss : " + getFirstName() + " " + getLastName();
    }
}
