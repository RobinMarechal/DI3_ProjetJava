package models;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class StandardDepartment extends VirtualDepartment
{
    private static int NEXT_ID = 1;

    private int id;

    private ArrayList<Employee> employees = new ArrayList<>();
    private Manager manager;

    public StandardDepartment(String name, String activitySector, Manager manager) {
        this(name, activitySector);
        setManager(manager);
    }


    public StandardDepartment(String name, String activitySector, int id) throws Exception
    {
        super(name, activitySector);

        if(Company.getCompany().getStandardDepartment(id) != null)
        {
            throw new Exception("There already is an employee with id " + id + ".");
        }

        if(id > StandardDepartment.NEXT_ID)
        {
            StandardDepartment.NEXT_ID = id + 1;
        }

        this.id = id;
        Company.getCompany().addStandardDepartment(this);
    }

    public StandardDepartment(String name, String activitySector) {
        super(name, activitySector);
        id = StandardDepartment.NEXT_ID;
        StandardDepartment.NEXT_ID++;
        Company.getCompany().addStandardDepartment(this);
    }

    public int getId()
    {
        return id;
    }

    public static int getNextId()
    {
        return StandardDepartment.NEXT_ID;
    }

    public StandardDepartment addEmployee(Employee employee)
    {
        if(employee != null && !employees.contains(employee))
        {
            this.employees.add(employee);
            employee.setDepartment(this);
        }

        return this;
    }

    public StandardDepartment removeEmployee(Employee employee) {
        if (employee != null && employees.contains(employee))
        {
            this.employees.remove(employee);
            employee.setDepartment(null);
        }

        return this;
    }

    public StandardDepartment setManager(Manager manager)
    {
        if (this.manager != null)
        {
            this.manager.doesNotManageAnymore(this);
            manager.manages(this);
        }

        this.manager = manager;
        this.addEmployee(manager);

        return this;
    }

    public Manager getManager() {
        return manager;
    }

    public void remove()
    {
        Company.getCompany().removeStandardDepartment(this);
    }

    public String toStringWithEmployees()
    {
        String str;
        int size = employees.size();
        if(employees.size() == 0 )
        {
            str = "There is no employee in the department " + getName() + ".  "; // 2 spaces because there is a substring(0, length-2) later.
        }
        else if(employees.size() == 1)
        {
            str = "There 1 employee in the department " + getName() +" :\n";
        }
        else
        {
            str = "There are " + size + " employees in the department " + getName() +" :\n";
        }

        for(Employee e : employees)
        {
            str += "\t - " + e + ",\n";
        }

        str = str.substring(0, str.length()-2);

        return str;
    }

    @Override
    public String toString() {
        return "StandardDepartment : " + getName() + ", activity sector : " + getActivitySector() + ", managed by : " + manager;
    }
}
