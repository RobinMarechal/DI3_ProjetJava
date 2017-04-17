import models.Company;
import models.Employee;
import models.Manager;

/**
 * Created by Robin on 27/03/2017.
 */
public class Main
{
    public static void main (String args[])
    {
        Employee e = new Employee("", "");
        Manager m = new Manager("", "");

        Company.getCompany().removeEmployee(m);

        System.out.println(Company.getCompany().getNbEmployees());
    }
}
