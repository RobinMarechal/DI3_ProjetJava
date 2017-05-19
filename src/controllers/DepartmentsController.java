package controllers;

import javafx.collections.ObservableList;
import lib.BaseController;
import lib.views.Tabs;
import lib.views.Template;
import models.Company;
import models.StandardDepartment;
import views.DepartmentsViewController;
import views.departments.DepartmentsList;

/**
 * Created by Robin on 26/04/2017.
 */
public class DepartmentsController extends BaseController
{
    @Override
    public void home ()
    {
        ObservableList<StandardDepartment> departments = Company.getCompany().getStandardDepartmentsList();
        DepartmentsViewController view = new DepartmentsList(departments);

        departments = Company.getCompany().getStandardDepartmentsList();

        Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
    }

    public void show (int id)
    {
        StandardDepartment dep = Company.getCompany().getStandardDepartment(id);
        show(dep);
    }

    public void show (StandardDepartment dep)
    {
        System.out.println("Show standard department n°" + dep.getId() + "...");
    }
}
