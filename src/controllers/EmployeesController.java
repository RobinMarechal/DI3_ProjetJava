package controllers;

import javafx.collections.ObservableList;
import lib.BaseController;
import lib.time.SimpleDate;
import lib.views.BaseViewController;
import lib.views.Tabs;
import lib.views.Template;
import models.Company;
import models.Employee;
import views.employees.EmployeeList;
import views.employees.EmployeeProfile;

/**
 * Created by Robin on 25/04/2017.
 */
public class EmployeesController extends BaseController
{
    public EmployeesController ()
    {
    }

    @Override
    public void home ()
    {
        listAt(SimpleDate.TODAY);
    }

    public void listAt(SimpleDate date)
    {
        ObservableList<Employee> employees = Company.getCompany().getEmployeesList();
        BaseViewController view = new EmployeeList(employees, date);

        Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    public void show (int id)
    {
        Employee employee = Company.getCompany().getEmployee(id);
        show(employee);

        // Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    public void show (Employee employee)
    {
        System.out.println("Showing employee n°" + employee.getId() + "...");
        BaseViewController view = new EmployeeProfile(employee);

        Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }
}
