package models;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class StandardDepartment extends VirtualDepartment
{
    private ArrayList<Employee> employees = new ArrayList<>();
    private Company company =  Company.getCompany();
    private ArrayList<Manager> managers = new ArrayList<>();

    public void addEmployee(Employee employee)
    {
        if(!employees.contains(employee))
            this.employees.add(employee);
    }

    public void removeEmployee(Employee employee)
    {
        if(employees.contains(employee))
            this.employees.remove(employee);
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
