package application.controllers;

import application.lib.BaseController;
import application.lib.annotations.DisplayView;
import application.lib.annotations.UpdateModel;
import application.lib.form.Form;
import application.lib.views.Tabs;
import application.lib.views.Template;
import application.models.*;
import application.views.departments.DepartmentProfile;
import application.views.departments.DepartmentsList;
import application.views.dialogs.EditDepartmentDialog;
import application.views.dialogs.ManageEmployeesDialog;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * Created by Robin on 26/04/2017. <br/>
 * MVC Controller class that handle every <br/>
 * actions related to the departments data
 */
public class DepartmentsController extends BaseController
{
    /**
     * Display the departments tab home view
     */
    @Override
    @DisplayView
    public void home ()
    {
        ObservableList<StandardDepartment> departments = Company.getCompany().getStandardDepartmentsList();
        final DepartmentsList              view        = new DepartmentsList(departments);

        Company.getCompany().getStandardDepartmentsList();

        Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
    }

    /**
     * Display the department profile view, with employees' checks at a specific date
     *
     * @param dep  The department to display
     * @param date the date of checks
     */
    @DisplayView
    public void show (StandardDepartment dep, SimpleDate date)
    {
        if (dep != null)
        {
            final DepartmentProfile view = new DepartmentProfile(dep, date);
            Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
        }
    }

    /**
     * Create a department <br/>
     *
     * @param form the form containing the department's information.
     * @return a {@link StandardDepartment} instance, or null if an the form has been unvalidated
     */
    @UpdateModel
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

            Employee comboBoxResult;
            String   name;
            String   sector;

            name = ((TextField) form.get("name").getField()).getText();
            sector = ((TextField) form.get("activitySector").getField()).getText();
            comboBoxResult = ((ComboBox<Employee>) form.get("manager").getField()).getValue();

            if (comboBoxResult == null)
            {
                form.get("manager").unValidate();
                return null;
            }
            else
            {
                form.get("manager").validate();
            }

            Manager manager = comboBoxResult.upgradeToManager();

            dep = new StandardDepartment(name, sector, manager);

            form.clearAll();

            return dep;
        }
    }

    /**
     * Open a confirmation box to remove a department <br/>
     * The employees are not removed, but they no longer work in a department
     *
     * @param department the department to remove
     */
    @UpdateModel
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

    /**
     * update a department <br/>
     *
     * @param department the department to update
     * @param form       the form containing the department's information.
     * @return true if the department was updated, false otherwise
     */
    @UpdateModel
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

    /**
     * Open a dialog box to create a {@link StandardDepartment} instance
     */
    @DisplayView
    public void openCreationDepartmentDialog ()
    {
        final ObservableList<Manager>  managers  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
        final ObservableList<Employee> employees = Company.getCompany().getEmployeesWithoutDepartment();
        EditDepartmentDialog dialog = new EditDepartmentDialog(null, managers, employees);
        dialog.getBtnSubmit().setOnAction(event -> new Thread(() -> Platform.runLater(() ->
        {
            if (createDepartment(dialog.getForm()) != null)
            {
                final ObservableList<Manager>  managersList  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
                final ObservableList<Employee> employeesList = Company.getCompany().getEmployeesWithoutDepartment();

                dialog.setComboBoxItems(managersList, employeesList);
            }
        })).start());

    }


    /**
     * Open a dialog box to update a {@link StandardDepartment} instance
     *
     * @param department the department to update
     */
    @DisplayView
    public void openEditionDepartmentDialog (StandardDepartment department)
    {
        final ObservableList<Manager>  managers  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
        final ObservableList<Employee> employees = Company.getCompany().getEmployeesWithoutDepartment();
        employees.removeAll(managers);
        EditDepartmentDialog dialog = new EditDepartmentDialog(department, managers, employees);
        dialog.getBtnSubmit().setOnAction(event ->
        {
            new Thread(() -> Platform.runLater(() ->
            {
                if (updateDepartment(department, dialog.getForm()))
                {
                    dialog.close();
                }
            })).start();
        });
    }


    /**
     * Open a dialog box to manage a {@link StandardDepartment}'s list of employees
     */
    @DisplayView
    public void openManageEmployeesDialog (StandardDepartment department)
    {
        new ManageEmployeesDialog(department, Company.getCompany());
    }
}
