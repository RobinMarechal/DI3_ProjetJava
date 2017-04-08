import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import models.*;

/**
 * Created by Robin on 27/03/2017.
 */
public class Main
{
   public static void main(String args[])
   {
       Employee e = new Employee("Jean", "Dupont");
       System.out.println(e);
       LocalDate now = LocalDate.now();
       e.doCheck(now, LocalTime.of(7, 55));

       e.doCheck(now, LocalTime.of(17, 3));

       System.out.println(CheckInOut.getTotalChecks());
       System.out.println(e.getCheckInOutAt(now));

       LocalDate tomorrow = now.plusDays(1);

       e.doCheck(tomorrow, LocalTime.of(8, 3));

       System.out.println(e.getCheckInOutAt(tomorrow));

       System.out.println(Company.getCompany());
       System.out.println(Company.getCompany().getBoss());
       System.out.println(Boss.getBoss());

       Manager m = new Manager("Man", "Ager");
       
       System.out.println(m);

       StandardDepartment dep = new StandardDepartment("Dep1", "test", m);
       dep.addEmployee(e);

       System.out.println(dep);
       System.out.println(dep.toStringWithEmployees());

       Employee e2 = new Employee("Emp", "loyé");

       dep.addEmployee(e2);
       
       System.out.println(dep.toStringWithEmployees());
       
       dep.removeEmployee(e);
       
       System.out.println(dep.toStringWithEmployees());
       
       System.out.println(dep.getManager());

       System.out.println(Company.getCompany().getManagementDepartment());
       System.out.println(Company.getCompany().getManagementDepartment().toStringWithManagers());
       
       System.out.println("Employees :");
       System.out.println(Company.getCompany().employeesToString());
       
       System.out.println("---------------------------------------");

       Employee emp = Company.addEmployee("Te", "St");
       Employee emp2 = Company.addEmployee("Test2", "test2");
       Manager man = Company.addManager("Manag", "er");
       StandardDepartment stDep = Company.addStandardDepartment("Department", "test")
               .setManager(man)
               .addEmployee(emp)
               .addEmployee(emp2);

       System.out.println(stDep.toStringWithEmployees());

   }
}
