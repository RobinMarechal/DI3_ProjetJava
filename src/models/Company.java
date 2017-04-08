package models;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class Company {
    // Singleton
    private static Company companyInstance = new Company();

    private String name = "Best Company Ever !";

    // Relations
    private Boss bossInstance = Boss.getBoss();
    private ManagementDepartment managementDepartmentInstance = ManagementDepartment.getManagementDepartment();

    private ArrayList<StandardDepartment> departments = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>();

    private Company() { }

    public static Company getCompany() {
        return companyInstance;
    }

    public Boss getBoss() {
        return bossInstance;
    }

    public ManagementDepartment getManagementDepartment() {
        return managementDepartmentInstance;
    }

    public String getName() {
        return name;
    }

    public Company setName(String name) {
        this.name = name;
        return this;
    }

    public Company addEmployee(Employee employee)
    {
        if(employee != null && !employees.contains(employee))
        {
            employees.add(employee);
        }

        return this;
    }

    public Company fire(Employee employee)
    {
        if(employee == null)
            return this;

        employee.fire();
        employee = null;
        return this;
    }

    public Company fire(Manager manager)
    {
        if(manager == null)
            return this;

        manager.fire();
        manager = null;
        return this;
    }

    public Company removeEmployee(Employee employee)
    {
        if(employee != null && employees.contains(employee))
        {
            employees.remove(employee);
            employee.fire();
        }

        return this;
    }

    public static Employee addEmployee(String firstName, String lastName)
    {
        return new Employee(firstName, lastName);
    }

    public static Manager addManager(String firstName, String lastName)
    {
        return new Manager(firstName, lastName);
    }

    public Employee getEmployee(int id)
    {
        if(id < 0)
            return null;

        for (Employee e : employees)
        {
            if(e.getId() == id)
                return e;
        }

        return null;
    }

    public int getNbEmployees()
    {
        return employees.size();
    }

    public StandardDepartment getStandardDepartment(int id)
    {
        if(id < 0)
            return null;

        for (StandardDepartment dep : departments)
        {
            if(dep.getId() == id)
                return dep;
        }

        return null;
    }

    public int getNbStandardDepartments() { return departments.size(); }

    public Company addStandardDepartment(StandardDepartment department)
    {
        departments.add(department);
        return this;
    }

    public static StandardDepartment addStandardDepartment(String name, String activitySector)
    {
        StandardDepartment department = new StandardDepartment(name, activitySector);
        return department;
    }

    public static StandardDepartment addStandardDepartment(String name, String activitySector, Manager manager)
    {
        StandardDepartment dep = addStandardDepartment(name, activitySector);
        dep.setManager(manager);
        return dep;
    }

    public Company removeStandardDepartment(StandardDepartment department)
    {
        if(department != null && departments.contains(department))
        {
            departments.remove(department);
            department = null;
        }

        return this;
    }

    /*
        SEARCHES
     */

    public ArrayList<Employee> searchEmployee(String firstName, String lastName)
    {
        ArrayList<Employee> list = new ArrayList<>();

        for(Employee e : employees)
        {
            if(e.getFirstName().toLowerCase().contains(firstName.toLowerCase())
                    && e.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            {
                list.add(e);
            }
        }

        return list;
    }

    public ArrayList<StandardDepartment> searchStandardDepartment(String name, String activitySector)
    {
        ArrayList<StandardDepartment> list = new ArrayList<>();

        for(StandardDepartment e : departments)
        {
            if(e.getName().toLowerCase().contains(name.toLowerCase())
                    && e.getActivitySector().toLowerCase().contains(activitySector.toLowerCase()))
            {
                list.add(e);
            }
        }

        return list;
    }

    /*
        toString methods
     */


    public String employeesToString()
    {
        String str = "There are/is " + employees.size() + " employee(s) : \n  ";

        for(Employee e : employees)
        {
            str += "\t - " + e + ",\n";
        }

        str = str.substring(0, str.length() - 2);

        return str;
    }

    @Override
    public String toString() {
        return "Company : " + name;
    }
}
