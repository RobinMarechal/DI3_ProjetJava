package models;

import java.util.ArrayList;

/**
 * Created by Robin on 27/03/2017.
 */
public class Company {
    private static Company companyInstance = new Company();

    private String name = "Best Company Ever !";

    // Relations
    private Boss bossInstance = new Boss();
    private ManagementDepartment managementDepartmentInstance = new ManagementDepartment();
    private ArrayList<StandardDepartment> departmentsList = new ArrayList<>();
    private ArrayList<Employee> employeesList = new ArrayList<>();

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

    public void setName(String name) {
        this.name = name;
    }
}
