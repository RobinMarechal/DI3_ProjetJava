package controllers;

import fr.etu.univtours.marechal.SimpleDate;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lib.BaseController;
import lib.util.form.Form;
import lib.views.Tabs;
import lib.views.Template;
import models.*;
import views.departments.DepartmentProfile;
import views.departments.DepartmentsList;
import views.dialogs.CreateDepartmentDialog;
import views.dialogs.EditDepartmentDialog;
import views.dialogs.ManageEmployeesDialog;

import java.util.Optional;

/**
 * Created by Robin on 26/04/2017.
 */
public class DepartmentsController extends BaseController
{
    @Override
    public void home ()
    {
        ObservableList<StandardDepartment> departments = Company.getCompany().getStandardDepartmentsList();
        final DepartmentsList              view        = new DepartmentsList(departments);

        Company.getCompany().getStandardDepartmentsList();

        Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
    }

    public void show (int id, SimpleDate date)
    {
        StandardDepartment dep = Company.getCompany().getStandardDepartment(id);
        show(dep, date);
    }

    public void show (StandardDepartment dep, SimpleDate date)
    {
        if (dep != null)
        {
            final DepartmentProfile view = new DepartmentProfile(dep, date);
            Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
        }
    }

    public void openCreationDepartmentDialog ()
    {
        final ObservableList<Manager>  managers  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
        final ObservableList<Employee> employees = Company.getCompany().getEmployeesWithoutDepartment();
        employees.removeAll(managers);
        new CreateDepartmentDialog(managers, employees);
    }

    public StandardDepartment createDepartment (Form form)
    {
        boolean allPassed = validateForm(form);

        if (!allPassed)
        {
            return null;
        }
        else
        {
            StandardDepartment dep;
            Employee           comboBoxResult;
            String             name;
            String             sector;

            name = ((TextField) form.get("name").getField()).getText();
            sector = ((TextField) form.get("activitySector").getField()).getText();
            comboBoxResult = ((ComboBox<Employee>) form.get("manager").getField()).getValue();

            if (comboBoxResult == null)
            {
                form.get("manager").unValidate();
                return null;
            }

            Manager manager = comboBoxResult.upgradeToManager();

            dep = new StandardDepartment(name, sector, manager);

            return dep;
        }
    }

    public void removeDepartment (StandardDepartment department)
    {
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
        String title = "Removing " + department.getName();
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText("Are you sure? \nThis can't be undone!\nThe employees won't be fired.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            department.remove();
            home();
        }

        alert.close();
    }


    public void openEditionDepartmentDialog (StandardDepartment department)
    {
        final ObservableList<Manager>  managers  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
        final ObservableList<Employee> employees = Company.getCompany().getEmployeesWithoutDepartment();
        employees.removeAll(managers);
        new EditDepartmentDialog(department, managers, employees);
    }

    public boolean updateDepartment (StandardDepartment department, Form form)
    {
        boolean allPassed = validateForm(form);

        if (!allPassed)
        {
            return false;
        }
        else
        {
            String   name    = ((TextField) form.get("name").getField()).getText();
            String   sector  = ((TextField) form.get("activitySector").getField()).getText();
            Employee manager = ((ComboBox<Employee>) form.get("manager").getField()).getValue();

            department.setName(name);
            department.setActivitySector(sector);

            if (manager != null)
            {
                department.setManager(manager.upgradeToManager());
            }

            return true;
        }
    }

    public void openManageEmployeesDialog (StandardDepartment department)
    {
        new ManageEmployeesDialog(department, Company.getCompany());
    }
}
