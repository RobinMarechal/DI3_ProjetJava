package controllers;

import javafx.scene.control.*;
import lib.BaseController;
import lib.annotations.DisplayView;
import lib.annotations.UpdateModel;
import lib.form.Form;
import lib.views.Tabs;
import lib.views.Template;
import models.*;
import org.jetbrains.annotations.NotNull;
import views.departments.DepartmentProfile;
import views.departments.DepartmentsList;
import views.dialogs.EditDepartmentDialog;
import views.dialogs.ManageEmployeesDialog;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * Created by Robin on 26/04/2017. <br>
 * MVC Controller class that handle every <br>
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
    public void show (StandardDepartment dep, @NotNull SimpleDate date)
    {
        if (dep != null)
        {
            final DepartmentProfile view = new DepartmentProfile(dep, date);
            Template.getInstance().setView(Tabs.STANDARD_DEPARTMENTS, view);
        }
        else
        {
            System.err.println("Can't show a department that is null.");
        }
    }

    /**
     * Create a department <br>
     *
     * @param form the form containing the department's information.
     * @return a {@link StandardDepartment} instance, or null if an the form has been unvalidated
     */
    @UpdateModel
    public StandardDepartment createDepartment (@NotNull Form form)
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
     * Open a confirmation box to remove a department <br>
     * The employees are not removed, but they no longer work in a department
     *
     * @param department the department to remove
     */
    @UpdateModel
    public void removeDepartment (@NotNull StandardDepartment department)
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
     * update a department <br>
     *
     * @param department the department to update
     * @param form       the form containing the department's information.
     * @return true if the department was updated, false otherwise
     */
    @UpdateModel
    public boolean updateDepartment (@NotNull StandardDepartment department, @NotNull Form form)
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
        EditDepartmentDialog           dialog    = new EditDepartmentDialog(null, managers, employees);
        dialog.getBtnSubmit().setOnAction(event -> Platform.runLater(() ->
        {
            if (createDepartment(dialog.getForm()) != null)
            {
                final ObservableList<Manager>  managersList  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
                final ObservableList<Employee> employeesList = Company.getCompany().getEmployeesWithoutDepartment();

                dialog.setComboBoxItems(managersList, employeesList);
            }
        }));

    }


    /**
     * Open a dialog box to update a {@link StandardDepartment} instance
     *
     * @param department the department to update
     */
    @DisplayView
    public void openEditionDepartmentDialog (@NotNull StandardDepartment department)
    {
        final ObservableList<Manager>  managers  = ManagementDepartment.getManagementDepartment().getManagersThatDontManage();
        final ObservableList<Employee> employees = Company.getCompany().getEmployeesWithoutDepartment();
        employees.removeAll(managers);

        EditDepartmentDialog dialog = new EditDepartmentDialog(department, managers, employees);
        dialog.getBtnSubmit().setOnAction(event -> Platform.runLater(() ->
        {
            if (updateDepartment(department, dialog.getForm()))
            {
                dialog.close();
            }
        }));
    }


    /**
     * Open a dialog box to manage a {@link StandardDepartment}'s list of employees
     *
     * @param department the department to manage
     */
    @DisplayView
    public void openManageEmployeesDialog (@NotNull StandardDepartment department)
    {
        ManageEmployeesDialog dialog               = new ManageEmployeesDialog(department);
        ListView<Employee>    listOfDepEmployees   = dialog.getListOfDepEmployees();
        ListView<Employee>    listOfNoDepEmployees = dialog.getListOfNoDepEmployees();

        // Add a no-department employee to the department
        dialog.getBtnAddToDep().setOnAction(event -> Platform.runLater(() ->
        {
            // List in case, one day, we allow multiple selection
            final ObservableList<Employee> selectedItems = listOfNoDepEmployees.getSelectionModel().getSelectedItems();
            for (Employee item : selectedItems)
            {
                department.addEmployee(item);
            }
            dialog.fillLists();
            listOfNoDepEmployees.getSelectionModel().select(listOfNoDepEmployees.getItems().size() - 1);
        }));

        // Remove an employee from the department
        dialog.getBtnRemoveFromDep().setOnAction(event -> Platform.runLater(() ->
        {
            // List in case, one day, we allow multiple selection
            final ObservableList<Employee> selectedItems = listOfDepEmployees.getSelectionModel().getSelectedItems();
            for (Employee item : selectedItems)
            {
                department.removeEmployee(item);
            }
            dialog.fillLists();
            listOfDepEmployees.getSelectionModel().select(listOfDepEmployees.getItems().size() - 1);
        }));

        // Done
        dialog.getBtnSubmit().setOnAction(event -> dialog.close());
    }
}
